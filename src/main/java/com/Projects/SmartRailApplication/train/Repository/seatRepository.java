package com.Projects.SmartRailApplication.train.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Projects.SmartRailApplication.train.Entities.Seat;

public interface seatRepository extends JpaRepository<Seat, Long> {
    
}
