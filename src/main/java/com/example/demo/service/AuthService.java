package com.example.demo.service;

import com.example.demo.config.JwtUtil;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    public String register(RegisterRequest request) {
        Optional<User> existing = userRepo.findByUsername(request.getUsername());
        if (existing.isPresent()) {
            return "❌ Username already exists";
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole().toUpperCase());
        user.setEmail(request.getEmail());

        userRepo.save(user);
        return "✅ User registered successfully";
    }

    public String login(LoginRequest request) {
        try {
            authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            User user = userRepo.findByUsername(request.getUsername()).get();

            Map<String, Object> claims = new HashMap<>();
            claims.put("role", user.getRole());

            return jwtUtil.generateToken(claims, user.getUsername());
        } catch (BadCredentialsException e) {
            return "❌ Invalid credentials";
        }
    }
}
