package com.lms.controller;

import com.lms.dto.*;
import com.lms.model.User;
import com.lms.service.AuthService;
import com.lms.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthService authService;

    @Mock
    private UserService userService;

    private LoginRequest loginRequest;
    private SignupRequest signupRequest;
    private JwtResponse jwtResponse;
    private User user;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        setupTestData();
    }

    private void setupTestData() {
        // Login Request
        loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password123");

        // Signup Request
        signupRequest = new SignupRequest();
        signupRequest.setUsername("newuser");
        signupRequest.setEmail("newuser@test.com");
        signupRequest.setPassword("password123");

        // JWT Response
        jwtResponse = new JwtResponse();
        jwtResponse.setAccessToken("mock-jwt-token");
        jwtResponse.setType("Bearer");

        // User
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@test.com");

        // UserDto
        userDto = new UserDto();
        userDto.setId(1L);
        userDto.setUsername("testuser");
        userDto.setEmail("test@test.com");
    }

    // ========== authenticateUser Tests ==========

    @Test
    @DisplayName("Should authenticate user successfully")
    void testAuthenticateUser_Success() {
        // Arrange
        when(authService.authenticateUser(loginRequest)).thenReturn(jwtResponse);

        // Act
        ResponseEntity<ApiResponse<JwtResponse>> response = 
                authController.authenticateUser(loginRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        assertEquals("User signed in successfully", response.getBody().getMessage());
        assertEquals(jwtResponse, response.getBody().getData());
        assertEquals("mock-jwt-token", response.getBody().getData().getAccessToken());
        
        verify(authService, times(1)).authenticateUser(loginRequest);
    }

    @Test
    @DisplayName("Should return error for invalid credentials")
    void testAuthenticateUser_InvalidCredentials() {
        // Arrange
        when(authService.authenticateUser(loginRequest))
                .thenThrow(new RuntimeException("Invalid username or password"));

        // Act
        ResponseEntity<ApiResponse<JwtResponse>> response = 
                authController.authenticateUser(loginRequest);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertTrue(response.getBody().getMessage().contains("Invalid username or password"));
        assertNull(response.getBody().getData());
        
        verify(authService, times(1)).authenticateUser(loginRequest);
    }

    @Test
    @DisplayName("Should handle authentication service exception")
    void testAuthenticateUser_ServiceException() {
        // Arrange
        when(authService.authenticateUser(loginRequest))
                .thenThrow(new RuntimeException("Authentication service unavailable"));

        // Act
        ResponseEntity<ApiResponse<JwtResponse>> response = 
                authController.authenticateUser(loginRequest);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertFalse(response.getBody().isSuccess());
        assertTrue(response.getBody().getMessage().contains("Authentication service unavailable"));
        
        verify(authService, times(1)).authenticateUser(loginRequest);
    }

    // ========== registerUser Tests ==========

    @Test
    @DisplayName("Should register user successfully")
    void testRegisterUser_Success() {
        // Arrange
        when(authService.registerUser(signupRequest)).thenReturn(user);
        when(userService.convertToDto(user)).thenReturn(userDto);

        // Act
        ResponseEntity<ApiResponse<UserDto>> response = 
                authController.registerUser(signupRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
        assertEquals("User registered successfully", response.getBody().getMessage());
        assertEquals(userDto, response.getBody().getData());
        assertEquals("testuser", response.getBody().getData().getUsername());
        
        verify(authService, times(1)).registerUser(signupRequest);
        verify(userService, times(1)).convertToDto(user);
    }

    @Test
    @DisplayName("Should return error when username already exists")
    void testRegisterUser_UsernameExists() {
        // Arrange
        when(authService.registerUser(signupRequest))
                .thenThrow(new RuntimeException("Username is already taken"));

        // Act
        ResponseEntity<ApiResponse<UserDto>> response = 
                authController.registerUser(signupRequest);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertFalse(response.getBody().isSuccess());
        assertTrue(response.getBody().getMessage().contains("Username is already taken"));
        assertNull(response.getBody().getData());
        
        verify(authService, times(1)).registerUser(signupRequest);
        verify(userService, never()).convertToDto(any());
    }

    @Test
    @DisplayName("Should return error when email already exists")
    void testRegisterUser_EmailExists() {
        // Arrange
        when(authService.registerUser(signupRequest))
                .thenThrow(new RuntimeException("Email is already in use"));

        // Act
        ResponseEntity<ApiResponse<UserDto>> response = 
                authController.registerUser(signupRequest);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertFalse(response.getBody().isSuccess());
        assertTrue(response.getBody().getMessage().contains("Email is already in use"));
        
        verify(authService, times(1)).registerUser(signupRequest);
    }

    @Test
    @DisplayName("Should handle registration validation errors")
    void testRegisterUser_ValidationError() {
        // Arrange
        when(authService.registerUser(signupRequest))
                .thenThrow(new RuntimeException("Password must be at least 8 characters"));

        // Act
        ResponseEntity<ApiResponse<UserDto>> response = 
                authController.registerUser(signupRequest);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertFalse(response.getBody().isSuccess());
        assertTrue(response.getBody().getMessage().contains("Password must be at least 8 characters"));
        
        verify(authService, times(1)).registerUser(signupRequest);
    }

    // ========== getCurrentUser Tests ==========

    @Test
    @DisplayName("Should retrieve current user successfully")
    void testGetCurrentUser_Success() {
        // Arrange
        when(authService.getCurrentUser()).thenReturn(user);
        when(userService.convertToDto(user)).thenReturn(userDto);

        // Act
        ResponseEntity<ApiResponse<UserDto>> response = authController.getCurrentUser();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
        assertEquals("User retrieved successfully", response.getBody().getMessage());
        assertEquals(userDto, response.getBody().getData());
        assertNotNull(response.getBody().getData().getId());
        
        verify(authService, times(1)).getCurrentUser();
        verify(userService, times(1)).convertToDto(user);
    }

    @Test
    @DisplayName("Should return error when user not authenticated")
    void testGetCurrentUser_NotAuthenticated() {
        // Arrange
        when(authService.getCurrentUser())
                .thenThrow(new RuntimeException("User not authenticated"));

        // Act
        ResponseEntity<ApiResponse<UserDto>> response = authController.getCurrentUser();

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertFalse(response.getBody().isSuccess());
        assertTrue(response.getBody().getMessage().contains("User not authenticated"));
        assertNull(response.getBody().getData());
        
        verify(authService, times(1)).getCurrentUser();
        verify(userService, never()).convertToDto(any());
    }

    @Test
    @DisplayName("Should handle session expired error")
    void testGetCurrentUser_SessionExpired() {
        // Arrange
        when(authService.getCurrentUser())
                .thenThrow(new RuntimeException("Session expired"));

        // Act
        ResponseEntity<ApiResponse<UserDto>> response = authController.getCurrentUser();

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertFalse(response.getBody().isSuccess());
        assertTrue(response.getBody().getMessage().contains("Session expired"));
        
        verify(authService, times(1)).getCurrentUser();
    }

    @Test
    @DisplayName("Should handle user not found error")
    void testGetCurrentUser_UserNotFound() {
        // Arrange
        when(authService.getCurrentUser())
                .thenThrow(new RuntimeException("User not found"));

        // Act
        ResponseEntity<ApiResponse<UserDto>> response = authController.getCurrentUser();

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertFalse(response.getBody().isSuccess());
        
        verify(authService, times(1)).getCurrentUser();
    }
}
