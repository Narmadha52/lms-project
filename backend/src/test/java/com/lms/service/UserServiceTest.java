package com.lms.service;

import com.lms.dto.UserDto;
import com.lms.model.Role;
import com.lms.model.User;
import com.lms.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setUsername("narmadha");
        user.setEmail("narmadha@example.com");
        user.setFirstName("Narmadha");
        user.setLastName("G");
        user.setRole(Role.INSTRUCTOR);
        user.setIsApproved(false);
    }

    // ✅ getAllUsers()
    @Test
    void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<UserDto> result = userService.getAllUsers();

        assertEquals(1, result.size());
        assertEquals("narmadha", result.get(0).getUsername());
        verify(userRepository).findAll();
    }

    // ✅ getAllUsers(Pageable)
    @Test
    void testGetAllUsersPaged() {
        Pageable pageable = PageRequest.of(0, 5);
        Page<User> userPage = new PageImpl<>(List.of(user));
        when(userRepository.findAll(pageable)).thenReturn(userPage);

        Page<UserDto> result = userService.getAllUsers(pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("narmadha@example.com", result.getContent().get(0).getEmail());
    }

    // ✅ getUsersByRole()
    @Test
    void testGetUsersByRole() {
        when(userRepository.findByRole(Role.INSTRUCTOR)).thenReturn(List.of(user));

        List<UserDto> result = userService.getUsersByRole(Role.INSTRUCTOR);

        assertEquals(1, result.size());
        assertEquals(Role.INSTRUCTOR, result.get(0).getRole());
    }

    // ✅ getPendingInstructors()
    @Test
    void testGetPendingInstructors() {
        when(userRepository.findPendingInstructors(Role.INSTRUCTOR)).thenReturn(List.of(user));

        List<UserDto> result = userService.getPendingInstructors();

        assertEquals(1, result.size());
        assertFalse(result.get(0).getIsApproved());
    }

    // ✅ getUserById()
    @Test
    void testGetUserById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserDto dto = userService.getUserById(1L);

        assertEquals("narmadha", dto.getUsername());
        verify(userRepository).findById(1L);
    }

    // ❌ getUserById throws exception
    @Test
    void testGetUserById_NotFound() {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.getUserById(2L));
        assertEquals("User not found with id: 2", ex.getMessage());
    }

    // ✅ getUserByUsername()
    @Test
    void testGetUserByUsername() {
        when(userRepository.findByUsername("narmadha")).thenReturn(Optional.of(user));

        UserDto dto = userService.getUserByUsername("narmadha");

        assertEquals("narmadha@example.com", dto.getEmail());
    }

    // ✅ getUserByEmail()
    @Test
    void testGetUserByEmail() {
        when(userRepository.findByEmail("narmadha@example.com")).thenReturn(Optional.of(user));

        UserDto dto = userService.getUserByEmail("narmadha@example.com");

        assertEquals("narmadha", dto.getUsername());
    }

    // ✅ updateUser()
    @Test
    void testUpdateUser() {
        UserDto dto = new UserDto(1L, "narmadha", "newmail@example.com", "Narmadha", "G", Role.ADMIN, true, null, null);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserDto updated = userService.updateUser(1L, dto);

        assertEquals("newmail@example.com", updated.getEmail());
        assertEquals(Role.ADMIN, updated.getRole());
        verify(userRepository).save(any(User.class));
    }

    // ✅ approveUser()
    @Test
    void testApproveUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserDto result = userService.approveUser(1L);

        assertTrue(result.getIsApproved());
        verify(userRepository).save(any(User.class));
    }

    // ✅ rejectUser()
    @Test
    void testRejectUser() {
        user.setIsApproved(true);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserDto result = userService.rejectUser(1L);

        assertFalse(result.getIsApproved());
    }

    // ✅ deleteUser()
    @Test
    void testDeleteUser() {
        when(userRepository.existsById(1L)).thenReturn(true);

        userService.deleteUser(1L);

        verify(userRepository).deleteById(1L);
    }

    // ❌ deleteUser throws when not found
    @Test
    void testDeleteUser_NotFound() {
        when(userRepository.existsById(1L)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.deleteUser(1L));
        assertEquals("User not found with id: 1", ex.getMessage());
    }

    // ✅ count methods
    @Test
    void testCountMethods() {
        when(userRepository.count()).thenReturn(10L);
        when(userRepository.countByRole(Role.STUDENT)).thenReturn(5L);
        when(userRepository.countByRoleAndIsApproved(Role.INSTRUCTOR, true)).thenReturn(3L);
        when(userRepository.countByRoleAndIsApproved(Role.INSTRUCTOR, false)).thenReturn(2L);

        assertEquals(10, userService.getUserCount());
        assertEquals(5, userService.getUserCountByRole(Role.STUDENT));
        assertEquals(3, userService.getApprovedUserCountByRole(Role.INSTRUCTOR));
        assertEquals(2, userService.getPendingUserCountByRole(Role.INSTRUCTOR));
    }

    // ✅ exists checks
    @Test
    void testExistsByUsernameAndEmail() {
        when(userRepository.existsByUsername("narmadha")).thenReturn(true);
        when(userRepository.existsByEmail("narmadha@example.com")).thenReturn(false);

        assertTrue(userService.existsByUsername("narmadha"));
        assertFalse(userService.existsByEmail("narmadha@example.com"));
    }

    // ✅ DTO conversion
    @Test
    void testConvertToDto() {
        UserDto dto = userService.convertToDto(user);

        assertEquals(user.getUsername(), dto.getUsername());
        assertEquals(user.getEmail(), dto.getEmail());
        assertEquals(user.getRole(), dto.getRole());
    }
}
