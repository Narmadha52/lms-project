package com.lms.controller;

import com.lms.dto.*;
import com.lms.model.Role;
import com.lms.model.User;
import com.lms.service.AuthService;
import com.lms.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Unit tests for AuthController class.
 * This test class verifies the behavior of all endpoints inside AuthController.
 * It uses Mockito for mocking service dependencies and JUnit 5 for assertions.
 */
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthController authController;

    // DTOs and mock objects used across multiple tests
    private LoginRequest loginRequest;
    private SignupRequest signupRequest;
    private JwtResponse jwtResponse;
    private User mockUser;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Creating mock login request
        loginRequest = new LoginRequest("john_doe", "securepassword");

        // Creating mock signup request
        signupRequest = new SignupRequest();
        signupRequest.setUsername("john_doe");
        signupRequest.setEmail("john@example.com");
        signupRequest.setPassword("securepassword");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        signupRequest.setRole(Role.STUDENT);

        // Simulate a JWT response from AuthService
        jwtResponse = new JwtResponse(
                "mock-jwt-token",
                1L,
                "john_doe",
                "john@example.com",
                "John",
                "Doe",
                Role.STUDENT,
                true
        );

        // Create a mock User entity for testing
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("john_doe");
        mockUser.setEmail("john@example.com");
        mockUser.setFirstName("John");
        mockUser.setLastName("Doe");
        mockUser.setRole(Role.STUDENT);
        mockUser.setIsApproved(true);

        // Create corresponding DTO
        userDto = new UserDto(
                1L,
                "john_doe",
                "john@example.com",
                "John",
                "Doe",
                Role.STUDENT,
                true,
                null,
                null
        );
    }

    // ------------------------------------------------------------------------------------
    // TEST CASES FOR /auth/signin
    // ------------------------------------------------------------------------------------

    @Test
    @DisplayName("Test successful user authentication")
    void testAuthenticateUser_Success() {
        when(authService.authenticateUser(any(LoginRequest.class))).thenReturn(jwtResponse);

        ResponseEntity<ApiResponse<JwtResponse>> response = authController.authenticateUser(loginRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().isSuccess());
        assertEquals("john_doe", response.getBody().getData().getUsername());
        assertEquals("mock-jwt-token", response.getBody().getData().getAccessToken());
        assertEquals("User signed in successfully", response.getBody().getMessage());
    }

    @Test
    @DisplayName("Test failed user authentication due to invalid credentials")
    void testAuthenticateUser_Failure() {
        when(authService.authenticateUser(any(LoginRequest.class)))
                .thenThrow(new RuntimeException("Invalid username or password"));

        ResponseEntity<ApiResponse<JwtResponse>> response = authController.authenticateUser(loginRequest);

        assertEquals(400, response.getStatusCodeValue());
        assertFalse(response.getBody().isSuccess());
        assertTrue(response.getBody().getMessage().contains("Invalid username or password"));
        assertNull(response.getBody().getData());
    }

    // ------------------------------------------------------------------------------------
    // TEST CASES FOR /auth/signup
    // ------------------------------------------------------------------------------------

    @Test
    @DisplayName("Test successful user registration")
    void testRegisterUser_Success() {
        when(authService.registerUser(any(SignupRequest.class))).thenReturn(mockUser);
        when(userService.convertToDto(mockUser)).thenReturn(userDto);

        ResponseEntity<ApiResponse<UserDto>> response = authController.registerUser(signupRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().isSuccess());
        assertEquals("john_doe", response.getBody().getData().getUsername());
        assertEquals("User registered successfully", response.getBody().getMessage());
    }

    @Test
    @DisplayName("Test user registration failure due to duplicate email")
    void testRegisterUser_Failure() {
        when(authService.registerUser(any(SignupRequest.class)))
                .thenThrow(new RuntimeException("Email already exists"));

        ResponseEntity<ApiResponse<UserDto>> response = authController.registerUser(signupRequest);

        assertEquals(400, response.getStatusCodeValue());
        assertFalse(response.getBody().isSuccess());
        assertTrue(response.getBody().getMessage().contains("Email already exists"));
    }

    // ------------------------------------------------------------------------------------
    // TEST CASES FOR /auth/me
    // ------------------------------------------------------------------------------------

    @Test
    @DisplayName("Test get current user details success")
    void testGetCurrentUser_Success() {
        when(authService.getCurrentUser()).thenReturn(mockUser);
        when(userService.convertToDto(mockUser)).thenReturn(userDto);

        ResponseEntity<ApiResponse<UserDto>> response = authController.getCurrentUser();

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().isSuccess());
        assertEquals("john_doe", response.getBody().getData().getUsername());
        assertEquals("User retrieved successfully", response.getBody().getMessage());
    }

    @Test
    @DisplayName("Test get current user failure when no user found")
    void testGetCurrentUser_Failure() {
        when(authService.getCurrentUser()).thenThrow(new RuntimeException("User not found"));

        ResponseEntity<ApiResponse<UserDto>> response = authController.getCurrentUser();

        assertEquals(400, response.getStatusCodeValue());
        assertFalse(response.getBody().isSuccess());
        assertTrue(response.getBody().getMessage().contains("User not found"));
    }
}
