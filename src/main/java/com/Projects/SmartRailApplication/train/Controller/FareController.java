package com.Projects.SmartRailApplication.train.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.Projects.SmartRailApplication.train.Service.FareService;
import com.Projects.SmartRailApplication.train.dto.FareRequest;

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
}
