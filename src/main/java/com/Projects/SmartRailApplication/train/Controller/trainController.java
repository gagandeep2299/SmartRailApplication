package com.Projects.SmartRailApplication.train.Controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.Projects.SmartRailApplication.train.Service.trainService;
import com.Projects.SmartRailApplication.train.dto.TrainRequest;
import com.Projects.SmartRailApplication.train.dto.TrainResponse;

@RestController
@RequestMapping("/api/v1/trains")
public class trainController {

    @Autowired
    private trainService trainService;
    private static final Logger log =
        LoggerFactory.getLogger(trainController.class);

    @PostMapping()
    public ResponseEntity<?> addTrain(@RequestBody TrainRequest trainRequest) {
        log.info("Adding new train with train number: {}", trainRequest.getTrainNumber());
        trainService.addTrain(trainRequest);
        return ResponseEntity.ok("Adding new train with train number: " + trainRequest.getTrainNumber());
    }

    @GetMapping()
    public ResponseEntity<?> getAllTrains() {
        List<TrainResponse> trains = trainService.getAllTrains();
        return ResponseEntity.ok(trains);
    }

    @GetMapping ("/{trainId}")
    public ResponseEntity<?> getTrain(@PathVariable Long trainId) {
        TrainResponse trainResponse = trainService.getTrainById(trainId);
        return ResponseEntity.ok(trainResponse);
    }

    @GetMapping ("/number/{trainNumber}")
    public ResponseEntity<?> getTrainByNumber(@PathVariable String trainNumber) {
        TrainResponse trainResponse = trainService.getTrainByNumber(trainNumber);
        return ResponseEntity.ok(trainResponse);
    }
    @GetMapping(value = "/search", params = "name")
    public ResponseEntity<?> searchTrainsByName(@RequestParam String name) {
        List<TrainResponse> trains = trainService.getTrainsByName(name);
        return ResponseEntity.ok(trains);
    }

    @PutMapping("/{trainId}")
    public ResponseEntity<?> updateTrain(@PathVariable Long trainId, @RequestBody TrainRequest trainRequest) {
        trainService.updateTrain(trainId, trainRequest);
        return ResponseEntity.ok("Updating train with ID: " + trainId);
    }
    @PatchMapping("/{trainId}/status")
    public ResponseEntity<?> updateTrainStatus(@PathVariable Long trainId, @RequestParam String status) {
        trainService.updateTrainStatus(trainId, status);
        return ResponseEntity.ok("Updating train status with ID: " + trainId);
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
