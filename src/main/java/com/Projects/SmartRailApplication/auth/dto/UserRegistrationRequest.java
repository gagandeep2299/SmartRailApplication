package com.Projects.SmartRailApplication.auth.dto;

import com.Projects.SmartRailApplication.auth.entities.Role;

import lombok.Data;

@Data 
public class UserRegistrationRequest {
    private Long id;
    private String name;
    private String email;
    private String password;
    private Role role;
}
