package com.Projects.SmartRailApplication.train.Controller;

import java.util.List;

import javax.xml.crypto.dsig.TransformService;

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

import com.Projects.SmartRailApplication.train.Service.TrainService;
import com.Projects.SmartRailApplication.train.dto.TrainRequest;
import com.Projects.SmartRailApplication.train.dto.TrainResponse;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api/v1/trains")
public class trainController {

    @Autowired
    private TrainService trainService;

    @PostMapping()
    public ResponseEntity<?> addTrain(@RequestBody TrainRequest trainRequest) {
        try{
            trainService.addTrain(trainRequest);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Train already exist with this number");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred while adding the train");
        }

        return ResponseEntity.ok("Adding new train with train number: " + trainRequest.getTrainNumber());
    }

    @GetMapping()
    public ResponseEntity<?> getAllTrains() {
        List<TrainResponse> trains = trainService.getAllTrains();
        return ResponseEntity.ok(trains);
    }

    @GetMapping ("/{trainId}")
    public ResponseEntity<?> getTrain(@PathVariable Long trainId) {
        try{
            trainService.getTrainById(trainId);
        } catch(EntityNotFoundException e) {
            return ResponseEntity.status(404).body("Train not found with ID: " + trainId);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred while retrieving the train");
        }
        return ResponseEntity.ok("Getting train with ID: " + trainId);
    }

    @GetMapping ("/number/{trainNumber}")
    public ResponseEntity<?> getTrainByNumber(@PathVariable String trainNumber) {
        try{
            trainService.getTrainByNumber(trainNumber);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body("Train not found with number: " + trainNumber);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred while retrieving the train");
        }
        return ResponseEntity.ok("Getting train with number: " + trainNumber);
    }
    @GetMapping("/search?name=")
    public ResponseEntity<?> searchTrainsByName(@RequestParam String name) {
        List<TrainResponse> trains;
        try{
            trains = trainService.getTrainsByName(name);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body("Train not found with name: " + name);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred while searching for trains");
        }
        return ResponseEntity.ok(trains);
    }

    @PutMapping("/{trainId}")
    public ResponseEntity<?> updateTrain(@PathVariable Long trainId, @RequestBody TrainRequest trainRequest) {
        try{
            trainService.updateTrain(trainId, trainRequest);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body("Train not found with ID: " + trainId);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred while updating the train");
        }
        return ResponseEntity.ok("Updating train with ID: " + trainId);
    }
    @PatchMapping("/{trainId}/status=")
    public ResponseEntity<?> updateTrainStatus(@PathVariable Long trainId, @RequestParam String status) {
        try{
            trainService.updateTrainStatus(trainId, status);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body("Train not found with ID: " + trainId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid status value: " + status);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred while updating the train status");
        }
        return ResponseEntity.ok("Updating train status with ID: " + trainId);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchTrains(@RequestParam String from, @RequestParam String to, @RequestParam String date) {
        // Implementation for searching trains
        return ResponseEntity.ok("Searching trains from " + from + " to " + to + " on " + date);
    }

    @GetMapping ("/{trainId}/seats")
    public ResponseEntity<?> getAvailableSeats(@PathVariable String trainId) {
        // Implementation for getting available seats for a specific train
        return ResponseEntity.ok("Getting available seats for train with ID: " + trainId);
    }

    @GetMapping ("/{trainId}/routes")
    public ResponseEntity<?> getTrainRoutes(@PathVariable String trainId) {
        // Implementation for getting routes for a specific train
        return ResponseEntity.ok("Getting routes for train with ID: " + trainId);
    }
}
