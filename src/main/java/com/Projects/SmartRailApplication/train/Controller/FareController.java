package com.Projects.SmartRailApplication.train.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Projects.SmartRailApplication.train.Entities.enums.CoachType;
import com.Projects.SmartRailApplication.train.Service.FareService;
import com.Projects.SmartRailApplication.train.dto.FareRequest;
import com.Projects.SmartRailApplication.train.dto.FareResponse;


@RestController
@RequestMapping("/api/v1")
public class FareController {
    
    @Autowired
    private FareService fareService;

    @PostMapping("/trains/{trainId}/fares")
    public ResponseEntity<?> createFare(@RequestBody FareRequest fareRequest) {
        fareService.createFare(fareRequest);
        return ResponseEntity.ok().build();
    }
    @GetMapping ("/trains/{trainId}/fares")
    public ResponseEntity<?> getFare(@PathVariable Long trainId, @RequestParam Long sourceStationId, @RequestParam Long destinationStationId, @RequestParam CoachType coachType) {
        FareResponse fareResponse = fareService.getFare(trainId, sourceStationId, destinationStationId, coachType);
        return ResponseEntity.ok(fareResponse);
    }

    @GetMapping("/fares/{id}")
    public ResponseEntity<?> getFareById(@PathVariable Long id) {
        FareResponse fareResponse = fareService.getFareById(id);
        return ResponseEntity.ok(fareResponse);
        
    }

    @PutMapping("/fares/{id}")
    public ResponseEntity<?> updateFare(@PathVariable Long id, @RequestBody FareRequest fareRequest) {
        FareResponse fareResponse = fareService.updateFare(id, fareRequest);
        return ResponseEntity.ok(fareResponse);
        
    }
    @DeleteMapping("/fares/{id}")
    public ResponseEntity<?> deleteFare(@PathVariable Long id) {
        fareService.deleteFare(id);
        return ResponseEntity.ok().build();
        
    }

}
