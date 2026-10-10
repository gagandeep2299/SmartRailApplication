package com.Projects.SmartRailApplication.auth.service;

import com.Projects.SmartRailApplication.auth.dto.UserRegistrationRequest;
import com.Projects.SmartRailApplication.auth.dto.UserResponse;
import com.Projects.SmartRailApplication.auth.entities.User;

public interface userService {
    public UserResponse createUser(UserRegistrationRequest userRegistrationRequest);
    public UserResponse getUser(Long id);
    public UserResponse updateUser(Long id, UserRegistrationRequest userRegistrationRequest);
    public UserResponse getUserByEmail(String email);
    public User getUserEntityByEmail(String email);
}
