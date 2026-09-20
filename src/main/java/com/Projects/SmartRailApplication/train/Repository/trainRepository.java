package com.Projects.SmartRailApplication.train.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Projects.SmartRailApplication.train.Entities.train;

@Repository 
public interface trainRepository extends JpaRepository<train, Long> {
    
}
