package com.Projects.SmartRailApplication.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.Projects.SmartRailApplication.auth.service.*;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import com.Projects.SmartRailApplication.auth.dto.UserRegistrationRequest;
import com.Projects.SmartRailApplication.auth.dto.UserResponse;

@RestController 
@RequestMapping ("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private userService userService;
    @PostMapping 
    public ResponseEntity<?> createUser(@RequestBody UserRegistrationRequest userRequest) {
        UserResponse userResponse;
        try{
            userResponse = userService.createUser(userRequest);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());    
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating user");
        }
        return ResponseEntity.ok(userResponse);
    }

    @GetMapping ("/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        try{
            return ResponseEntity.ok(userService.getUser(id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());    
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error retrieving user");
        }
    }

    @GetMapping ("/email/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email) {
        try{
            return ResponseEntity.ok(userService.getUserByEmail(email));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());    
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error retrieving user");
        }
    }

    @PutMapping ("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserRegistrationRequest userRequest) {
        try{
            return ResponseEntity.ok(userService.updateUser(id, userRequest));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating user");
        }
    }
}
