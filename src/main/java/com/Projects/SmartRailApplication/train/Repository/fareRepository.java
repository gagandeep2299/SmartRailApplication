package com.Projects.SmartRailApplication.train.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Projects.SmartRailApplication.train.Entities.Fare;
import com.Projects.SmartRailApplication.train.Entities.enums.CoachType;

public interface fareRepository extends JpaRepository<Fare, Long> {

    Optional<Fare> findByTrainIdAndCoachTypeAndSourceStationidAndDestinationStationid(Long trainId,
            CoachType coachType, Long sourceStationId, Long destinationStationId);
    
}
