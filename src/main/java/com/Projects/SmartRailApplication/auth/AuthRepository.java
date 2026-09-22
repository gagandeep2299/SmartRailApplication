package com.Projects.SmartRailApplication.auth;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Projects.SmartRailApplication.auth.entities.User;

public interface AuthRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    
}
