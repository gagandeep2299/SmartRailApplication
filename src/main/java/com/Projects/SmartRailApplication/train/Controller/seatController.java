package com.Projects.SmartRailApplication.train.Controller;

import java.util.List;

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
import com.Projects.SmartRailApplication.train.Service.seatService;
import com.Projects.SmartRailApplication.train.dto.SeatRequest;
import com.Projects.SmartRailApplication.train.dto.SeatResponse;

@RestController
@RequestMapping("/api/v1")
public class seatController {
    @Autowired
    private seatService seatService;

    @PostMapping("/coaches/{coachId}/seats")
    public ResponseEntity<?> addSeat(@PathVariable Long coachId, @RequestBody SeatRequest seatRequest) {
        SeatResponse seatResponse = seatService.addSeat(coachId, seatRequest);
        return ResponseEntity.ok(seatResponse);
    }

    @GetMapping("/coaches/{coachId}/seats")
    public ResponseEntity<?> getSeatsByCoachId(@PathVariable Long coachId) {
        List<SeatResponse> seatResponses = seatService.getSeatsByCoachId(coachId);
        return ResponseEntity.ok(seatResponses);
    }
    @GetMapping("/seats/{id}")
    public ResponseEntity<?> getSeatById(@PathVariable Long id) {
        SeatResponse seatResponse = seatService.getSeatById(id);
        return ResponseEntity.ok(seatResponse);
    }

    @PutMapping("/seats/{id}")
    public ResponseEntity<?> updateSeat(@PathVariable Long id, @RequestBody SeatRequest seatRequest) {
        SeatResponse seatResponse = seatService.updateSeat(id, seatRequest);
        return ResponseEntity.ok(seatResponse);
    }
    @DeleteMapping("/seats/{id}")
    public ResponseEntity<?> deleteSeat(@PathVariable Long id) {
        seatService.deleteSeat(id);
        return ResponseEntity.noContent().build();
    }
}
