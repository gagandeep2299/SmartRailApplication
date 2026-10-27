package com.Projects.SmartRailApplication.train.Repository;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Projects.SmartRailApplication.train.Entities.Fare;
import com.Projects.SmartRailApplication.train.Entities.enums.CoachType;

public interface fareRepository extends JpaRepository<Fare, Long> {

    Optional<Fare> findByTrainId_IdAndCoachTypeAndSourceStationid_IdAndDestinationStationid_Id(Long trainId,
            CoachType coachType, Long sourceStationId, Long destinationStationId);

        List<Fare> findAllByTrainId_IdAndCoachTypeAndSourceStationid_IdAndDestinationStationid_Id(Long trainId,
            CoachType coachType, Long sourceStationId, Long destinationStationId);
    
}
