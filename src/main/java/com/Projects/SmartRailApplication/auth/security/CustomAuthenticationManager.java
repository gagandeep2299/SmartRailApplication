package com.Projects.SmartRailApplication.auth.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.authentication.*;
import org.springframework.stereotype.Component;
import com.Projects.SmartRailApplication.auth.entities.User;
import com.Projects.SmartRailApplication.auth.service.*;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@Component 
@AllArgsConstructor 
public class CustomAuthenticationManager implements AuthenticationManager {

    
    private final userService userService;
    
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        User user;
        try{
        user = userService.getUserEntityByEmail(authentication.getName());
        } catch (EntityNotFoundException e) {
            throw new BadCredentialsException("User not with "+authentication.getName()+" does not exist");
        }
        boolean passwordMatches = new BCryptPasswordEncoder().matches(authentication.getCredentials().toString(), user.getPassword());
        if(!passwordMatches){
            throw new BadCredentialsException("Invalid username or password");
        }
        return new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
    }   
    
}
