package com.Projects.SmartRailApplication.train.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Projects.SmartRailApplication.train.Entities.RouteStop;

public interface routeStopRepository extends JpaRepository<RouteStop, Long> {
    
}
