package com.Projects.SmartRailApplication.auth.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import com.Projects.SmartRailApplication.auth.repository.UserRepository;

@Configuration 
public class securityConfig {
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value ("${jwt.expiration}")
    private long jwtExpiration;

    private final CustomAuthenticationManager customAuthenticationManager;
    private final UserRepository userRepository;

    public securityConfig(CustomAuthenticationManager customAuthenticationManager, UserRepository userRepository) {
        this.customAuthenticationManager = customAuthenticationManager;
        this.userRepository = userRepository;
    }
    
    @Bean 
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(customAuthenticationManager, jwtSecret, jwtExpiration);
        authenticationFilter.setFilterProcessesUrl("/api/v1/auth/login");
        http.csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/v1/auth/register").permitAll()
                .requestMatchers("/api/v1/auth/login").permitAll()
                //.requestMatchers("/api/v1/*").permitAll() // added for testing purposes, remove this line in production
                .anyRequest().authenticated()
            )
            .addFilter(authenticationFilter)
            .addFilterAfter(new JwtAuthenticationFilter(jwtSecret, userRepository), AuthenticationFilter.class)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }
}
