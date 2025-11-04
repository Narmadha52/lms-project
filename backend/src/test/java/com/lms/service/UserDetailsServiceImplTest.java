package com.lms.service;

import com.lms.model.Role;
import com.lms.model.User;
import com.lms.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setUsername("narmadha");
        user.setEmail("narmadha@example.com");
        user.setPassword("securePass");
        user.setFirstName("Narmadha");
        user.setLastName("G");
        user.setRole(Role.ADMIN);
        user.setIsApproved(true);
    }

    // ✅ Should load user by username successfully
    @Test
    void testLoadUserByUsername_Success_UsingUsername() {
        when(userRepository.findByUsernameOrEmail("narmadha", "narmadha"))
                .thenReturn(Optional.of(user));

        UserDetails result = userDetailsService.loadUserByUsername("narmadha");

        assertNotNull(result);
        assertEquals("narmadha", result.getUsername());
        assertEquals("securePass", result.getPassword());
        assertTrue(result.isEnabled());
        assertTrue(result.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));

        verify(userRepository, times(1))
                .findByUsernameOrEmail("narmadha", "narmadha");
    }

    // ✅ Should load user by email successfully
    @Test
    void testLoadUserByUsername_Success_UsingEmail() {
        when(userRepository.findByUsernameOrEmail("narmadha@example.com", "narmadha@example.com"))
                .thenReturn(Optional.of(user));

        UserDetails result = userDetailsService.loadUserByUsername("narmadha@example.com");

        assertNotNull(result);
        assertEquals("narmadha", result.getUsername());
        assertTrue(result.isEnabled());
    }

    // ❌ Should throw exception when user not found
    @Test
    void testLoadUserByUsername_UserNotFound() {
        when(userRepository.findByUsernameOrEmail("unknown", "unknown"))
                .thenReturn(Optional.empty());

        UsernameNotFoundException ex = assertThrows(
                UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("unknown")
        );

        assertEquals("User not found with username or email: unknown", ex.getMessage());
        verify(userRepository).findByUsernameOrEmail("unknown", "unknown");
    }

    // ✅ UserPrincipal properties test
    @Test
    void testUserPrincipalProperties() {
        UserDetailsServiceImpl.UserPrincipal principal =
                UserDetailsServiceImpl.UserPrincipal.create(user);

        assertEquals(user.getId(), principal.getId());
        assertEquals(user.getUsername(), principal.getUsername());
        assertEquals(user.getEmail(), principal.getEmail());
        assertEquals(user.getPassword(), principal.getPassword());
        assertEquals(user.getRole(), principal.getRole());
        assertTrue(principal.isEnabled());
        assertTrue(principal.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
    }

    // ✅ Should handle disabled (unapproved) user
    @Test
    void testUserPrincipalDisabledUser() {
        user.setIsApproved(false);
        UserDetailsServiceImpl.UserPrincipal principal =
                UserDetailsServiceImpl.UserPrincipal.create(user);

        assertFalse(principal.isEnabled());
    }
}
