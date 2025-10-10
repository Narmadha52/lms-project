package com.lms.service;

import com.lms.dto.JwtResponse;
import com.lms.dto.LoginRequest;
import com.lms.dto.SignupRequest;
import com.lms.model.Role;
import com.lms.model.User;
import com.lms.config.JwtTokenProvider;

import com.lms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider tokenProvider;

    // --- Authentication Method ---
    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsernameOrEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
        UserDetailsServiceImpl.UserPrincipal userPrincipal = (UserDetailsServiceImpl.UserPrincipal) authentication.getPrincipal();

        return new JwtResponse(
                jwt,
                userPrincipal.getId(),
                userPrincipal.getUsername(),
                userPrincipal.getEmail(),
                userPrincipal.getFirstName(), // Requires getter in UserPrincipal
                userPrincipal.getLastName(),  // Requires getter in UserPrincipal
                userPrincipal.getRole(),      // Requires getter in UserPrincipal
                userPrincipal.getIsApproved()
        );
    }

    // --- Registration Method (Syntax Fixed) ---
    public User registerUser(SignupRequest signUpRequest) {
        // CRITICAL CHECKS: Prevent duplicate accounts
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new RuntimeException("Username is already taken!");
        }
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new RuntimeException("Email is already in use!");
        }

        // FIX: The illegal start of expression was due to placeholder comments in the constructor.
        // Assuming your User constructor is (username, email, password, firstName, lastName, role)
        User user = new User(
                signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                passwordEncoder.encode(signUpRequest.getPassword()), // Hashed Password
                signUpRequest.getFirstName(),
                signUpRequest.getLastName(),
                signUpRequest.getRole()
        ); // <- Syntax Corrected

        // Approval Logic
        if (user.getRole() == Role.INSTRUCTOR) {
            user.setIsApproved(false); // Instructor requires admin approval
        } else {
            user.setIsApproved(true);  // Student is auto-approved
        }

        return userRepository.save(user);
    }

    // --- getCurrentUser and other methods from your previous code ---
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsServiceImpl.UserPrincipal) {
            UserDetailsServiceImpl.UserPrincipal userPrincipal = (UserDetailsServiceImpl.UserPrincipal) authentication.getPrincipal();
            return userRepository.findById(userPrincipal.getId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
        }
        throw new RuntimeException("No authenticated user found");
    }

    // ... (rest of AuthService methods like isCurrentUserAdmin, etc.)
}