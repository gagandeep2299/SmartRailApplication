package com.Projects.SmartRailApplication.payment;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

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
import com.Projects.SmartRailApplication.payment.dto.PaymentRequest;
import com.Projects.SmartRailApplication.payment.dto.PaymentResponse;
import com.Projects.SmartRailApplication.train.Entities.Coach;
import com.Projects.SmartRailApplication.train.Entities.Seat;
import com.Projects.SmartRailApplication.train.Entities.enums.CoachType;
import com.Projects.SmartRailApplication.train.Entities.enums.SeatType;
import com.Projects.SmartRailApplication.train.Entities.enums.TrainStatus;
import com.Projects.SmartRailApplication.train.Entities.train;
import com.Projects.SmartRailApplication.train.Repository.coachRepository;
import com.Projects.SmartRailApplication.train.Repository.seatRepository;
import com.Projects.SmartRailApplication.train.Repository.trainRepository;

import jakarta.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
@Testcontainers
@Tag("Layer1")
class PaymentSuccessL1Test {
    @Container
    @ServiceConnection 
    private static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:16-alpine");

    /*@DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        registry.add("mock.payment.default-outcome", () -> "SUCCESS");
    }*/
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
    void successfulPaymentUpdatesAllPersistedStates() {
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
        bookingSeat = bookingSeatRepository.findAll().get(0);

        SeatReservation reservation = new SeatReservation();
        reservation.setBookingSeat(bookingSeat);
        reservation.setJourneyDate(LocalDate.now().plusDays(1));
        reservation.setFromStopSequence(1L);
        reservation.setToStopSequence(2L);
        reservation.setStatus(ReservationStatus.HELD);
        reservation.setExpiresAt(LocalDateTime.now().plusMinutes(10));
        reservation = seatReservationRepository.saveAndFlush(reservation);

        String idempotencyKey = "Success-payment-"+UUID.randomUUID();
        PaymentResponse response = paymentService.initiatePayment(
                booking.getId(), new PaymentRequest(idempotencyKey));


        entityManager.clear(); // Clear the persistence context to ensure fresh data is fetched from the database
        
        Booking savedBooking = bookingRepository.findById(
                                booking.getId()).orElseThrow();
        BookingSeat savedBookingSeat = bookingSeatRepository.findById(
                                        bookingSeat.getId()).orElseThrow(); 
        SeatReservation savedReservation = seatReservationRepository.findById(
                                        reservation.getId()).orElseThrow();
        
        var savedPayment = paymentRepository.findAll().get(0);

        assertEquals(PaymentStatus.SUCCESS, response.getPaymentStatus());
        assertEquals(booking.getId(), response.getBookingId());
        assertEquals(new BigDecimal("100.00"),response.getAmount());
        assertNotNull(response.getTransactionReference());
        assertTrue(response.getTransactionReference().startsWith("mock-"));
        
        assertEquals(1, paymentRepository.count());
        assertEquals(idempotencyKey, savedPayment.getIdempotencyKey());
        assertEquals(PaymentStatus.SUCCESS, savedPayment.getPaymentStatus());
        assertEquals(savedBooking.getId(), savedPayment.getBooking_Id().getId());

        assertEquals(BookingStatus.CONFIRMED, savedBooking.getBookingStatus());
        assertEquals(BookingStatus.CONFIRMED, savedBookingSeat.getBookingStatus());
        assertEquals(ReservationStatus.CONFIRMED, savedReservation.getStatus());
        assertNull(savedReservation.getExpiresAt());
    }
}
