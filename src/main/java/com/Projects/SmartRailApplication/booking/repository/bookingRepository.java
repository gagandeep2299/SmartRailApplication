package com.Projects.SmartRailApplication.booking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Projects.SmartRailApplication.booking.Entities.Booking;

public interface bookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByUserId_Id(Long userId);
    
}
