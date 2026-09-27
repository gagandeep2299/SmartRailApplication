package com.Projects.SmartRailApplication.train.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Projects.SmartRailApplication.train.Entities.RouteStop;

public interface routeStopRepository extends JpaRepository<RouteStop, Long> {
    boolean existsByTrain_IdAndStopSequence(Long trainId, Long stopSequence);

    List<RouteStop> findByTrain_IdOrderByStopSequenceAsc(Long trainId);
    
}
