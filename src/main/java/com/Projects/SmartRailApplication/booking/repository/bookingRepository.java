package com.Projects.SmartRailApplication.booking.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.Projects.SmartRailApplication.booking.Entities.Booking;

import jakarta.persistence.LockModeType;

public interface bookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByUserId_Id(Long userId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT b FROM Booking b
        WHERE b.id = :bookingId
        """)
    Optional<Booking> findByIdForUpdate(@Param("bookingId")Long bookingId);
    
}
