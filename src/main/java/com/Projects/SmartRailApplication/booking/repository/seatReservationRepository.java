package com.Projects.SmartRailApplication.booking.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.Projects.SmartRailApplication.booking.Entities.SeatReservation;
import com.Projects.SmartRailApplication.booking.Entities.enums.ReservationStatus;

public interface seatReservationRepository extends JpaRepository<SeatReservation,Long>{
    @Query("""
        SELECT COUNT(r) > 0
        FROM SeatReservation r
        WHERE r.bookingSeat.seatId.id = :seatId
          AND r.journeyDate = :journeyDate
          AND r.fromStopSequence < :toSequence
          AND r.toStopSequence > :fromSequence
          AND (
              r.status = :confirmed
              OR (
                  r.status = :held
                  AND r.expiresAt > :now
              )
          )
        """)
    boolean existsConflictingReservation(
             @Param("seatId") Long seatId,
            @Param("journeyDate") LocalDate journeyDate,
            @Param("fromSequence") Long fromSequence,
            @Param("toSequence") Long toSequence,
            @Param("confirmed") ReservationStatus confirmed,
            @Param("held") ReservationStatus held,
            @Param("now") LocalDateTime now
        );
    @Query("""
        SELECT r
        FROM SeatReservation r
        WHERE r.bookingSeat.bookingId.id = :bookingId
    """)
    List<SeatReservation> findByBookingId(
        @Param("bookingId") Long bookingId
    );

    List<SeatReservation> findByStatusAndExpiresAtBefore(
        ReservationStatus status,
        LocalDateTime time
    );

    
}
