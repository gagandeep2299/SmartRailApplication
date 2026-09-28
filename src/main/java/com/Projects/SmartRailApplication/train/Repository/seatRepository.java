package com.Projects.SmartRailApplication.train.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Projects.SmartRailApplication.train.Entities.Coach;
import com.Projects.SmartRailApplication.train.Entities.Seat;

public interface seatRepository extends JpaRepository<Seat, Long> {

    boolean existsByCoachIdAndSeatNumber(Coach coach, String seatNumber);

    List<Seat> findByCoachId(Long coachId);
    
}
