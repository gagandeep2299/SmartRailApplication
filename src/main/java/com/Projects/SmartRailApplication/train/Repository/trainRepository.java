package com.Projects.SmartRailApplication.train.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Projects.SmartRailApplication.train.Entities.train;

@Repository 
public interface trainRepository extends JpaRepository<train, Long> {

    boolean existsByTrainNumber(String trainNumber);
    Optional<train> findByTrainNumber(String trainNumber);

    List<train> findByNameContainingIgnoreCase(String name);
    
}
