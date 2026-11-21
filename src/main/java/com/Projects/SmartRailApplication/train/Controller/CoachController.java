package com.Projects.SmartRailApplication.train.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Projects.SmartRailApplication.train.Service.CoachService;
import com.Projects.SmartRailApplication.train.dto.CoachRequest;
import lombok.RequiredArgsConstructor;

@RestController 
@RequestMapping ("/api/v1")
@RequiredArgsConstructor
public class CoachController {
    private CoachService coachService;

    @PostMapping ("/trains/coaches")
    public ResponseEntity<?> addCoach(@RequestBody CoachRequest coachRequest){ 
        coachService.addCoach(coachRequest);
        return ResponseEntity.ok("Adding coach for train with ID: " + coachRequest.getTrainId());
    }
    
    @GetMapping ("/trains/{trainId}/coaches")
    public ResponseEntity<?> getCoachesByTrainId(@PathVariable Long trainId) {
        return ResponseEntity.ok(coachService.getCoachesByTrainId(trainId));
        
    }

    @GetMapping ("/coaches/{id}")
    public ResponseEntity<?> getCoachById(@PathVariable Long id) {
        return ResponseEntity.ok(coachService.getCoachById(id));
        
    }

    @PutMapping ("/coaches/{id}")
    public ResponseEntity<?> updateCoach(@PathVariable Long id, @RequestBody CoachRequest coachRequest) {
        coachService.updateCoach(id, coachRequest);
        return ResponseEntity.ok("Coach updated successfully");
        
    }
    @DeleteMapping("/coaches/{id}")
    public ResponseEntity<?> deleteCoach(@PathVariable Long id) {
        coachService.deleteCoach(id);
        return ResponseEntity.ok("Coach deleted successfully");
    }
}
