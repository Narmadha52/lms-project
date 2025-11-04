package com.lms.service;

import com.lms.model.Role;
import com.lms.model.User;
import com.lms.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("UserDetailsServiceImpl Tests")
class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    private User approvedUser;
    private User unapprovedUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        setupTestData();
    }

    private void setupTestData() {
        approvedUser = new User();
        approvedUser.setId(1L);
        approvedUser.setUsername("narmadha");
        approvedUser.setEmail("narmadha@example.com");
        approvedUser.setPassword("securePassword");
        approvedUser.setFirstName("Narmadha");
        approvedUser.setLastName("G");
        approvedUser.setRole(Role.ADMIN);
        approvedUser.setIsApproved(true);

        unapprovedUser = new User();
        unapprovedUser.setId(2L);
        unapprovedUser.setUsername("pending_instructor");
        unapprovedUser.setEmail("pending@example.com");
        unapprovedUser.setPassword("password");
        unapprovedUser.setFirstName("Pending");
        unapprovedUser.setLastName("Instructor");
        unapprovedUser.setRole(Role.INSTRUCTOR);
        unapprovedUser.setIsApproved(false);
    }

    // ==================== LOAD USER BY USERNAME TESTS ====================

    @Test
    @DisplayName("Should successfully load user by username")
    void loadUserByUsername_WhenValidUsername_ShouldReturnUserDetails() {
        // Arrange
        when(userRepository.findByUsernameOrEmail("narmadha", "narmadha"))
                .thenReturn(Optional.of(approvedUser));

        // Act
        UserDetails result = userDetailsService.loadUserByUsername("narmadha");

        // Assert
        assertNotNull(result, "UserDetails should not be null");
        assertEquals("narmadha", result.getUsername());
        assertEquals("securePassword", result.getPassword());
        assertTrue(result.isEnabled(), "User should be enabled");
        assertTrue(result.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")),
                "User should have ADMIN authority");

        verify(userRepository, times(1)).findByUsernameOrEmail("narmadha", "narmadha");
    }

    @Test
    @DisplayName("Should successfully load user by email")
    void loadUserByUsername_WhenValidEmail_ShouldReturnUserDetails() {
        // Arrange
        when(userRepository.findByUsernameOrEmail("narmadha@example.com", "narmadha@example.com"))
                .thenReturn(Optional.of(approvedUser));

        // Act
        UserDetails result = userDetailsService.loadUserByUsername("narmadha@example.com");

        // Assert
        assertNotNull(result);
        assertEquals("narmadha", result.getUsername());
        assertTrue(result.isEnabled());
        assertEquals("securePassword", result.getPassword());

        verify(userRepository, times(1))
                .findByUsernameOrEmail("narmadha@example.com", "narmadha@example.com");
    }

    @Test
    @DisplayName("Should handle user not found error")
    void loadUserByUsername_WhenUserNotFound_ShouldHandleError() {
        // Arrange
        when(userRepository.findByUsernameOrEmail("unknown", "unknown"))
                .thenReturn(Optional.empty());

        // Act & Assert
        try {
            userDetailsService.loadUserByUsername("unknown");
            fail("Expected UsernameNotFoundException to be thrown");
        } catch (UsernameNotFoundException ex) {
            assertEquals("User not found with username or email: unknown", ex.getMessage());
        }

        verify(userRepository, times(1)).findByUsernameOrEmail("unknown", "unknown");
    }

    @Test
    @DisplayName("Should load unapproved user and mark as disabled")
    void loadUserByUsername_WhenUserNotApproved_ShouldReturnDisabledUserDetails() {
        // Arrange
        when(userRepository.findByUsernameOrEmail("pending_instructor", "pending_instructor"))
                .thenReturn(Optional.of(unapprovedUser));

        // Act
        UserDetails result = userDetailsService.loadUserByUsername("pending_instructor");

        // Assert
        assertNotNull(result);
        assertEquals("pending_instructor", result.getUsername());
        assertFalse(result.isEnabled(), "Unapproved user should be disabled");
        assertTrue(result.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_INSTRUCTOR")));

        verify(userRepository, times(1))
                .findByUsernameOrEmail("pending_instructor", "pending_instructor");
    }

    @Test
    @DisplayName("Should handle empty username")
    void loadUserByUsername_WhenEmptyUsername_ShouldHandleError() {
        // Arrange
        when(userRepository.findByUsernameOrEmail("", ""))
                .thenReturn(Optional.empty());

        // Act & Assert
        try {
            userDetailsService.loadUserByUsername("");
            fail("Expected UsernameNotFoundException to be thrown");
        } catch (UsernameNotFoundException ex) {
            assertTrue(ex.getMessage().contains("User not found"));
        }

        verify(userRepository, times(1)).findByUsernameOrEmail("", "");
    }

    @Test
    @DisplayName("Should handle null username")
    void loadUserByUsername_WhenNullUsername_ShouldHandleError() {
        // Act & Assert
        try {
            userDetailsService.loadUserByUsername(null);
            fail("Expected exception to be thrown for null username");
        } catch (Exception ex) {
            assertNotNull(ex);
        }
    }

    // ==================== USER PRINCIPAL TESTS ====================

    @Test
    @DisplayName("Should create UserPrincipal with correct properties for approved user")
    void userPrincipal_WhenApprovedUser_ShouldHaveCorrectProperties() {
        // Act
        UserDetailsServiceImpl.UserPrincipal principal =
                UserDetailsServiceImpl.UserPrincipal.create(approvedUser);

        // Assert
        assertNotNull(principal);
        assertEquals(approvedUser.getId(), principal.getId());
        assertEquals(approvedUser.getUsername(), principal.getUsername());
        assertEquals(approvedUser.getEmail(), principal.getEmail());
        assertEquals(approvedUser.getPassword(), principal.getPassword());
        assertEquals(approvedUser.getRole(), principal.getRole());
        assertTrue(principal.isEnabled(), "Approved user should be enabled");
        assertNotNull(principal.getAuthorities());
        assertEquals(1, principal.getAuthorities().size());
        assertTrue(principal.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    @DisplayName("Should create UserPrincipal with disabled status for unapproved user")
    void userPrincipal_WhenUnapprovedUser_ShouldBeDisabled() {
        // Act
        UserDetailsServiceImpl.UserPrincipal principal =
                UserDetailsServiceImpl.UserPrincipal.create(unapprovedUser);

        // Assert
        assertNotNull(principal);
        assertEquals(unapprovedUser.getId(), principal.getId());
        assertEquals("pending_instructor", principal.getUsername());
        assertFalse(principal.isEnabled(), "Unapproved user should be disabled");
        assertEquals(Role.INSTRUCTOR, principal.getRole());
    }

    @Test
    @DisplayName("Should verify UserPrincipal account properties")
    void userPrincipal_ShouldHaveCorrectAccountProperties() {
        // Act
        UserDetailsServiceImpl.UserPrincipal principal =
                UserDetailsServiceImpl.UserPrincipal.create(approvedUser);

        // Assert
        assertTrue(principal.isAccountNonExpired(), "Account should not be expired");
        assertTrue(principal.isAccountNonLocked(), "Account should not be locked");
        assertTrue(principal.isCredentialsNonExpired(), "Credentials should not be expired");
    }

    @Test
    @DisplayName("Should create UserPrincipal for STUDENT role")
    void userPrincipal_WhenStudentRole_ShouldHaveStudentAuthority() {
        // Arrange
        User student = new User();
        student.setId(3L);
        student.setUsername("student");
        student.setEmail("student@example.com");
        student.setPassword("password");
        student.setRole(Role.STUDENT);
        student.setIsApproved(true);

        // Act
        UserDetailsServiceImpl.UserPrincipal principal =
                UserDetailsServiceImpl.UserPrincipal.create(student);

        // Assert
        assertNotNull(principal);
        assertTrue(principal.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_STUDENT")));
        assertTrue(principal.isEnabled());
    }

    @Test
    @DisplayName("Should create UserPrincipal for INSTRUCTOR role")
    void userPrincipal_WhenInstructorRole_ShouldHaveInstructorAuthority() {
        // Arrange
        User instructor = new User();
        instructor.setId(4L);
        instructor.setUsername("instructor");
        instructor.setEmail("instructor@example.com");
        instructor.setPassword("password");
        instructor.setRole(Role.INSTRUCTOR);
        instructor.setIsApproved(true);

        // Act
        UserDetailsServiceImpl.UserPrincipal principal =
                UserDetailsServiceImpl.UserPrincipal.create(instructor);

        // Assert
        assertNotNull(principal);
        assertTrue(principal.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_INSTRUCTOR")));
        assertTrue(principal.isEnabled());
    }

    @Test
    @DisplayName("Should handle UserPrincipal equality")
    void userPrincipal_WhenComparingInstances_ShouldHandleEquality() {
        // Act
        UserDetailsServiceImpl.UserPrincipal principal1 =
                UserDetailsServiceImpl.UserPrincipal.create(approvedUser);
        UserDetailsServiceImpl.UserPrincipal principal2 =
                UserDetailsServiceImpl.UserPrincipal.create(approvedUser);

        // Assert
        assertEquals(principal1.getId(), principal2.getId());
        assertEquals(principal1.getUsername(), principal2.getUsername());
        assertEquals(principal1.getEmail(), principal2.getEmail());
    }
}
