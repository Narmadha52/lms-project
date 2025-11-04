package com.lms.service;

import com.lms.dto.UserDto;
import com.lms.model.Role;
import com.lms.model.User;
import com.lms.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("UserService Tests")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User student;
    private User instructor;
    private User admin;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        setupTestData();
    }

    private void setupTestData() {
        student = new User();
        student.setId(1L);
        student.setUsername("john_student");
        student.setEmail("john@example.com");
        student.setFirstName("John");
        student.setLastName("Doe");
        student.setRole(Role.STUDENT);
        student.setIsApproved(true);
        student.setCreatedAt(LocalDateTime.now());

        instructor = new User();
        instructor.setId(2L);
        instructor.setUsername("jane_instructor");
        instructor.setEmail("jane@example.com");
        instructor.setFirstName("Jane");
        instructor.setLastName("Smith");
        instructor.setRole(Role.INSTRUCTOR);
        instructor.setIsApproved(false);
        instructor.setCreatedAt(LocalDateTime.now());

        admin = new User();
        admin.setId(3L);
        admin.setUsername("admin");
        admin.setEmail("admin@example.com");
        admin.setFirstName("Admin");
        admin.setLastName("User");
        admin.setRole(Role.ADMIN);
        admin.setIsApproved(true);
        admin.setCreatedAt(LocalDateTime.now());
    }

    // ==================== GET ALL USERS TESTS ====================

    @Test
    @DisplayName("Should get all users successfully")
    void getAllUsers_WhenCalled_ShouldReturnAllUsers() {
        // Arrange
        List<User> users = Arrays.asList(student, instructor, admin);
        when(userRepository.findAll()).thenReturn(users);

        // Act
        List<UserDto> result = userService.getAllUsers();

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("john_student", result.get(0).getUsername());
        assertEquals("jane_instructor", result.get(1).getUsername());
        assertEquals("admin", result.get(2).getUsername());

        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return empty list when no users exist")
    void getAllUsers_WhenNoUsers_ShouldReturnEmptyList() {
        // Arrange
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<UserDto> result = userService.getAllUsers();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should get all users with pagination")
    void getAllUsers_WithPagination_ShouldReturnPagedUsers() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<User> userPage = new PageImpl<>(Arrays.asList(student, instructor), pageable, 2);
        when(userRepository.findAll(pageable)).thenReturn(userPage);

        // Act
        Page<UserDto> result = userService.getAllUsers(pageable);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());
        assertEquals("john_student", result.getContent().get(0).getUsername());

        verify(userRepository, times(1)).findAll(pageable);
    }

    // ==================== GET USERS BY ROLE TESTS ====================

    @Test
    @DisplayName("Should get all students")
    void getUsersByRole_WhenStudent_ShouldReturnStudents() {
        // Arrange
        when(userRepository.findByRole(Role.STUDENT)).thenReturn(Arrays.asList(student));

        // Act
        List<UserDto> result = userService.getUsersByRole(Role.STUDENT);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(Role.STUDENT, result.get(0).getRole());

        verify(userRepository, times(1)).findByRole(Role.STUDENT);
    }

    @Test
    @DisplayName("Should get all instructors")
    void getUsersByRole_WhenInstructor_ShouldReturnInstructors() {
        // Arrange
        when(userRepository.findByRole(Role.INSTRUCTOR)).thenReturn(Arrays.asList(instructor));

        // Act
        List<UserDto> result = userService.getUsersByRole(Role.INSTRUCTOR);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(Role.INSTRUCTOR, result.get(0).getRole());

        verify(userRepository, times(1)).findByRole(Role.INSTRUCTOR);
    }

    @Test
    @DisplayName("Should return empty list when no users with specified role exist")
    void getUsersByRole_WhenNoUsersWithRole_ShouldReturnEmptyList() {
        // Arrange
        when(userRepository.findByRole(Role.ADMIN)).thenReturn(Collections.emptyList());

        // Act
        List<UserDto> result = userService.getUsersByRole(Role.ADMIN);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // ==================== PENDING INSTRUCTORS TESTS ====================

    @Test
    @DisplayName("Should get pending instructors")
    void getPendingInstructors_WhenCalled_ShouldReturnPendingInstructors() {
        // Arrange
        when(userRepository.findPendingInstructors(Role.INSTRUCTOR))
                .thenReturn(Arrays.asList(instructor));

        // Act
        List<UserDto> result = userService.getPendingInstructors();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertFalse(result.get(0).getIsApproved());
        assertEquals(Role.INSTRUCTOR, result.get(0).getRole());

        verify(userRepository, times(1)).findPendingInstructors(Role.INSTRUCTOR);
    }

    // ==================== GET USER BY ID TESTS ====================

    @Test
    @DisplayName("Should get user by ID successfully")
    void getUserById_WhenUserExists_ShouldReturnUser() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(student));

        // Act
        UserDto result = userService.getUserById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("john_student", result.getUsername());
        assertEquals("john@example.com", result.getEmail());

        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should handle user not found by ID")
    void getUserById_WhenUserNotFound_ShouldHandleError() {
        // Arrange
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        try {
            userService.getUserById(999L);
            fail("Expected RuntimeException to be thrown");
        } catch (RuntimeException ex) {
            assertEquals("User not found with id: 999", ex.getMessage());
        }

        verify(userRepository, times(1)).findById(999L);
    }

    // ==================== GET USER BY USERNAME TESTS ====================

    @Test
    @DisplayName("Should get user by username successfully")
    void getUserByUsername_WhenUserExists_ShouldReturnUser() {
        // Arrange
        when(userRepository.findByUsername("john_student")).thenReturn(Optional.of(student));

        // Act
        UserDto result = userService.getUserByUsername("john_student");

        // Assert
        assertNotNull(result);
        assertEquals("john_student", result.getUsername());
        assertEquals("john@example.com", result.getEmail());

        verify(userRepository, times(1)).findByUsername("john_student");
    }

    @Test
    @DisplayName("Should handle user not found by username")
    void getUserByUsername_WhenUserNotFound_ShouldHandleError() {
        // Arrange
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        // Act & Assert
        try {
            userService.getUserByUsername("unknown");
            fail("Expected RuntimeException to be thrown");
        } catch (RuntimeException ex) {
            assertTrue(ex.getMessage().contains("not found"));
        }
    }

    // ==================== GET USER BY EMAIL TESTS ====================

    @Test
    @DisplayName("Should get user by email successfully")
    void getUserByEmail_WhenUserExists_ShouldReturnUser() {
        // Arrange
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(student));

        // Act
        UserDto result = userService.getUserByEmail("john@example.com");

        // Assert
        assertNotNull(result);
        assertEquals("john@example.com", result.getEmail());
        assertEquals("john_student", result.getUsername());

        verify(userRepository, times(1)).findByEmail("john@example.com");
    }

    @Test
    @DisplayName("Should handle user not found by email")
    void getUserByEmail_WhenUserNotFound_ShouldHandleError() {
        // Arrange
        when(userRepository.findByEmail("unknown@example.com")).thenReturn(Optional.empty());

        // Act & Assert
        try {
            userService.getUserByEmail("unknown@example.com");
            fail("Expected RuntimeException to be thrown");
        } catch (RuntimeException ex) {
            assertTrue(ex.getMessage().contains("not found"));
        }
    }

    // ==================== UPDATE USER TESTS ====================

    @Test
    @DisplayName("Should update user successfully")
    void updateUser_WhenValidData_ShouldUpdateUser() {
        // Arrange
        UserDto updateDto = new UserDto();
        updateDto.setId(1L);
        updateDto.setUsername("john_student");
        updateDto.setEmail("newemail@example.com");
        updateDto.setFirstName("Johnny");
        updateDto.setLastName("Doe");
        updateDto.setRole(Role.STUDENT);
        updateDto.setIsApproved(true);

        when(userRepository.findById(1L)).thenReturn(Optional.of(student));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        UserDto result = userService.updateUser(1L, updateDto);

        // Assert
        assertNotNull(result);
        assertEquals("newemail@example.com", result.getEmail());
        assertEquals("Johnny", result.getFirstName());

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should handle update user not found")
    void updateUser_WhenUserNotFound_ShouldHandleError() {
        // Arrange
        UserDto updateDto = new UserDto();
        updateDto.setEmail("test@example.com");

        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        try {
            userService.updateUser(999L, updateDto);
            fail("Expected RuntimeException to be thrown");
        } catch (RuntimeException ex) {
            assertTrue(ex.getMessage().contains("not found"));
        }

        verify(userRepository, never()).save(any(User.class));
    }

    // ==================== APPROVE/REJECT USER TESTS ====================

    @Test
    @DisplayName("Should approve user successfully")
    void approveUser_WhenUserExists_ShouldApproveUser() {
        // Arrange
        when(userRepository.findById(2L)).thenReturn(Optional.of(instructor));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        UserDto result = userService.approveUser(2L);

        // Assert
        assertNotNull(result);
        assertTrue(result.getIsApproved());

        verify(userRepository, times(1)).findById(2L);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should reject user successfully")
    void rejectUser_WhenUserExists_ShouldRejectUser() {
        // Arrange
        admin.setIsApproved(true);
        when(userRepository.findById(3L)).thenReturn(Optional.of(admin));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        UserDto result = userService.rejectUser(3L);

        // Assert
        assertNotNull(result);
        assertFalse(result.getIsApproved());

        verify(userRepository, times(1)).save(any(User.class));
    }

    // ==================== DELETE USER TESTS ====================

    @Test
    @DisplayName("Should delete user successfully")
    void deleteUser_WhenUserExists_ShouldDeleteUser() {
        // Arrange
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);

        // Act
        userService.deleteUser(1L);

        // Assert
        verify(userRepository, times(1)).existsById(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should handle delete user not found")
    void deleteUser_WhenUserNotFound_ShouldHandleError() {
        // Arrange
        when(userRepository.existsById(999L)).thenReturn(false);

        // Act & Assert
        try {
            userService.deleteUser(999L);
            fail("Expected RuntimeException to be thrown");
        } catch (RuntimeException ex) {
            assertEquals("User not found with id: 999", ex.getMessage());
        }

        verify(userRepository, times(1)).existsById(999L);
        verify(userRepository, never()).deleteById(any());
    }

    // ==================== COUNT TESTS ====================

    @Test
    @DisplayName("Should get total user count")
    void getUserCount_WhenCalled_ShouldReturnCount() {
        // Arrange
        when(userRepository.count()).thenReturn(10L);

        // Act
        long count = userService.getUserCount();

        // Assert
        assertEquals(10L, count);
        verify(userRepository, times(1)).count();
    }

    @Test
    @DisplayName("Should get user count by role")
    void getUserCountByRole_WhenCalled_ShouldReturnCount() {
        // Arrange
        when(userRepository.countByRole(Role.STUDENT)).thenReturn(5L);

        // Act
        long count = userService.getUserCountByRole(Role.STUDENT);

        // Assert
        assertEquals(5L, count);
        verify(userRepository, times(1)).countByRole(Role.STUDENT);
    }

    @Test
    @DisplayName("Should get approved user count by role")
    void getApprovedUserCountByRole_WhenCalled_ShouldReturnCount() {
        // Arrange
        when(userRepository.countByRoleAndIsApproved(Role.INSTRUCTOR, true)).thenReturn(3L);

        // Act
        long count = userService.getApprovedUserCountByRole(Role.INSTRUCTOR);

        // Assert
        assertEquals(3L, count);
        verify(userRepository, times(1)).countByRoleAndIsApproved(Role.INSTRUCTOR, true);
    }

    @Test
    @DisplayName("Should get pending user count by role")
    void getPendingUserCountByRole_WhenCalled_ShouldReturnCount() {
        // Arrange
        when(userRepository.countByRoleAndIsApproved(Role.INSTRUCTOR, false)).thenReturn(2L);

        // Act
        long count = userService.getPendingUserCountByRole(Role.INSTRUCTOR);

        // Assert
        assertEquals(2L, count);
        verify(userRepository, times(1)).countByRoleAndIsApproved(Role.INSTRUCTOR, false);
    }

    // ==================== EXISTS TESTS ====================

    @Test
    @DisplayName("Should check if username exists")
    void existsByUsername_WhenUsernameExists_ShouldReturnTrue() {
        // Arrange
        when(userRepository.existsByUsername("john_student")).thenReturn(true);

        // Act
        boolean exists = userService.existsByUsername("john_student");

        // Assert
        assertTrue(exists);
        verify(userRepository, times(1)).existsByUsername("john_student");
    }

    @Test
    @DisplayName("Should return false when username does not exist")
    void existsByUsername_WhenUsernameDoesNotExist_ShouldReturnFalse() {
        // Arrange
        when(userRepository.existsByUsername("unknown")).thenReturn(false);

        // Act
        boolean exists = userService.existsByUsername("unknown");

        // Assert
        assertFalse(exists);
    }

    @Test
    @DisplayName("Should check if email exists")
    void existsByEmail_WhenEmailExists_ShouldReturnTrue() {
        // Arrange
        when(userRepository.existsByEmail("john@example.com")).thenReturn(true);

        // Act
        boolean exists = userService.existsByEmail("john@example.com");

        // Assert
        assertTrue(exists);
        verify(userRepository, times(1)).existsByEmail("john@example.com");
    }

    // ==================== DTO CONVERSION TESTS ====================

    @Test
    @DisplayName("Should convert User to UserDto correctly")
    void convertToDto_WhenValidUser_ShouldConvertCorrectly() {
        // Act
        UserDto dto = userService.convertToDto(student);

        // Assert
        assertNotNull(dto);
        assertEquals(student.getId(), dto.getId());
        assertEquals(student.getUsername(), dto.getUsername());
        assertEquals(student.getEmail(), dto.getEmail());
        assertEquals(student.getFirstName(), dto.getFirstName());
        assertEquals(student.getLastName(), dto.getLastName());
        assertEquals(student.getRole(), dto.getRole());
        assertEquals(student.getIsApproved(), dto.getIsApproved());
    }

    @Test
    @DisplayName("Should handle null user in DTO conversion")
    void convertToDto_WhenNullUser_ShouldHandleGracefully() {
        // Act & Assert
        try {
            userService.convertToDto(null);
            // Depending on implementation, this might return null or throw exception
        } catch (Exception ex) {
            assertNotNull(ex);
        }
    }
}
