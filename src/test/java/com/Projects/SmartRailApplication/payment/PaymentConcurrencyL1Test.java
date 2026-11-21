package com.Projects.SmartRailApplication.payment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.Projects.SmartRailApplication.booking.Entities.Booking;
import com.Projects.SmartRailApplication.booking.Entities.BookingSeat;
import com.Projects.SmartRailApplication.booking.Entities.SeatReservation;
import com.Projects.SmartRailApplication.booking.Entities.enums.BookingStatus;
import com.Projects.SmartRailApplication.booking.Entities.enums.ReservationStatus;
import com.Projects.SmartRailApplication.booking.repository.bookingRepository;
import com.Projects.SmartRailApplication.booking.repository.bookingSeatRepository;
import com.Projects.SmartRailApplication.booking.repository.seatReservationRepository;
import com.Projects.SmartRailApplication.payment.Entities.PaymentStatus;
import com.Projects.SmartRailApplication.payment.Entities.payment;
import com.Projects.SmartRailApplication.payment.dto.PaymentRequest;
import com.Projects.SmartRailApplication.payment.dto.PaymentResponse;
import com.Projects.SmartRailApplication.train.Entities.Coach;
import com.Projects.SmartRailApplication.train.Entities.Seat;
import com.Projects.SmartRailApplication.train.Entities.train;
import com.Projects.SmartRailApplication.train.Entities.enums.CoachType;
import com.Projects.SmartRailApplication.train.Entities.enums.SeatType;
import com.Projects.SmartRailApplication.train.Entities.enums.TrainStatus;
import com.Projects.SmartRailApplication.train.Repository.coachRepository;
import com.Projects.SmartRailApplication.train.Repository.seatRepository;
import com.Projects.SmartRailApplication.train.Repository.trainRepository;

import jakarta.persistence.EntityManager;

@Testcontainers 
@SpringBootTest 
@Tag ("Layer1")
public class PaymentConcurrencyL1Test {
    
    @Container 
    @ServiceConnection 
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    private paymentService paymentService;

    @Autowired
    private paymentRepository paymentRepository;

    @Autowired
    private bookingRepository bookingRepository;

    @Autowired
    private bookingSeatRepository bookingSeatRepository;

    @Autowired
    private seatReservationRepository seatReservationRepository;

    @Autowired
    private trainRepository trainRepository;

    @Autowired
    private coachRepository coachRepository;

    @Autowired
    private seatRepository seatRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    public void testConcurrentPaymentInitiation() throws Exception {
        train testTrain = new train();
        testTrain.setTrainNumber("SR-" + UUID.randomUUID());
        testTrain.setName("Test Train");
        testTrain.setStatus(TrainStatus.ACTIVE);
        testTrain = trainRepository.saveAndFlush(testTrain);

        Coach coach = new Coach();
        coach.setTrainId(testTrain);
        coach.setCoachNumber("C-" + UUID.randomUUID());
        coach.setCoachType(CoachType.SLEEPER);
        coach = coachRepository.saveAndFlush(coach);

        Seat seat = new Seat();
        seat.setCoachId(coach);
        seat.setSeatNumber("S-1");
        seat.setSeatType(SeatType.WINDOW);
        seat.setSeatStatus("AVAILABLE");
        seat = seatRepository.saveAndFlush(seat);

        Booking booking = new Booking();
        booking.setTrain(testTrain);
        booking.setTravelDateTime(LocalDate.now().plusDays(1));
        booking.setBookingStatus(BookingStatus.PENDING_PAYMENT);
        booking.setTotalPrice(new BigDecimal("100.00"));
        booking = bookingRepository.saveAndFlush(booking);

        BookingSeat bookingSeat = new BookingSeat();
        bookingSeat.setBookingId(booking);
        bookingSeat.setSeatId(seat);
        bookingSeat.setBookingStatus(BookingStatus.PENDING_PAYMENT);
        bookingSeat.setPrice(new BigDecimal("100.00"));
        booking.addBookingSeat(bookingSeat);
        booking = bookingRepository.saveAndFlush(booking);
        Long bookingId = booking.getId();
        bookingSeat = bookingSeatRepository.findAll().stream()
                .filter(savedSeat -> savedSeat.getBookingId().getId().equals(bookingId))
                .findFirst()
                .orElseThrow();
        Long bookingSeatId = bookingSeat.getId();

        SeatReservation reservation = new SeatReservation();
        reservation.setBookingSeat(bookingSeat);
        reservation.setJourneyDate(LocalDate.now().plusDays(1));
        reservation.setFromStopSequence(1L);
        reservation.setToStopSequence(2L);
        reservation.setStatus(ReservationStatus.HELD);
        reservation.setExpiresAt(LocalDateTime.now().plusMinutes(10));
        reservation = seatReservationRepository.saveAndFlush(reservation);
        Long reservationId = reservation.getId();

        String idempotencyKey1 = "Concurrent-payment-"+UUID.randomUUID();
        String idempotencyKey2 = "Concurrent-payment-"+UUID.randomUUID();
        ExecutorService executor = Executors.newFixedThreadPool(2);
        CountDownLatch ready = new CountDownLatch(2);
        CountDownLatch start = new CountDownLatch(1);

        try{
            Future<PaymentAttempt> first = executor.submit(
                ()->runPaymentAttempt(bookingId,idempotencyKey1,ready,start)
            );
            Future<PaymentAttempt> second = executor.submit(
                ()->runPaymentAttempt(bookingId,idempotencyKey2,ready,start)
            );
            assertTrue(ready.await(10,TimeUnit.SECONDS),
                    "Both payment tasks should be ready before starting.");
            start.countDown();

            PaymentAttempt firstAttempt = first.get(30,TimeUnit.SECONDS);
            PaymentAttempt secondAttempt = second.get(30,TimeUnit.SECONDS);

            int successfulAttempts = (firstAttempt.response() != null ? 1 : 0)
                    + (secondAttempt.response() != null ? 1 : 0);

            assertEquals(1, successfulAttempts, "Exactly one payment should succeed.");

            PaymentAttempt successfulAttempt =
                    firstAttempt.response() != null ? firstAttempt : secondAttempt;
            PaymentAttempt rejectedAttempt =
                    firstAttempt.response() == null ? firstAttempt : secondAttempt;
            assertEquals(PaymentStatus.SUCCESS, successfulAttempt.response().getPaymentStatus());
            assertNotNull(successfulAttempt.response().getTransactionReference());
            assertTrue(rejectedAttempt.failure() instanceof IllegalStateException,
                    "The second request should be rejected after the booking is confirmed.");

            entityManager.clear();
            List<payment> savedPayments = paymentRepository.findAll().stream()
                    .filter(savedPayment -> savedPayment.getBooking_Id().getId().equals(bookingId))
                    .toList();
            assertEquals(1, savedPayments.size(),
                    "Exactly one payment should be persisted for this booking.");
            assertEquals(PaymentStatus.SUCCESS, savedPayments.get(0).getPaymentStatus());
            assertEquals(BookingStatus.CONFIRMED,
                    bookingRepository.findById(bookingId).orElseThrow().getBookingStatus());
            assertEquals(BookingStatus.CONFIRMED,
                    bookingSeatRepository.findById(bookingSeatId).orElseThrow().getBookingStatus());
            assertEquals(ReservationStatus.CONFIRMED,
                    seatReservationRepository.findById(reservationId).orElseThrow().getStatus());

        } finally{

            start.countDown();
            executor.shutdownNow();
        }
    }
    private PaymentAttempt runPaymentAttempt(
        Long bookingId,
        String idempotencyKey,
        CountDownLatch ready,
        CountDownLatch start) throws InterruptedException{
        ready.countDown();
        if (!start.await(10, TimeUnit.SECONDS)) {
            throw new IllegalStateException("Timed out waiting to start payment request.");
        }

        try {
            PaymentResponse response = paymentService.initiatePayment(
                    bookingId, new PaymentRequest(idempotencyKey));
            return new PaymentAttempt(response, null);
        } catch (RuntimeException exception) {
            return new PaymentAttempt(null, exception);
        }
    }

    private record PaymentAttempt(PaymentResponse response, RuntimeException failure) {
    }

}
