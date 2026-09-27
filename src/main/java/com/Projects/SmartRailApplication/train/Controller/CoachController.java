package com.Projects.SmartRailApplication.train.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Projects.SmartRailApplication.train.Service.CoachService;

@RestController 
@RequestMapping ("/api/v1")
public class CoachController {
    @Autowired 
    private CoachService coachService;

    @PostMapping ("/trains/{trainId}/coaches")
    public ResponseEntity<?> addCoach() {
        return ResponseEntity.ok("Adding coach for train with ID: ");
    }
}
