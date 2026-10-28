package com.Projects.SmartRailApplication.payment;

import com.Projects.SmartRailApplication.payment.Entities.PaymentStatus;
import com.Projects.SmartRailApplication.payment.Entities.payment;
import com.Projects.SmartRailApplication.payment.dto.PaymentRequest;
import com.Projects.SmartRailApplication.payment.dto.PaymentResponse;
import com.Projects.SmartRailApplication.payment.dto.MockPaymentRequest;
import com.Projects.SmartRailApplication.payment.dto.MockPaymentResponse;
import com.Projects.SmartRailApplication.payment.mock.MockPaymentOutcome;
import com.Projects.SmartRailApplication.payment.mock.MockPaymentServer;
import com.Projects.SmartRailApplication.train.Entities.Seat;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(()-> new IllegalArgumentException("Booking not found with ID: " + bookingId));
        if(booking.getBookingStatus() != BookingStatus.PENDING_PAYMENT){
            throw new IllegalStateException("Payment can only be initiated for bookings with PENDING_PAYMENT status.");
        }
        if (paymentRepository.existsByIdempotencyKey(paymentRequest.idempotencyKey())) {
            throw new DuplicateKeyException("payment with same Idempotent key already processed");
        };

        BigDecimal seatAmount = BigDecimal.ZERO;
        for(BookingSeat seat : booking.getBookingSeats()){
            SeatReservation seatReservation = seatReservationRepository.findByBookingIdAndSeatId(bookingId, seat.getSeatId().getId()).orElseThrow(() -> new IllegalArgumentException("Seat reservation not found for booking ID: " + bookingId + " and seat ID: " + seat.getSeatId().getId()));
            if(seatReservation.getStatus() != ReservationStatus.HELD || seatReservation.getExpiresAt().isBefore(LocalDateTime.now())){
                throw new IllegalStateException("payment for the seat can only be initiated if the seat is currently held and not expired.");
            }
            seatAmount = seatAmount.add(seat.getPrice());
        }
        if(seatAmount.compareTo(booking.getTotalPrice()) != 0){
            throw new IllegalArgumentException("Payment amount does not match the total price of the seats.");
        }
        payment payment = new payment();
        payment.setBooking_Id(booking);
        payment.setAmount(booking.getTotalPrice());
        payment.setPaymentStatus(PaymentStatus.PENDING);
        payment.setIdempotencyKey(paymentRequest.idempotencyKey());
        payment.setCreatedAt(LocalDateTime.now());
        payment.setUpdatedAt(LocalDateTime.now());
        paymentRepository.save(payment);

        MockPaymentResponse gatewayResponse = mockPaymentServer.charge(
            new MockPaymentRequest(booking.getTotalPrice(), paymentRequest.idempotencyKey(), defaultPaymentOutcome));
        
        if(gatewayResponse.outcome() == MockPaymentOutcome.FAILED){
            List<SeatReservation> seatReservations = seatReservationRepository.findByBookingId(bookingId);
            for(SeatReservation seatReservation : seatReservations){
                SeatReservation updatedReservation = seatReservationRepository.findByIdForUpdate(seatReservation.getId()).orElseThrow(() -> new IllegalArgumentException("Seat reservation not found with ID: " + seatReservation.getId()));
                updatedReservation.setStatus(ReservationStatus.RELEASED);
                updatedReservation.setExpiresAt(null);
            }
            bookingRepository.findByIdForUpdate(bookingId).ifPresent(b -> b.setBookingStatus(BookingStatus.EXPIRED));
            throw new IllegalStateException("Payment failed. All held seats have been released and booking status set to EXPIRED.");
        }
        
        
        paymentRepository.findByIdForUpdate(payment.getId()).ifPresent(p -> {
            p.setPaymentStatus(gatewayResponse.outcome() == MockPaymentOutcome.SUCCESS ? PaymentStatus.SUCCESS : PaymentStatus.FAILED);
            p.setPaymentReference(gatewayResponse.transactionReference());
            p.setUpdatedAt(LocalDateTime.now());
        });

        bookingRepository.findByIdForUpdate(bookingId).ifPresent(b -> {
            if(b.getBookingStatus() != BookingStatus.PENDING_PAYMENT){
                //Initiate the refund since the booking status has changed
                throw new IllegalStateException("Booking status has changed since payment initiation. Current status: " + b.getBookingStatus());
            } else{
                b.setBookingStatus(gatewayResponse.outcome() == MockPaymentOutcome.SUCCESS ? BookingStatus.CONFIRMED : BookingStatus.EXPIRED);
            }
        });
        List<SeatReservation> seatReservations = seatReservationRepository.findByBookingId(bookingId);
            for(SeatReservation seatReservation : seatReservations){
                SeatReservation updatedReservation = seatReservationRepository.findByIdForUpdate(seatReservation.getId()).orElseThrow(() -> new IllegalArgumentException("Seat reservation not found with ID: " + seatReservation.getId()));
                if(updatedReservation.getStatus() != ReservationStatus.HELD || updatedReservation.getExpiresAt().isBefore(LocalDateTime.now())){
                    //Initiate the refund since the seatReservation status has changed
                    throw new IllegalStateException("Seat reservation status has changed since payment initiation. Current status: " + updatedReservation.getStatus());
                } else{
                    updatedReservation.setStatus(ReservationStatus.CONFIRMED);
                    updatedReservation.setExpiresAt(null);
                }
            }



        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setId(payment.getId());
        paymentResponse.setBookingId(bookingId);
        paymentResponse.setAmount(booking.getTotalPrice());
        paymentResponse.setPaymentStatus(payment.getPaymentStatus());
        paymentResponse.setTransactionReference(payment.getPaymentReference());
        
        // Save the payment details to the database
        // You can create a new Payment entity and set its properties based on the paymentRequest
        // Then save it using the paymentRepository
        
        return paymentResponse;
    }
}
