package com.lms.controller;

import com.lms.dto.*;
import com.lms.service.EnrollmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EnrollmentControllerTest {

    @InjectMocks
    private EnrollmentController enrollmentController;

    @Mock
    private EnrollmentService enrollmentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testEnrollInCourse_Success() {
        EnrollmentDto dto = new EnrollmentDto();
        when(enrollmentService.enrollInCourse(1L)).thenReturn(dto);

        ResponseEntity<ApiResponse<EnrollmentDto>> response = enrollmentController.enrollInCourse(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(dto, response.getBody().getData());
    }

    @Test
    void testUnenrollFromCourse() {
        doNothing().when(enrollmentService).unenrollFromCourse(1L);

        ResponseEntity<ApiResponse<Void>> response = enrollmentController.unenrollFromCourse(1L);

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetMyEnrollments() {
        List<EnrollmentDto> list = List.of(new EnrollmentDto());
        when(enrollmentService.getStudentEnrollments()).thenReturn(list);

        ResponseEntity<ApiResponse<List<EnrollmentDto>>> response = enrollmentController.getMyEnrollments();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(list, response.getBody().getData());
    }

    @Test
    void testGetCourseEnrollments() {
        List<EnrollmentDto> list = List.of(new EnrollmentDto());
        when(enrollmentService.getCourseEnrollments(1L)).thenReturn(list);

        ResponseEntity<ApiResponse<List<EnrollmentDto>>> response = enrollmentController.getCourseEnrollments(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(list, response.getBody().getData());
    }

    @Test
    void testIsEnrolled() {
        when(enrollmentService.isEnrolled(1L)).thenReturn(true);

        ResponseEntity<ApiResponse<Boolean>> response = enrollmentController.isEnrolled(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().getData());
    }

    @Test
    void testGetEnrollment() {
        EnrollmentDto dto = new EnrollmentDto();
        when(enrollmentService.getEnrollment(1L)).thenReturn(dto);

        ResponseEntity<ApiResponse<EnrollmentDto>> response = enrollmentController.getEnrollment(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(dto, response.getBody().getData());
    }
}
