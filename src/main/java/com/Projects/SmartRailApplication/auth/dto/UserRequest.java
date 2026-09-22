package com.Projects.SmartRailApplication.auth.dto;

import java.time.LocalDateTime;

import com.Projects.SmartRailApplication.auth.entities.Role;

import lombok.Data;

@Data 
public class UserRequest {
    private Long id;
    private String name;
    private String email;
    private String password;
    private Role role;
    private LocalDateTime createdAt;
}
