package com.Projects.SmartRailApplication.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController 
@RequestMapping ("/api/v1/auth")
public class AuthController {
    @PostMapping("/register")
    public String registerUser() {
        return "User registered successfully";
    }

    @GetMapping ("/login")
    public String getUser(@PathVariable Long id) {
        return "User with ID " + id + " retrieved successfully";
    }
}
