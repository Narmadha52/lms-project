package com.lms.controller;

import com.lms.dto.*;
import com.lms.service.EnrollmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EnrollmentControllerTest {

    @InjectMocks
    private EnrollmentController enrollmentController;

    @Mock
    private EnrollmentService enrollmentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ========== enrollInCourse Tests ==========
    
    @Test
    @DisplayName("Should successfully enroll in course")
    void testEnrollInCourse_Success() {
        // Arrange
        Long courseId = 1L;
        EnrollmentDto enrollmentDto = createMockEnrollmentDto(courseId);
        when(enrollmentService.enrollInCourse(courseId)).thenReturn(enrollmentDto);

        // Act
        ResponseEntity<ApiResponse<EnrollmentDto>> response = enrollmentController.enrollInCourse(courseId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Successfully enrolled in course", response.getBody().getMessage());
        assertEquals(enrollmentDto, response.getBody().getData());
        
        // Verify service method was called
        verify(enrollmentService, times(1)).enrollInCourse(courseId);
    }

    @Test
    @DisplayName("Should return error when enrollment fails")
    void testEnrollInCourse_Failure() {
        // Arrange
        Long courseId = 1L;
        String errorMessage = "Course not found";
        when(enrollmentService.enrollInCourse(courseId))
                .thenThrow(new RuntimeException(errorMessage));

        // Act
        ResponseEntity<ApiResponse<EnrollmentDto>> response = enrollmentController.enrollInCourse(courseId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertEquals("Error: " + errorMessage, response.getBody().getMessage());
        assertNull(response.getBody().getData());
        
        verify(enrollmentService, times(1)).enrollInCourse(courseId);
    }

    @Test
    @DisplayName("Should handle already enrolled exception")
    void testEnrollInCourse_AlreadyEnrolled() {
        // Arrange
        Long courseId = 1L;
        when(enrollmentService.enrollInCourse(courseId))
                .thenThrow(new RuntimeException("Already enrolled in this course"));

        // Act
        ResponseEntity<ApiResponse<EnrollmentDto>> response = enrollmentController.enrollInCourse(courseId);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().getMessage().contains("Already enrolled"));
        verify(enrollmentService, times(1)).enrollInCourse(courseId);
    }

    // ========== unenrollFromCourse Tests ==========
    
    @Test
    @DisplayName("Should successfully unenroll from course")
    void testUnenrollFromCourse_Success() {
        // Arrange
        Long courseId = 1L;
        doNothing().when(enrollmentService).unenrollFromCourse(courseId);

        // Act
        ResponseEntity<ApiResponse<Void>> response = enrollmentController.unenrollFromCourse(courseId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Successfully unenrolled from course", response.getBody().getMessage());
        assertNull(response.getBody().getData());
        
        verify(enrollmentService, times(1)).unenrollFromCourse(courseId);
    }

    @Test
    @DisplayName("Should return error when unenrollment fails")
    void testUnenrollFromCourse_Failure() {
        // Arrange
        Long courseId = 1L;
        String errorMessage = "Enrollment not found";
        doThrow(new RuntimeException(errorMessage))
                .when(enrollmentService).unenrollFromCourse(courseId);

        // Act
        ResponseEntity<ApiResponse<Void>> response = enrollmentController.unenrollFromCourse(courseId);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertFalse(response.getBody().isSuccess());
        assertEquals("Error: " + errorMessage, response.getBody().getMessage());
        
        verify(enrollmentService, times(1)).unenrollFromCourse(courseId);
    }

    // ========== getMyEnrollments Tests ==========
    
    @Test
    @DisplayName("Should retrieve student enrollments successfully")
    void testGetMyEnrollments_Success() {
        // Arrange
        List<EnrollmentDto> enrollments = Arrays.asList(
                createMockEnrollmentDto(1L),
                createMockEnrollmentDto(2L)
        );
        when(enrollmentService.getStudentEnrollments()).thenReturn(enrollments);

        // Act
        ResponseEntity<ApiResponse<List<EnrollmentDto>>> response = 
                enrollmentController.getMyEnrollments();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Enrollments retrieved successfully", response.getBody().getMessage());
        assertEquals(2, response.getBody().getData().size());
        assertEquals(enrollments, response.getBody().getData());
        
        verify(enrollmentService, times(1)).getStudentEnrollments();
    }

    @Test
    @DisplayName("Should return empty list when no enrollments")
    void testGetMyEnrollments_EmptyList() {
        // Arrange
        when(enrollmentService.getStudentEnrollments()).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<ApiResponse<List<EnrollmentDto>>> response = 
                enrollmentController.getMyEnrollments();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
        assertTrue(response.getBody().getData().isEmpty());
        
        verify(enrollmentService, times(1)).getStudentEnrollments();
    }

    @Test
    @DisplayName("Should handle error when retrieving enrollments")
    void testGetMyEnrollments_Failure() {
        // Arrange
        when(enrollmentService.getStudentEnrollments())
                .thenThrow(new RuntimeException("Database error"));

        // Act
        ResponseEntity<ApiResponse<List<EnrollmentDto>>> response = 
                enrollmentController.getMyEnrollments();

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertFalse(response.getBody().isSuccess());
        assertTrue(response.getBody().getMessage().contains("Database error"));
        
        verify(enrollmentService, times(1)).getStudentEnrollments();
    }

    // ========== getCourseEnrollments Tests ==========
    
    @Test
    @DisplayName("Should retrieve course enrollments successfully")
    void testGetCourseEnrollments_Success() {
        // Arrange
        Long courseId = 1L;
        List<EnrollmentDto> enrollments = Arrays.asList(
                createMockEnrollmentDto(courseId),
                createMockEnrollmentDto(courseId)
        );
        when(enrollmentService.getCourseEnrollments(courseId)).thenReturn(enrollments);

        // Act
        ResponseEntity<ApiResponse<List<EnrollmentDto>>> response = 
                enrollmentController.getCourseEnrollments(courseId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Course enrollments retrieved successfully", response.getBody().getMessage());
        assertEquals(2, response.getBody().getData().size());
        
        verify(enrollmentService, times(1)).getCourseEnrollments(courseId);
    }

    @Test
    @DisplayName("Should handle error when course not found")
    void testGetCourseEnrollments_CourseNotFound() {
        // Arrange
        Long courseId = 999L;
        when(enrollmentService.getCourseEnrollments(courseId))
                .thenThrow(new RuntimeException("Course not found"));

        // Act
        ResponseEntity<ApiResponse<List<EnrollmentDto>>> response = 
                enrollmentController.getCourseEnrollments(courseId);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertFalse(response.getBody().isSuccess());
        
        verify(enrollmentService, times(1)).getCourseEnrollments(courseId);
    }

    // ========== isEnrolled Tests ==========
    
    @Test
    @DisplayName("Should return true when user is enrolled")
    void testIsEnrolled_True() {
        // Arrange
        Long courseId = 1L;
        when(enrollmentService.isEnrolled(courseId)).thenReturn(true);

        // Act
        ResponseEntity<ApiResponse<Boolean>> response = enrollmentController.isEnrolled(courseId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Enrollment status retrieved successfully", response.getBody().getMessage());
        assertTrue(response.getBody().getData());
        
        verify(enrollmentService, times(1)).isEnrolled(courseId);
    }

    @Test
    @DisplayName("Should return false when user is not enrolled")
    void testIsEnrolled_False() {
        // Arrange
        Long courseId = 1L;
        when(enrollmentService.isEnrolled(courseId)).thenReturn(false);

        // Act
        ResponseEntity<ApiResponse<Boolean>> response = enrollmentController.isEnrolled(courseId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
        assertFalse(response.getBody().getData());
        
        verify(enrollmentService, times(1)).isEnrolled(courseId);
    }

    @Test
    @DisplayName("Should handle error when checking enrollment status")
    void testIsEnrolled_Error() {
        // Arrange
        Long courseId = 1L;
        when(enrollmentService.isEnrolled(courseId))
                .thenThrow(new RuntimeException("Service error"));

        // Act
        ResponseEntity<ApiResponse<Boolean>> response = enrollmentController.isEnrolled(courseId);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertFalse(response.getBody().isSuccess());
        
        verify(enrollmentService, times(1)).isEnrolled(courseId);
    }

    // ========== getEnrollment Tests ==========
    
    @Test
    @DisplayName("Should retrieve specific enrollment successfully")
    void testGetEnrollment_Success() {
        // Arrange
        Long courseId = 1L;
        EnrollmentDto enrollmentDto = createMockEnrollmentDto(courseId);
        when(enrollmentService.getEnrollment(courseId)).thenReturn(enrollmentDto);

        // Act
        ResponseEntity<ApiResponse<EnrollmentDto>> response = 
                enrollmentController.getEnrollment(courseId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Enrollment retrieved successfully", response.getBody().getMessage());
        assertNotNull(response.getBody().getData());
        assertEquals(enrollmentDto, response.getBody().getData());
        
        verify(enrollmentService, times(1)).getEnrollment(courseId);
    }

    @Test
    @DisplayName("Should handle error when enrollment not found")
    void testGetEnrollment_NotFound() {
        // Arrange
        Long courseId = 1L;
        when(enrollmentService.getEnrollment(courseId))
                .thenThrow(new RuntimeException("Enrollment not found"));

        // Act
        ResponseEntity<ApiResponse<EnrollmentDto>> response = 
                enrollmentController.getEnrollment(courseId);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertFalse(response.getBody().isSuccess());
        assertTrue(response.getBody().getMessage().contains("Enrollment not found"));
        
        verify(enrollmentService, times(1)).getEnrollment(courseId);
    }

    // ========== Helper Methods ==========
    
    private EnrollmentDto createMockEnrollmentDto(Long courseId) {
        EnrollmentDto dto = new EnrollmentDto();
        return dto;
    }
}
