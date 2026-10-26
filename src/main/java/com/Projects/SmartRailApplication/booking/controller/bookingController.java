package com.Projects.SmartRailApplication.booking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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

import jakarta.persistence.EntityNotFoundException;

@RestController 
@RequestMapping ("/api/v1")
public class bookingController {

    @Autowired 
    private BookingServices bookingServices;
    
    @PostMapping ("/bookings")
    public ResponseEntity<?> createBooking(@AuthenticationPrincipal CustomUserDetails userDetails,@RequestBody bookingRequest bookingRequest) {
        try{
            return ResponseEntity.status(HttpStatus.CREATED).body(bookingServices.createBooking(bookingRequest,userDetails.getUser()));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while creating the booking. "+   e.getMessage());
        }
    }

    @GetMapping ("/bookings/{id}")
    public ResponseEntity<?> getBookingById(@PathVariable("id") Long id) {
        try{
            return ResponseEntity.status(HttpStatus.OK).body(bookingServices.getBookingById(id));
        } catch (EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch ( Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping ("bookings")
    public ResponseEntity<?> getBookingsByUserId(@AuthenticationPrincipal CustomUserDetails userDetails) {
        try{
            return ResponseEntity.status(HttpStatus.OK).body(bookingServices.getBookingByUser(userDetails));
        } catch(EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());

        }
    }
    @GetMapping ("/bookings/{id}/cancel")
    public ResponseEntity<?> cancelBooking(@PathVariable("id") Long id) {
        return ResponseEntity.ok().build();
    }
}
