package com.Projects.SmartRailApplication.train.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Projects.SmartRailApplication.train.Service.CoachService;
import com.Projects.SmartRailApplication.train.dto.CoachRequest;

import jakarta.persistence.EntityNotFoundException;

@RestController 
@RequestMapping ("/api/v1")
public class CoachController {
    @Autowired 
    private CoachService coachService;

    @PostMapping ("/trains/{trainId}/coaches")
    public ResponseEntity<?> addCoach(@RequestParam Long trainId,@RequestBody CoachRequest coachRequest){ 
        try{
            coachService.addCoach(trainId, coachRequest);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An unexpected error occurred: " + e.getMessage());
        }
        return ResponseEntity.ok("Adding coach for train with ID: " + trainId);
    }
    
    @GetMapping ("/trains/{trainId}/coaches")
    public ResponseEntity<?> getCoachesByTrainId(@RequestParam Long trainId) {
        try {
            return ResponseEntity.ok(coachService.getCoachesByTrainId(trainId));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @GetMapping ("/coaches/{id}")
    public ResponseEntity<?> getCoachById(@RequestParam Long id) {
        try {
            return ResponseEntity.ok(coachService.getCoachById(id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @PutMapping ("/coaches/{id}")
    public ResponseEntity<?> updateCoach(@RequestParam Long id, @RequestBody CoachRequest coachRequest) {
        try {
            coachService.updateCoach(id, coachRequest);
            return ResponseEntity.ok("Coach updated successfully");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An unexpected error occurred: " + e.getMessage());
        }
    }
    @DeleteMapping("/coaches/{id}")
    public ResponseEntity<?> deleteCoach(@RequestParam Long id) {
        try {
            coachService.deleteCoach(id);
            return ResponseEntity.ok("Coach deleted successfully");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An unexpected error occurred: " + e.getMessage());
        }
    }
}
