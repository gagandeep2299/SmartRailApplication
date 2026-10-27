package com.Projects.SmartRailApplication.booking.Entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.Projects.SmartRailApplication.auth.entities.User;
import com.Projects.SmartRailApplication.booking.Entities.enums.BookingStatus;
import com.Projects.SmartRailApplication.train.Entities.*;

import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity 
@Getter 
@Setter 
@Table (name = "booking")
public class Booking {
    
    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "user_id",nullable = true)
    private User user;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "source_station_id")
    private Station sourceStation;

    @ManyToOne (fetch = FetchType.LAZY) 
    @JoinColumn (name = "destination_station_id")
    private Station destinationStation;

    @OneToMany (mappedBy = "bookingId", cascade = CascadeType.ALL)
    private List<BookingSeat> bookingSeats = new ArrayList<>();

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "train_id", nullable = false)
    private com.Projects.SmartRailApplication.train.Entities.train train;

    @Column (name = "travel_date", nullable = false)
    private LocalDate travelDateTime;

    @Column (name = "booking_status", nullable = false)
    @Enumerated (EnumType.STRING)
    private BookingStatus bookingStatus;

    @Column (name = "total_price", nullable = false)
    private java.math.BigDecimal totalPrice;

    public void addBookingSeat(BookingSeat bookingSeat) {
        bookingSeats.add(bookingSeat);
        bookingSeat.setBookingId(this);
    }


}
