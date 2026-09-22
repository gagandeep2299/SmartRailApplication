package com.Projects.SmartRailApplication.auth.dto;

import java.time.LocalDateTime;

import com.Projects.SmartRailApplication.auth.entities.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder 
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private String password;
    private Role role;
    private LocalDateTime createdAt;
}
