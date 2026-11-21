package com.Projects.SmartRailApplication.auth.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.Projects.SmartRailApplication.api.exception.ResourceConflictException;
import com.Projects.SmartRailApplication.api.exception.ResourceNotFoundException;
import com.Projects.SmartRailApplication.auth.dto.UserRegistrationRequest;
import com.Projects.SmartRailApplication.auth.dto.UserResponse;
import com.Projects.SmartRailApplication.auth.entities.Role;
import com.Projects.SmartRailApplication.auth.entities.User;
import com.Projects.SmartRailApplication.auth.repository.UserRepository;


@Service 
public class userServiceImpl implements userService {
    @Autowired 
    private UserRepository userRepository;

    @Override
    public UserResponse createUser(UserRegistrationRequest userRegistrationRequest) {
        Optional<User> users = userRepository.findByEmailIgnoreCase(userRegistrationRequest.getEmail());
        if(users.isPresent()) {
            throw new ResourceConflictException("User with this email already exists");
        }
        User user = new User();
        user.setName(userRegistrationRequest.getName());
        user.setEmail(userRegistrationRequest.getEmail());
        user.setPassword(new BCryptPasswordEncoder().encode(userRegistrationRequest.getPassword()));
        user.setRole(Role.USER); // Default role
        user.setCreatedAt(java.time.LocalDateTime.now());
        user.setUpdatedAt(java.time.LocalDateTime.now());
        return mapToEntity(userRepository.save(user));
    }

    
    @Override
    public UserResponse getUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with ID " + id + " does not exist"));
        return mapToEntity(user);
    }

    @Override
    public UserResponse updateUser(Long id, UserRegistrationRequest userRegistrationRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with ID " + id + " does not exist"));
        if(userRegistrationRequest.getName() != null) {
            user.setName(userRegistrationRequest.getName());
        }
        if(userRegistrationRequest.getEmail() != null) {
            user.setEmail(userRegistrationRequest.getEmail());
        }
        if(userRegistrationRequest.getPassword() != null) {
            user.setPassword(new BCryptPasswordEncoder().encode(userRegistrationRequest.getPassword()));
        }
        user.setUpdatedAt(java.time.LocalDateTime.now());
        return mapToEntity(userRepository.save(user));
    }
    
        @Override
        public UserResponse getUserByEmail(String email) {
            Optional<User> user = userRepository.findByEmailIgnoreCase(email);
            if(user.isEmpty()) {
                throw new ResourceNotFoundException("User with email " + email + " does not exist");
            }
            return mapToEntity(user.get());
        }
    
        @Override
        public User getUserEntityByEmail(String email) {
            return userRepository.findByEmailIgnoreCase(email)
                    .orElseThrow(() -> new ResourceNotFoundException("User with email " + email + " does not exist"));
        }
    private UserResponse mapToEntity(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setName(user.getName());
        userResponse.setEmail(user.getEmail());
        userResponse.setRole(user.getRole());
        userResponse.setCreatedAt(user.getCreatedAt());
        userResponse.setUpdatedAt(user.getUpdatedAt());
        return userResponse;
    }



}
