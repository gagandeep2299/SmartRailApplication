package com.Projects.SmartRailApplication.booking.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.Projects.SmartRailApplication.booking.Entities.SeatReservation;
import com.Projects.SmartRailApplication.booking.Entities.enums.ReservationStatus;

import jakarta.persistence.LockModeType;

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

    @Query("""
            SELECT r.id FROM SeatReservation r
            WHERE r.status = :status AND r.expiresAt <= :now
            ORDER BY r.expiresAt ASC
        """)
    List<Long> findExpiredReservationIds(@Param("status") ReservationStatus status,@Param("now") LocalDateTime now);
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT r FROM SeatReservation r WHERE r.id = :id")
    Optional<SeatReservation> findByIdForUpdate(@Param("id") Long id);

    @Query("""
        SELECT COUNT(r)
        FROM SeatReservation r
        WHERE r.bookingSeat.bookingId.id = :bookingId
          AND r.status = :status
          AND r.expiresAt >= :now
        """)
    long countActiveCountHold(@Param("bookingId") Long bookingId,
                                @Param("status") ReservationStatus status,
                                @Param("now") LocalDateTime now);
    
    @Query("""
        SELECT r
        FROM SeatReservation r
        WHERE r.bookingSeat.bookingId.id = :bookingId
          AND r.bookingSeat.seatId.id = :seatId
    """)
    Optional<SeatReservation> findByBookingIdAndSeatId(
        @Param("bookingId") Long bookingId,
        @Param("seatId") Long seatId
    );
}
