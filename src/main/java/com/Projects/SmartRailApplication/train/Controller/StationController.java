package com.Projects.SmartRailApplication.train.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
        return ResponseEntity.ok("Adding new station: " + station.getName());
    }

    @GetMapping()
    public ResponseEntity<?> getAllStations() {
        List<StationResponse> stations = stationService.getAllStations();
        return ResponseEntity.ok(stations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getStationById(@PathVariable Long id) {
        StationResponse station = stationService.getStationById(id); 
        return ResponseEntity.ok(station);
    }
    @GetMapping ("/code/{code}")
    public ResponseEntity<?> getStationByCode(@PathVariable String code) {
        StationResponse station = stationService.getStationByCode(code); 
        return ResponseEntity.ok(station);
    }
    @GetMapping ("/search")
    public ResponseEntity<?> searchStations(@RequestParam String query) {
        List<StationResponse> stations = stationService.searchStations(query);
        return ResponseEntity.ok(stations);
    }
    @PutMapping ("/{id}")
    public ResponseEntity<?> updateStation(@PathVariable Long id, @RequestBody StationRequest station) {
        StationResponse station1 = stationService.updateStation(id, station);
        return ResponseEntity.ok().body(station1);
    }
    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateStationStatus(@PathVariable Long id, @RequestParam boolean status) {
        StationResponse station = stationService.updateStationStatus(id, status);
        return ResponseEntity.ok().body(station);
    }
    
}
