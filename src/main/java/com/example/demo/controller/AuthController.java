package com.example.demo.controller;

import com.example.demo.dto.RegisterRequest;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.dto.LoginRequest;
import com.example.demo.service.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	private UserRepository userRepository;

    @Autowired
    private AuthService authService;
	private User userRepo;

    // ✅ Register new user
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        String result = authService.register(request);

        if (result.startsWith("✅")) {
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    // ✅ Login and return JWT token
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        String token = authService.login(request);

        if (token.startsWith("❌")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", token));
        } else {
            return ResponseEntity.ok(Map.of("token", token));
        }
    }
    @DeleteMapping("/delete-my-account")
    public ResponseEntity<String> deleteSelf(@AuthenticationPrincipal UserDetails userDetails) {

    	User user = userRepository.findByUsername(userDetails.getUsername()).orElse(null);
    	if (user == null) {
    	    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("❌ User not found");
    	}
    	userRepository.delete(user);
    	return ResponseEntity.ok("✅ Your account has been deleted");
    }
}
