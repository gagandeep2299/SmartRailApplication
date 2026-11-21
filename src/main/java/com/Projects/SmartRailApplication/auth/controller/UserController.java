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
import lombok.RequiredArgsConstructor;

import com.Projects.SmartRailApplication.auth.dto.UserRegistrationRequest;

@RestController 
@RequestMapping ("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final userService userService;
    @PostMapping 
    public ResponseEntity<?> createUser(@RequestBody UserRegistrationRequest userRequest) {
        return ResponseEntity.ok(userService.createUser(userRequest));
    }

    @GetMapping ("/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @GetMapping ("/email/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @PutMapping ("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserRegistrationRequest userRequest) {
        return ResponseEntity.ok(userService.updateUser(id, userRequest));
    }
}
