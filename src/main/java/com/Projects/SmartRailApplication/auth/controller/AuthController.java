package com.Projects.SmartRailApplication.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Projects.SmartRailApplication.auth.dto.UserRegistrationRequest;
import com.Projects.SmartRailApplication.auth.service.*;

@RestController 
@RequestMapping ("/api/v1/auth")
public class AuthController {
    @Autowired 
    private authService authService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationRequest userRegistrationRequest) {
        try{
            return ResponseEntity.ok(authService.registerUser(userRegistrationRequest));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred while registering user");
        }
    }

    @GetMapping ("/login")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        return ResponseEntity.ok("User with ID " + id + " retrieved successfully");
    }
}
