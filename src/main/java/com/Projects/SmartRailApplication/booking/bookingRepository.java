package com.Projects.SmartRailApplication.booking;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Projects.SmartRailApplication.booking.Entities.Booking;

public interface bookingRepository extends JpaRepository<Booking, Long> {
    
}
