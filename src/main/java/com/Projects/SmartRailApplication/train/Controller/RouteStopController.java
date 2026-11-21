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


@RestController 
@RequestMapping ("/api/v1")
public class RouteStopController {
    @Autowired 
    private RouteStopService routeStopService;

    @PostMapping ("/trains/{trainId}/route-stops")
    public ResponseEntity<String> addRouteStop(@PathVariable Long trainId, @RequestBody RouteStopRequest routeStopRequest) {
        routeStopService.addRouteStop(trainId, routeStopRequest);
        return ResponseEntity.ok("Adding route stop for train with ID: " + trainId);
    }

    @GetMapping ("/route-stops/{id}")
    public ResponseEntity<?> getRouteStopsById(@PathVariable Long id) {
        RouteStopResponse response = routeStopService.getRouteStopsById(id);
        return ResponseEntity.ok(response);
    }
    @GetMapping ("/trains/{trainId}/route-stops")
    public ResponseEntity<?> getRouteStopsByTrain(@PathVariable Long trainId) {
        return ResponseEntity.ok(routeStopService.getRouteStopsByTrain(trainId));
        
    }
    @PutMapping ("/route-stops/{id}")
    public ResponseEntity<?> updateRouteStop(@PathVariable Long id, @RequestBody RouteStopRequest routeStopRequest) {
        RouteStopResponse updatedRouteStop = routeStopService.updateRouteStop(id, routeStopRequest);
        return ResponseEntity.ok(updatedRouteStop);
        
    }
    @DeleteMapping("/route-stops/{id}")
    public ResponseEntity<?> deleteRouteStop(@PathVariable Long id) {
        routeStopService.deleteRouteStop(id);
        return ResponseEntity.ok("Route stop deleted successfully with ID: " + id);
        
    }
}
