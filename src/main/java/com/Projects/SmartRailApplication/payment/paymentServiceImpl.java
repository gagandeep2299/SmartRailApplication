package com.Projects.SmartRailApplication.payment;

import com.Projects.SmartRailApplication.payment.Entities.PaymentStatus;
import com.Projects.SmartRailApplication.payment.Entities.payment;
import com.Projects.SmartRailApplication.payment.dto.PaymentRequest;
import com.Projects.SmartRailApplication.payment.dto.PaymentResponse;
import com.Projects.SmartRailApplication.payment.dto.MockPaymentRequest;
import com.Projects.SmartRailApplication.payment.dto.MockPaymentResponse;
import com.Projects.SmartRailApplication.payment.mock.MockPaymentOutcome;
import com.Projects.SmartRailApplication.payment.mock.MockPaymentServer;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.DuplicateKeyException;

import com.Projects.SmartRailApplication.booking.Entities.Booking;
import com.Projects.SmartRailApplication.booking.Entities.BookingSeat;
import com.Projects.SmartRailApplication.booking.Entities.SeatReservation;
import com.Projects.SmartRailApplication.booking.Entities.enums.BookingStatus;
import com.Projects.SmartRailApplication.booking.Entities.enums.ReservationStatus;
import com.Projects.SmartRailApplication.booking.repository.bookingRepository;
import com.Projects.SmartRailApplication.booking.repository.seatReservationRepository;

@Service
@RequiredArgsConstructor
public class paymentServiceImpl implements paymentService {
    private final paymentRepository paymentRepository;
    private final bookingRepository bookingRepository;
    private final seatReservationRepository seatReservationRepository;
    private final MockPaymentServer mockPaymentServer;

    @Value("${mock.payment.default-outcome:SUCCESS}")
    private MockPaymentOutcome defaultPaymentOutcome;

    @Override 
    @Transactional 
    public PaymentResponse initiatePayment(Long bookingId,PaymentRequest paymentRequest){
        String idempotencyKey = paymentRequest.idempotencyKey().trim();
        Booking booking = bookingRepository.findByIdForUpdate(bookingId).orElseThrow(()-> new IllegalArgumentException("Booking not found with ID: " + bookingId));
        if (paymentRepository.existsByIdempotencyKey(idempotencyKey)) {
            throw new DuplicateKeyException("payment with same Idempotent key already processed");
        };
        
        if(booking.getBookingStatus() != BookingStatus.PENDING_PAYMENT){
            throw new IllegalStateException("Payment can only be initiated for bookings with PENDING_PAYMENT status.");
        }
        
        LocalDateTime now = LocalDateTime.now();
        List<SeatReservation> seatReservations = seatReservationRepository.findByBookingId(bookingId);
        for(SeatReservation seatReservation : seatReservations){
            if(seatReservation.getStatus() != ReservationStatus.HELD ||
               seatReservation.getExpiresAt().isBefore(now)){
                throw new IllegalStateException("payment for the seat can only be initiated if the seat is currently held and not expired.");
            }
        }

        
        BigDecimal seatAmount = BigDecimal.ZERO;
        for(BookingSeat bookingSeat : booking.getBookingSeats()){
            seatAmount = seatAmount.add(bookingSeat.getPrice());
        }
        if(seatAmount.compareTo(booking.getTotalPrice()) != 0){
            throw new IllegalArgumentException("Payment amount does not match the total price of the seats.");
        }
        payment payment = new payment();
        payment.setBooking_Id(booking);
        payment.setAmount(booking.getTotalPrice());
        payment.setPaymentStatus(PaymentStatus.PENDING);
        payment.setIdempotencyKey(idempotencyKey);
        payment.setCreatedAt(now);
        payment.setUpdatedAt(now);
        paymentRepository.saveAndFlush(payment);

        MockPaymentResponse gatewayResponse = mockPaymentServer.charge(
            new MockPaymentRequest(booking.getTotalPrice(), idempotencyKey, defaultPaymentOutcome));
        
        if(gatewayResponse.outcome() == MockPaymentOutcome.FAILED){
            payment.setPaymentStatus(PaymentStatus.FAILED);
            payment.setPaymentReference(gatewayResponse.transactionReference());
            payment.setUpdatedAt(now);
            for(SeatReservation seatReservation : seatReservations){
                seatReservation.release();
            }
            booking.setBookingStatus(BookingStatus.EXPIRED);
            return mapPayment(payment);
        } else if(gatewayResponse.outcome() == MockPaymentOutcome.PENDING){
            payment.setPaymentStatus(PaymentStatus.PENDING);
            payment.setPaymentReference(gatewayResponse.transactionReference());
            payment.setUpdatedAt(now);
            return mapPayment(payment);    
        }
        
        for(SeatReservation seatReservation : seatReservations){
            if(seatReservation.getStatus() != ReservationStatus.HELD ||
               seatReservation.getExpiresAt().isBefore(now)){

                initiateRefund(payment,gatewayResponse.transactionReference());

                throw new IllegalStateException("payment for the seat can only be confirmed if the seat is currently held and not expired.");
            }
        }

        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        payment.setPaymentReference(gatewayResponse.transactionReference());
        payment.setUpdatedAt(now);

        for(SeatReservation seatReservation : seatReservations){
            seatReservation.confirm();
            seatReservation.getBookingSeat().setBookingStatus(BookingStatus.CONFIRMED);
        }

        booking.setBookingStatus(BookingStatus.CONFIRMED);
        return mapPayment(payment);
    }

    private void initiateRefund(payment payment,String paymentReference) {
    // TODO: call the real/mock gateway refund API.
        payment.setPaymentStatus(PaymentStatus.REFUNDED_PENDING);
        payment.setPaymentReference(paymentReference);
        payment.setUpdatedAt(LocalDateTime.now());

        for (SeatReservation reservation :
            seatReservationRepository.findByBookingId(
                    payment.getBooking_Id().getId())) {
            reservation.release();
        }

        payment.getBooking_Id().setBookingStatus(
                BookingStatus.REFUND_PENDING);
}

    @Override
    @Transactional
    public PaymentResponse getPayment(Long bookingId, String idempotencyKey) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new IllegalArgumentException("Booking not found with ID: " + bookingId));
        payment payment = paymentRepository.findByBookingAndKeyForUpdate(bookingId, idempotencyKey)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found for the given booking ID and idempotency key."));
        if (!payment.getBooking_Id().getId().equals(booking.getId())) {
            throw new IllegalArgumentException("Payment does not belong to the specified booking.");
        }
        return mapPayment(payment);
    }

    private PaymentResponse mapPayment(payment payment) {
    return PaymentResponse.builder()
            .id(payment.getId())
            .bookingId(payment.getBooking_Id().getId())
            .amount(payment.getAmount())
            .paymentStatus(payment.getPaymentStatus())
            .transactionReference(payment.getPaymentReference())
            .build();
}
}
