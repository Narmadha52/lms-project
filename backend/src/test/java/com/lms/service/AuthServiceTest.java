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
import org.mockito.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ✅ Test registration for STUDENT (auto-approved)
    @Test
    void registerUser_shouldRegisterStudentSuccessfully() {
        SignupRequest request = new SignupRequest();
        request.setUsername("john");
        request.setEmail("john@example.com");
        request.setPassword("123456");
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setRole(Role.STUDENT);

        when(userRepository.existsByUsername("john")).thenReturn(false);
        when(userRepository.existsByEmail("john@example.com")).thenReturn(false);
        when(passwordEncoder.encode("123456")).thenReturn("encodedPass");

        User savedUser = new User("john", "john@example.com", "encodedPass",
                "John", "Doe", Role.STUDENT);
        savedUser.setIsApproved(true);

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User result = authService.registerUser(request);

        assertEquals("john", result.getUsername());
        assertTrue(result.getIsApproved());
        verify(userRepository, times(1)).save(any(User.class));
    }

    // ✅ Test registration for INSTRUCTOR (needs approval)
    @Test
    void registerUser_shouldMarkInstructorNotApproved() {
        SignupRequest request = new SignupRequest();
        request.setUsername("teach");
        request.setEmail("teach@example.com");
        request.setPassword("pass");
        request.setFirstName("T");
        request.setLastName("E");
        request.setRole(Role.INSTRUCTOR);

        when(userRepository.existsByUsername("teach")).thenReturn(false);
        when(userRepository.existsByEmail("teach@example.com")).thenReturn(false);
        when(passwordEncoder.encode("pass")).thenReturn("encoded");

        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        User result = authService.registerUser(request);

        assertFalse(result.getIsApproved());
    }

    // ✅ Test duplicate username
    @Test
    void registerUser_shouldThrowWhenUsernameExists() {
        SignupRequest req = new SignupRequest();
        req.setUsername("john");
        req.setEmail("john@example.com");

        when(userRepository.existsByUsername("john")).thenReturn(true);

        assertThrows(RuntimeException.class, () -> authService.registerUser(req));
    }

    // ✅ Test duplicate email
    @Test
    void registerUser_shouldThrowWhenEmailExists() {
        SignupRequest req = new SignupRequest();
        req.setUsername("john");
        req.setEmail("john@example.com");

        when(userRepository.existsByUsername("john")).thenReturn(false);
        when(userRepository.existsByEmail("john@example.com")).thenReturn(true);

        assertThrows(RuntimeException.class, () -> authService.registerUser(req));
    }
}
