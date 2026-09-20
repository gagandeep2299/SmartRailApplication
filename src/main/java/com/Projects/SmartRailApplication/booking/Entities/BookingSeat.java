package com.Projects.SmartRailApplication.booking.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity 
@Table (name = "booking_seats")
public class BookingSeat {
    
    //@EmbeddedId
    //private BookingSeatId id;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "booking_id", updatable = false)
    private Booking bookingId;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "seat_id", updatable = false)
    private com.Projects.SmartRailApplication.train.Entities.Seat seatId;
}
