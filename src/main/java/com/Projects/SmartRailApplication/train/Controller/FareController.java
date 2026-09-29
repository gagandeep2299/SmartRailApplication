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

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api/v1")
public class FareController {
    
    @Autowired
    private FareService fareService;

    @PostMapping("/trains/{trainId}/fares")
    public ResponseEntity<?> createFare(@RequestBody FareRequest fareRequest) {
        try{
            fareService.createFare(fareRequest);
        } catch (EntityNotFoundException e){
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }
    @GetMapping ("/trains/{trainId}/fares")
    public ResponseEntity<?> getFare(@PathVariable Long trainId, @RequestParam Long sourceStationId, @RequestParam Long destinationStationId, @RequestParam CoachType coachType) {
        try {
            FareResponse fareResponse = fareService.getFare(trainId, sourceStationId, destinationStationId, coachType);
            return ResponseEntity.ok(fareResponse);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/fares/{id}")
    public ResponseEntity<?> getFareById(@PathVariable Long id) {
        try {
            FareResponse fareResponse = fareService.getFareById(id);
            return ResponseEntity.ok(fareResponse);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/fares/{id}")
    public ResponseEntity<?> updateFare(@PathVariable Long id, @RequestBody FareRequest fareRequest) {
        try {
            FareResponse fareResponse = fareService.updateFare(id, fareRequest);
            return ResponseEntity.ok(fareResponse);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    @DeleteMapping("/fares/{id}")
    public ResponseEntity<?> deleteFare(@PathVariable Long id) {
        try {
            fareService.deleteFare(id);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
