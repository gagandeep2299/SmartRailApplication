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
import org.springframework.web.bind.annotation.RestController;

import com.Projects.SmartRailApplication.train.Service.RouteStopService;
import com.Projects.SmartRailApplication.train.dto.RouteStopRequest;
import com.Projects.SmartRailApplication.train.dto.RouteStopResponse;

import jakarta.persistence.EntityNotFoundException;

@RestController 
@RequestMapping ("/api/v1")
public class RouteStopController {
    @Autowired 
    private RouteStopService routeStopService;

    @PostMapping ("/trains/{trainId}/route-stops")
    public ResponseEntity<String> addRouteStop(@PathVariable Long trainId, @RequestBody RouteStopRequest routeStopRequest) {
        try{
            routeStopService.addRouteStop(trainId, routeStopRequest);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred while adding the route stop");
        }
        routeStopService.addRouteStop(trainId, routeStopRequest);
        return ResponseEntity.ok("Adding route stop for train with ID: " + trainId);
    }

    @GetMapping ("/route-stops/{id}")
    public ResponseEntity<?> getRouteStopsById(@PathVariable Long id) {
        RouteStopResponse response;
        try {
            response = routeStopService.getRouteStopsById(id);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body("Route stop not found with ID: " + id);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(response);
    }
    @GetMapping ("/trains/{trainId}/route-stops")
    public ResponseEntity<?> getRouteStopsByTrain(@PathVariable Long trainId) {
        try {
            return ResponseEntity.ok(routeStopService.getRouteStopsByTrain(trainId));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body("Train not found with ID: " + trainId);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred while retrieving route stops for train with ID: " + trainId);
        }
    }
    @PutMapping ("/route-stops/{id}")
    public ResponseEntity<?> updateRouteStop(@PathVariable Long id, @RequestBody RouteStopRequest routeStopRequest) {
        try {
            RouteStopResponse updatedRouteStop = routeStopService.updateRouteStop(id, routeStopRequest);
            return ResponseEntity.ok(updatedRouteStop);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body("Route stop not found with ID: " + id);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred while updating the route stop");
        }
    }
    @DeleteMapping("/route-stops/{id}")
    public ResponseEntity<?> deleteRouteStop(@PathVariable Long id) {
        try {
            routeStopService.deleteRouteStop(id);
            return ResponseEntity.ok("Route stop deleted successfully with ID: " + id);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body("Route stop not found with ID: " + id);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred while deleting the route stop");
        }
    }
}
