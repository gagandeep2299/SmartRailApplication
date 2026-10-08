package com.Projects.SmartRailApplication.train.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Projects.SmartRailApplication.train.Entities.Coach;

public interface coachRepository extends JpaRepository<Coach, Long> {

    boolean existsByTrainId_IdAndId(Long trainId, Long id);

    List<Coach> findByTrainId_Id(Long id);

    boolean existsByTrainId_IdAndCoachNumber(Long trainId, String coachNumber);
    
}
