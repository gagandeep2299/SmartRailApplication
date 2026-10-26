package com.Projects.SmartRailApplication.booking.Entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.Projects.SmartRailApplication.booking.Entities.enums.ReservationStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity 
@Getter 
@Setter 
@NoArgsConstructor 
@Table (
    name = "seat_reservations",
    indexes = {
        @Index (name = "idx_reservation_journey", columnList = "journey_date,status"),
        @Index (name = "idx_reservation_expiry" , columnList = "status , expires_at")
    })
public class SeatReservation {
    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    
    /*@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn (name = "seat_id",nullable = false)
    private Seat seat;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "booking_id",nullable = false)
    private Booking booking;*/

    @OneToOne (fetch = FetchType.LAZY)
    @JoinColumn (
        name= "booking_seat_id",
        nullable = false,
        unique = true
    )
    private BookingSeat bookingSeat;

    @Column (name = "journey_date",nullable = false)
    private LocalDate journeyDate;

    @Column (name = "from_stop_sequence",nullable = false)
    private Long fromStopSequence;

    @Column (name = "to_stop_sequence",nullable = false)
    private Long toStopSequence;

    @Enumerated (EnumType.STRING)
    @Column (nullable = false, length = 20)
    private ReservationStatus status;

    @Column (name = "expires_at")
    private LocalDateTime expiresAt;

    @Column (name = "created_at",nullable = false)
    private LocalDateTime createdAt;

    @PrePersist 
    public void onCreate(){
        if(createdAt == null)
            createdAt = LocalDateTime.now();
    }

    public void confirm(){
        if(status != ReservationStatus.HELD){
            throw new IllegalArgumentException("Only held reservation can be confirmed");
        }
        if(expiresAt == null || !expiresAt.isAfter(LocalDateTime.now())){
            throw new IllegalStateException("Reservation hold is expired");
        }

        status = ReservationStatus.CONFIRMED;
        expiresAt = null;
    }

    public void release(){
        if(status == ReservationStatus.HELD ||
            status == ReservationStatus.CONFIRMED){
                status = ReservationStatus.RELEASED;
                expiresAt =null;
        }
    }
    
}
