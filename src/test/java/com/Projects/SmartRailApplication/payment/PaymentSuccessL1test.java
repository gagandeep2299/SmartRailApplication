package com.Projects.SmartRailApplication.payment;

import org.springframework.beans.factory.annotation.Autowired;

@SpringBootTest
@Testcontainers
@Tag("Layer1")
public class PaymentSuccessL1test {
    @Container
    private PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");
    private static final MockPaymentOutcome DEFAULT_OUTCOME = MockPaymentOutcome.SUCCESS;
    private static final String SPRING_JPA_HIBERNATE_DDL_AUTO = "create-drop";

    @Autowired 
    private paymentService paymentService;

    @Autowired 
    private paymentRepository paymentRepository;

    @Autowired 
    private bookingRepository bookingRepository;

    @Autowired 
    private seatReservationRepository seatReservationRepository;
}
