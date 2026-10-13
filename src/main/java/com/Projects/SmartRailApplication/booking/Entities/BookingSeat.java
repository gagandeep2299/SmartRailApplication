package com.Projects.SmartRailApplication.booking.Entities;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table (name = "booking_seats")
@Getter 
@Setter 
public class BookingSeat {
    
    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "booking_id", updatable = false)
    private Booking bookingId;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "seat_id", updatable = false)
    private com.Projects.SmartRailApplication.train.Entities.Seat seatId;

    @Enumerated (jakarta.persistence.EnumType.STRING)
    @Column (name = "booking_status", nullable = false)
    private com.Projects.SmartRailApplication.booking.Entities.enums.BookingStatus bookingStatus;

    @Column (precision =10 , scale = 2)
    private BigDecimal price;
}
