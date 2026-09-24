package com.Projects.SmartRailApplication.train.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Projects.SmartRailApplication.train.Entities.Station;

@Repository 
public interface stationRepository extends JpaRepository<Station, Long> {
    Station save(Station station);
    Station findByCode(String code);
    List<Station> findByNameContainingIgnoreCase(String query);
}
