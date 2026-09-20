package com.Projects.SmartRailApplication.booking.Entities;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
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
    private com.Projects.SmartRailApplication.user.Entities.User user;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "train_id")
    private com.Projects.SmartRailApplication.train.Entities.train train;

    private LocalDateTime travelDateTime;

    private String bookingStatus;


}
