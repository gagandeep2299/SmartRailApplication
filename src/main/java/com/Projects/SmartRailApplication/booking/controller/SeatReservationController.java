package com.Projects.SmartRailApplication.booking.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Projects.SmartRailApplication.booking.dto.ReservationRequest;
import com.Projects.SmartRailApplication.booking.service.SeatReservationService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@RestController 
@RequestMapping ("/api/v1/reservations")
@RequiredArgsConstructor 
public class SeatReservationController {
    private final SeatReservationService service;

    @PostMapping 
    public ResponseEntity<?> reserveSeat(@RequestBody ReservationRequest request){
        try{
            return ResponseEntity.status(HttpStatus.CREATED).body(service.reserveSeat(request));
        } catch (EntityNotFoundException exception){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        } catch ( IllegalArgumentException exception){
            return ResponseEntity.badRequest().body(exception.getMessage());
        } catch (IllegalStateException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }

    }
}
