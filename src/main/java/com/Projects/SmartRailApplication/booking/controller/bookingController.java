package com.Projects.SmartRailApplication.booking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Projects.SmartRailApplication.auth.entities.CustomUserDetails;
import com.Projects.SmartRailApplication.booking.dto.bookingRequest;
import com.Projects.SmartRailApplication.booking.service.BookingServices;

@RestController 
@RequestMapping ("/api/v1")
public class bookingController {

    @Autowired 
    private BookingServices bookingServices;
    
    @PostMapping ("/bookings")
    public ResponseEntity<?> createBooking(@AuthenticationPrincipal CustomUserDetails userDetails,@RequestBody bookingRequest bookingRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingServices.createBooking(bookingRequest,userDetails.getUser()));
        
    }

    @GetMapping ("/bookings/{id}")
    public ResponseEntity<?> getBookingById(@AuthenticationPrincipal CustomUserDetails userDetails,@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(bookingServices.getBookingById(userDetails.getUser().getId(), id));
        
    }

    @GetMapping ("bookings")
    public ResponseEntity<?> getBookingsByUserId(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.OK).body(bookingServices.getBookingByUser(userDetails));
        
    }
    @GetMapping ("/bookings/{id}/cancel")
    public ResponseEntity<?> cancelBooking(@AuthenticationPrincipal CustomUserDetails userDetails,@PathVariable("id") Long id) {
        return ResponseEntity.ok().build();
    }
}
