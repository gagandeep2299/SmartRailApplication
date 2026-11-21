package com.Projects.SmartRailApplication.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data 
public class UserRegistrationRequest {
    private Long id;
    @NotBlank 
    private String name;
    private String email;
    private String password;
}
