package com.Projects.SmartRailApplication.auth.service;

import com.Projects.SmartRailApplication.auth.dto.UserRegistrationRequest;
import com.Projects.SmartRailApplication.auth.dto.UserResponse;

public interface userService {
    public UserResponse createUser(UserRegistrationRequest userRegistrationRequest);
    public UserResponse getUser(Long id);
    public UserResponse updateUser(Long id, UserRegistrationRequest userRegistrationRequest);
    public UserResponse getUserByEmail(String email);
}
