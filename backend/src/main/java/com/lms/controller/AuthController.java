package com.lms.controller;

import com.lms.dto.ApiResponse;
import com.lms.dto.JwtResponse;
import com.lms.dto.LoginRequest;
import com.lms.dto.SignupRequest;
import com.lms.dto.UserDto;
import com.lms.service.AuthService;
import com.lms.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@CrossOrigin(origins = "http://192.168.1.8:3000")
@RestController
@RequestMapping("/auth")

public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @PostMapping("/signin")
    public ResponseEntity<ApiResponse<JwtResponse>> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            JwtResponse jwtResponse = authService.authenticateUser(loginRequest);
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
}

