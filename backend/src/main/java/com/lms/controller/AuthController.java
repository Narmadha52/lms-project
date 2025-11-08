package com.lms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lms.dto.ApiResponse;
import com.lms.dto.JwtResponse;
import com.lms.dto.LoginRequest;
import com.lms.dto.SignupRequest;
import com.lms.dto.UserDto;
import com.lms.model.User;
import com.lms.repository.UserRepository;
import com.lms.service.AuthService;
import com.lms.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")

public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;
    
    @Autowired
    private PasswordEncoder passwordEncoder; 
    
    @Autowired
    private UserRepository repo;

    @PostMapping("/signin")
    public ResponseEntity<ApiResponse<JwtResponse>> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            JwtResponse jwtResponse = authService.authenticateUser(loginRequest);
            System.out.println(jwtResponse);
            return ResponseEntity.ok(ApiResponse.success("User signed in successfully", jwtResponse));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error: " + e.getMessage()));
        }
    }

    @PostMapping(value = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<UserDto>> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        try {
            var user = authService.registerUser(signUpRequest);
            UserDto userDto = userService.convertToDto(user);
            return ResponseEntity.ok(ApiResponse.success("User registered successfully", userDto));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error: " + e.getMessage()));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserDto>> getCurrentUser() {
        try {
            var user = authService.getCurrentUser();
            UserDto userDto = userService.convertToDto(user);
            return ResponseEntity.ok(ApiResponse.success("User retrieved successfully", userDto));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error: " + e.getMessage()));
        }
    }
    
    @GetMapping("/reset-admin-password")
    public ResponseEntity<ApiResponse<String>> resetAdminPassword() {
        try {
            User admin = repo.findByUsernameOrEmail("admin@lms.com", "admin@lms.com")
                    .orElseThrow(() -> new RuntimeException("Admin user not found"));
            
            // Set password to "admin123"
            admin.setPassword(passwordEncoder.encode("admin123"));
            repo.save(admin);
            
            return ResponseEntity.ok(ApiResponse.success(
                "Admin password reset to 'admin123' successfully", 
                "Password updated for: " + admin.getEmail()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error resetting password: " + e.getMessage()));
        }
    }
}

