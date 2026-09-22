package com.Projects.SmartRailApplication.booking.Entities;

import java.time.LocalDateTime;

import com.Projects.SmartRailApplication.auth.entities.User;
import com.Projects.SmartRailApplication.booking.Entities.enums.BookingStatus;

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
@Table (name = "booking")
public class Booking {
    
    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "user_id")
    private User user;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "train_id")
    private com.Projects.SmartRailApplication.train.Entities.train train;

    @Column (name = "travel_date_time", nullable = false)
    private LocalDateTime travelDateTime;

    @Column (name = "booking_status", nullable = false)
    @Enumerated (EnumType.STRING)
    private BookingStatus bookingStatus;


}
