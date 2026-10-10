package com.Projects.SmartRailApplication.auth.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.Projects.SmartRailApplication.auth.dto.LoginRequest;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.*;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Date;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.*;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final CustomAuthenticationManager customAuthenticationManager;
    private final String jwtSecret;
    private final long jwtExpiration;

    public AuthenticationFilter(CustomAuthenticationManager customAuthenticationManager, String jwtSecret, long jwtExpiration) {
        this.customAuthenticationManager = customAuthenticationManager;
        this.jwtSecret = jwtSecret;
        this.jwtExpiration = jwtExpiration;
    }
    @Override 
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try{
            LoginRequest userData = new ObjectMapper().readValue(request.getInputStream(),LoginRequest.class);
            Authentication authRequest = new UsernamePasswordAuthenticationToken(userData.getEmail(), userData.getPassword());
            return customAuthenticationManager.authenticate(authRequest);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override 
    public void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        String email = authResult.getName();
        String token = JWT.create().withSubject(email).withExpiresAt(new Date(System.currentTimeMillis() + jwtExpiration)).sign(Algorithm.HMAC512(jwtSecret));
        response.addHeader("Authorization", "Bearer " + token);
        response.getWriter().write("Authentication successful.");
        response.getWriter().flush();
    }
    @Override 
    public void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("Authentication failed.");
        response.getWriter().flush();
    }
}
