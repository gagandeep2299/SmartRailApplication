package com.Projects.SmartRailApplication.train.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.Projects.SmartRailApplication.train.Entities.Coach;
import com.Projects.SmartRailApplication.train.Entities.Seat;

import jakarta.persistence.LockModeType;

public interface seatRepository extends JpaRepository<Seat, Long> {

    boolean existsByCoachIdAndSeatNumber(Coach coach, String seatNumber);

    List<Seat> findByCoachId(Coach coachId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM Seat s WHERE s.id = :seatId")
    Optional<Seat> findByIdForUpdate(
        @Param("seatId") Long seatId
    );  
    
}
