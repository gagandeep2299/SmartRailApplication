package com.Projects.SmartRailApplication.train.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Projects.SmartRailApplication.train.Service.StationService;
import com.Projects.SmartRailApplication.train.dto.StationRequest;
import com.Projects.SmartRailApplication.train.dto.StationResponse;

@RestController
@RequestMapping("/api/v1/stations")
public class StationController {
    
    @Autowired 
    private StationService stationService;
    
    @PostMapping()
    public ResponseEntity<?> addStation(@RequestBody StationRequest station) {
        if (stationService.addStation(station)) {
            return ResponseEntity.ok("Adding new station: " + station.getName());
        } else {
            return ResponseEntity.status(500).body("Failed to add station: " + station.getName());
        }
    }

    @GetMapping()
    public ResponseEntity<?> getAllStations() {
        List<StationResponse> stations = stationService.getAllStations();
        return ResponseEntity.ok(stations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getStationById(@PathVariable Long id) {
        StationResponse station;
        try{
         station = stationService.getStationById(id); 
        } catch (Exception e) {
            return ResponseEntity.status(404).body("Station not found with id: " + id);
        }
        return ResponseEntity.ok(station);
    }
}
