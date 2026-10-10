package com.Projects.SmartRailApplication.auth.service;

import org.springframework.stereotype.Service;

import com.Projects.SmartRailApplication.auth.dto.UserRegistrationRequest;
import com.Projects.SmartRailApplication.auth.dto.UserResponse;
import com.Projects.SmartRailApplication.auth.entities.Role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service 
public class authService {

    @Autowired
    private userService userService;
    public UserResponse registerUser(UserRegistrationRequest userRegistrationRequest) {
        userRegistrationRequest.setPassword(new BCryptPasswordEncoder().encode(userRegistrationRequest.getPassword()));
        if(userRegistrationRequest.getRole() == null) {
            userRegistrationRequest.setRole(Role.USER); // Default role
        }
        return userService.createUser(userRegistrationRequest);
    }
    
}
