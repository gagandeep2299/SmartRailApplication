package com.Projects.SmartRailApplication.train.Controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Projects.SmartRailApplication.train.dto.TrainSearchResponse;
import com.Projects.SmartRailApplication.train.Entities.Seat;
import com.Projects.SmartRailApplication.train.Service.trainSearchService;

@RestController 
@RequestMapping ("/api/v1/trains")
public class trainSearchController {
    @Autowired 
    private  trainSearchService trainSearchService;

    @GetMapping ("/search")
    public ResponseEntity<?> searchTrains(@RequestParam String from, @RequestParam String to, @RequestParam LocalDate date) {
        // Implement the logic to search for trains based on the provided parameters
        // You can call a service method to perform the search and return the results
        List<TrainSearchResponse> searchResults = trainSearchService.searchTrains(from, to, date);
        return ResponseEntity.ok(searchResults);
    }

    @GetMapping ("/{id}/availability")
    public ResponseEntity<?> checkAvailability(@PathVariable Long id,@RequestParam LocalDate travelDate, @RequestParam Long sourceStationId, @RequestParam Long destinationStationId) {
        if(travelDate == null || sourceStationId == null || destinationStationId == null || sourceStationId >= destinationStationId) {
            return ResponseEntity.badRequest().body("Invalid parameters");
        }
        System.out.println("Checking availability for train ID: " +id);
        List<Seat> availableSeats = trainSearchService.isTrainAvailable(id,travelDate, sourceStationId, destinationStationId);
        return ResponseEntity.ok().body(availableSeats);
    }
}
