package com.Projects.SmartRailApplication.payment;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.Projects.SmartRailApplication.auth.entities.User;
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
@Tag("Layer2")
public class PaymentFailureL1Test {
    
    @Container 
    @ServiceConnection 
    private static final PostgreSQLContainer<?> POSTGRES_SQL_CONTAINER = new PostgreSQLContainer<>("postgres:16-alpine");

    @DynamicPropertySource 
    static void registerProperties(org.springframework.test.context.DynamicPropertyRegistry registry) {
        registry.add("mock.payment.default-outcome", () -> "FAILED");
    }

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
    @Autowired
        private com.Projects.SmartRailApplication.auth.repository.UserRepository UserRepository;
    @Test 
    public void testPaymentFailureScenario() {
        User testUser = new User();
        testUser.setName("Test User");
        testUser.setEmail("testUser@email.com");
        testUser.setPassword("password");
        testUser.setCreatedAt(LocalDateTime.now());
        testUser.setUpdatedAt(LocalDateTime.now());
        UserRepository.saveAndFlush(testUser);
        User user= UserRepository.findByEmailIgnoreCase(testUser.getEmail()).get();

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
        booking.setUser(user);
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

        String idempotencyKey = "Failed-payment-"+UUID.randomUUID();
        PaymentResponse response = paymentService.initiatePayment(user.getId(),
                booking.getId(), new PaymentRequest(idempotencyKey));


        entityManager.clear();
        Booking savedBooking = bookingRepository.findById(
                                booking.getId()).orElseThrow(); 
        SeatReservation savedReservation = seatReservationRepository.findById(
                                        reservation.getId()).orElseThrow();
        
        var savedPayment = paymentRepository.findAll().get(0);
        assertEquals(PaymentStatus.FAILED, response.getPaymentStatus());
        assertEquals(booking.getId(), response.getBookingId());
        assertEquals(new BigDecimal("100.00"),response.getAmount());
        assertNotNull(response.getTransactionReference());
        assertTrue(response.getTransactionReference().startsWith("mock-"));
        
        assertEquals(1, paymentRepository.count());
        assertEquals(idempotencyKey, savedPayment.getIdempotencyKey());
        assertEquals(PaymentStatus.FAILED, savedPayment.getPaymentStatus());
        assertEquals(savedBooking.getId(), savedPayment.getBooking_Id().getId());

        assertEquals(BookingStatus.EXPIRED, savedBooking.getBookingStatus());
        assertEquals(ReservationStatus.RELEASED, savedReservation.getStatus());
        assertNull(savedReservation.getExpiresAt());
    }


}
