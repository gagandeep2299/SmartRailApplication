package com.Projects.SmartRailApplication.train.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Projects.SmartRailApplication.train.Entities.Fare;

public interface fareRepository extends JpaRepository<Fare, Long> {
    
}
