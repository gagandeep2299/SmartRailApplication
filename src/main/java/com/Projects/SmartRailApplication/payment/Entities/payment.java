package com.Projects.SmartRailApplication.payment.Entities;

import java.math.BigDecimal;
import com.Projects.SmartRailApplication.booking.Entities.Booking;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table (name = "payment")
public class payment {
    
    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "booking_id")
    private Booking booking_Id;

    @Column (name = "amount", nullable = false)
    private BigDecimal amount;
    
    @Enumerated (EnumType.STRING)
    @Column (name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus;
    
    @Column (name = "transaction_reference", unique = true)
    private String transactionReference;
}
