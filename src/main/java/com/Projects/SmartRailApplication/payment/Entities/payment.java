package com.Projects.SmartRailApplication.payment.Entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
import lombok.Getter;
import lombok.Setter;

@Entity
@Table (name = "payment")
@Getter 
@Setter 
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
    
    @Column (name = "payment_reference", unique = true)
    private String paymentReference;

    @Column (name = "Idempotency_key", unique = true)
    private String idempotencyKey;

    @Column (name = "created_At", nullable = false)
    private LocalDateTime createdAt;

    @Column (name = "updated_At", nullable = false)
    private LocalDateTime updatedAt;
}
