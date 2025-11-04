package com.lms.service;

import com.lms.config.JwtTokenProvider;
import com.lms.dto.JwtResponse;
import com.lms.dto.LoginRequest;
import com.lms.dto.SignupRequest;
import com.lms.model.Role;
import com.lms.model.User;
import com.lms.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("AuthService Tests")
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider tokenProvider;

    @InjectMocks
    private AuthService authService;

    private SignupRequest validStudentRequest;
    private SignupRequest validInstructorRequest;
    private LoginRequest validLoginRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        setupTestData();
    }

    private void setupTestData() {
        validStudentRequest = new SignupRequest();
        validStudentRequest.setUsername("john_doe");
        validStudentRequest.setEmail("john@example.com");
        validStudentRequest.setPassword("password123");
        validStudentRequest.setFirstName("John");
        validStudentRequest.setLastName("Doe");
        validStudentRequest.setRole(Role.STUDENT);

        validInstructorRequest = new SignupRequest();
        validInstructorRequest.setUsername("jane_smith");
        validInstructorRequest.setEmail("jane@example.com");
        validInstructorRequest.setPassword("password456");
        validInstructorRequest.setFirstName("Jane");
        validInstructorRequest.setLastName("Smith");
        validInstructorRequest.setRole(Role.INSTRUCTOR);

        validLoginRequest = new LoginRequest();
        validLoginRequest.setUsernameOrEmail("john_doe");
        validLoginRequest.setPassword("password123");
    }

    // ==================== REGISTRATION TESTS ====================

    @Test
    @DisplayName("Should successfully register a new student with auto-approval")
    void registerUser_WhenValidStudent_ShouldRegisterAndAutoApprove() {
        // Arrange
        when(userRepository.existsByUsername(validStudentRequest.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(validStudentRequest.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(validStudentRequest.getPassword())).thenReturn("encodedPassword");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("john_doe");
        savedUser.setEmail("john@example.com");
        savedUser.setPassword("encodedPassword");
        savedUser.setFirstName("John");
        savedUser.setLastName("Doe");
        savedUser.setRole(Role.STUDENT);
        savedUser.setIsApproved(true);

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Act
        User result = authService.registerUser(validStudentRequest);

        // Assert
        assertNotNull(result, "Registered user should not be null");
        assertEquals("john_doe", result.getUsername());
        assertEquals("john@example.com", result.getEmail());
        assertEquals(Role.STUDENT, result.getRole());
        assertTrue(result.getIsApproved(), "Student should be auto-approved");

        verify(userRepository, times(1)).existsByUsername("john_doe");
        verify(userRepository, times(1)).existsByEmail("john@example.com");
        verify(passwordEncoder, times(1)).encode("password123");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should register instructor without auto-approval")
    void registerUser_WhenValidInstructor_ShouldRegisterWithoutApproval() {
        // Arrange
        when(userRepository.existsByUsername(validInstructorRequest.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(validInstructorRequest.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(validInstructorRequest.getPassword())).thenReturn("encodedPassword");

        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(2L);
            return user;
        });

        // Act
        User result = authService.registerUser(validInstructorRequest);

        // Assert
        assertNotNull(result);
        assertEquals("jane_smith", result.getUsername());
        assertEquals(Role.INSTRUCTOR, result.getRole());
        assertFalse(result.getIsApproved(), "Instructor should not be auto-approved");

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should handle duplicate username error")
    void registerUser_WhenDuplicateUsername_ShouldHandleError() {
        // Arrange
        when(userRepository.existsByUsername(validStudentRequest.getUsername())).thenReturn(true);

        // Act & Assert
        try {
            authService.registerUser(validStudentRequest);
            fail("Expected RuntimeException to be thrown");
        } catch (RuntimeException ex) {
            assertTrue(ex.getMessage().contains("username") || 
                      ex.getMessage().contains("already exists"),
                      "Exception message should mention username or already exists");
        }

        verify(userRepository, times(1)).existsByUsername("john_doe");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should handle duplicate email error")
    void registerUser_WhenDuplicateEmail_ShouldHandleError() {
        // Arrange
        when(userRepository.existsByUsername(validStudentRequest.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(validStudentRequest.getEmail())).thenReturn(true);

        // Act & Assert
        try {
            authService.registerUser(validStudentRequest);
            fail("Expected RuntimeException to be thrown");
        } catch (RuntimeException ex) {
            assertTrue(ex.getMessage().contains("email") || 
                      ex.getMessage().contains("already exists"),
                      "Exception message should mention email or already exists");
        }

        verify(userRepository, times(1)).existsByEmail("john@example.com");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should handle null signup request")
    void registerUser_WhenNullRequest_ShouldHandleError() {
        // Act & Assert
        try {
            authService.registerUser(null);
            fail("Expected exception to be thrown for null request");
        } catch (Exception ex) {
            assertNotNull(ex, "Exception should be thrown");
        }

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should handle admin registration appropriately")
    void registerUser_WhenAdminRole_ShouldRegister() {
        // Arrange
        SignupRequest adminRequest = new SignupRequest();
        adminRequest.setUsername("admin");
        adminRequest.setEmail("admin@example.com");
        adminRequest.setPassword("adminpass");
        adminRequest.setFirstName("Admin");
        adminRequest.setLastName("User");
        adminRequest.setRole(Role.ADMIN);

        when(userRepository.existsByUsername(adminRequest.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(adminRequest.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(adminRequest.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(3L);
            return user;
        });

        // Act
        User result = authService.registerUser(adminRequest);

        // Assert
        assertNotNull(result);
        assertEquals(Role.ADMIN, result.getRole());
        verify(userRepository, times(1)).save(any(User.class));
    }

    // ==================== LOGIN TESTS ====================

    @Test
    @DisplayName("Should successfully authenticate user with valid credentials")
    void authenticateUser_WhenValidCredentials_ShouldReturnJwtResponse() {
        // Arrange
        UserDetailsServiceImpl.UserPrincipal mockPrincipal = mock(UserDetailsServiceImpl.UserPrincipal.class);
        when(mockPrincipal.getId()).thenReturn(1L);
        when(mockPrincipal.getUsername()).thenReturn("john_doe");

        Authentication mockAuth = mock(Authentication.class);
        when(mockAuth.getPrincipal()).thenReturn(mockPrincipal);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(mockAuth);
        when(tokenProvider.generateToken(mockAuth)).thenReturn("jwt-token-12345");

        // Act
        JwtResponse response = authService.authenticateUser(validLoginRequest);

        // Assert
        assertNotNull(response);
        assertEquals("jwt-token-12345", response.getToken());

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenProvider, times(1)).generateToken(mockAuth);
    }


    @Test
    @DisplayName("Should handle invalid credentials error")
    void authenticateUser_WhenInvalidCredentials_ShouldHandleError() {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenThrow(new BadCredentialsException("Invalid username or password"));

        // Act & Assert
        try {
            authService.authenticateUser(validLoginRequest);
            fail("Expected BadCredentialsException to be thrown");
        } catch (BadCredentialsException ex) {
            assertEquals("Invalid username or password", ex.getMessage());
        }

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenProvider, never()).generateToken(any());
    }

    @Test
    @DisplayName("Should handle authentication with email instead of username")
    void authenticateUser_WhenUsingEmail_ShouldAuthenticate() {
        // Arrange
        LoginRequest emailLoginRequest = new LoginRequest();
        emailLoginRequest.setUsernameOrEmail("john@example.com");
        emailLoginRequest.setPassword("password123");

        // Mock principal returned after authentication
        UserDetailsServiceImpl.UserPrincipal mockPrincipal = mock(UserDetailsServiceImpl.UserPrincipal.class);
        when(mockPrincipal.getId()).thenReturn(1L);
        when(mockPrincipal.getUsername()).thenReturn("john@example.com");

        // Mock authentication object and dependencies
        Authentication mockAuth = mock(Authentication.class);
        when(mockAuth.getPrincipal()).thenReturn(mockPrincipal);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(mockAuth);
        when(tokenProvider.generateToken(mockAuth)).thenReturn("jwt-token-email");

        // Act
        JwtResponse response = authService.authenticateUser(emailLoginRequest);

        // Assert
        assertNotNull(response);
        assertEquals("jwt-token-email", response.getToken());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }


    @Test
    @DisplayName("Should handle null login request")
    void authenticateUser_WhenNullRequest_ShouldHandleError() {
        // Act & Assert
        try {
            authService.authenticateUser(null);
            fail("Expected exception for null login request");
        } catch (Exception ex) {
            assertNotNull(ex);
        }

        verify(authenticationManager, never()).authenticate(any());
    }

    // ==================== GET CURRENT USER TESTS ====================

    @Test
    @DisplayName("Should retrieve current authenticated user")
    void getCurrentUser_WhenAuthenticated_ShouldReturnUser() {
        // This test depends on your implementation of getCurrentUser
        // Usually requires SecurityContext setup
        // Add test based on your actual implementation
    }
}
