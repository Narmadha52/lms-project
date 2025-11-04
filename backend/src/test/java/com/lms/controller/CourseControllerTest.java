package com.lms.controller;

import com.lms.dto.*;
import com.lms.model.DifficultyLevel;
import com.lms.service.CourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CourseControllerTest {

    @InjectMocks
    private CourseController courseController;

    @Mock
    private CourseService courseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetPublishedCourses_Success() {
        List<CourseDto> list = List.of(new CourseDto());
        when(courseService.getPublishedCourses()).thenReturn(list);

        ResponseEntity<ApiResponse<List<CourseDto>>> response = courseController.getPublishedCourses();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(list, response.getBody().getData());
    }

    @Test
    void testSearchCourses_Success() {
        List<CourseDto> list = List.of(new CourseDto());
        when(courseService.searchCourses("java")).thenReturn(list);

        ResponseEntity<ApiResponse<List<CourseDto>>> response = courseController.searchCourses("java");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(list, response.getBody().getData());
    }

    @Test
    void testGetCoursesByCategory() {
        List<CourseDto> list = List.of(new CourseDto());
        when(courseService.getCoursesByCategory("Programming")).thenReturn(list);

        ResponseEntity<ApiResponse<List<CourseDto>>> response = courseController.getCoursesByCategory("Programming");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(list, response.getBody().getData());
    }

    @Test
    void testGetCoursesByDifficulty() {
        List<CourseDto> list = List.of(new CourseDto());
        when(courseService.getCoursesByDifficulty(DifficultyLevel.BEGINNER)).thenReturn(list);

        ResponseEntity<ApiResponse<List<CourseDto>>> response = courseController.getCoursesByDifficulty(DifficultyLevel.BEGINNER);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(list, response.getBody().getData());
    }

    @Test
    void testGetFreeCourses() {
        List<CourseDto> list = List.of(new CourseDto());
        when(courseService.getFreeCourses()).thenReturn(list);

        ResponseEntity<ApiResponse<List<CourseDto>>> response = courseController.getFreeCourses();

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetLatestCourses() {
        List<CourseDto> list = List.of(new CourseDto());
        when(courseService.getLatestCourses(PageRequest.of(0, 10))).thenReturn(list);

        ResponseEntity<ApiResponse<List<CourseDto>>> response = courseController.getLatestCourses(0, 10);

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetCourseById() {
        CourseDto dto = new CourseDto();
        when(courseService.getCourseById(1L)).thenReturn(dto);

        ResponseEntity<ApiResponse<CourseDto>> response = courseController.getCourseById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(dto, response.getBody().getData());
    }

    @Test
    void testCreateCourse() {
        CourseDto dto = new CourseDto();
        when(courseService.createCourse(dto)).thenReturn(dto);

        ResponseEntity<ApiResponse<CourseDto>> response = courseController.createCourse(dto);

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testUpdateCourse() {
        CourseDto dto = new CourseDto();
        when(courseService.updateCourse(1L, dto)).thenReturn(dto);

        ResponseEntity<ApiResponse<CourseDto>> response = courseController.updateCourse(1L, dto);

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testDeleteCourse() {
        doNothing().when(courseService).deleteCourse(1L);

        ResponseEntity<ApiResponse<Void>> response = courseController.deleteCourse(1L);

        assertEquals(200, response.getStatusCodeValue());
    }
}
