package com.lms.controller;

import com.lms.dto.CourseDto;
import com.lms.dto.EnrollmentDto;
import com.lms.service.CourseService;
import com.lms.service.EnrollmentService;
import com.lms.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PageControllerTest {

    @InjectMocks
    private PageController pageController;

    @Mock
    private CourseService courseService;

    @Mock
    private EnrollmentService enrollmentService;

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ========== dashboard Tests ==========

    @Test
    @DisplayName("Should load dashboard with statistics")
    void testDashboard_Success() {
        // Arrange
        Long totalUsers = 100L;
        Long totalCourses = 50L;
        when(userService.getUserCount()).thenReturn(totalUsers);
        when(courseService.getCourseCount()).thenReturn(totalCourses);

        // Act
        String viewName = pageController.dashboard(model);

        // Assert
        assertEquals("dashboard", viewName);
        verify(model, times(1)).addAttribute("totalUsers", totalUsers);
        verify(model, times(1)).addAttribute("totalCourses", totalCourses);
        verify(userService, times(1)).getUserCount();
        verify(courseService, times(1)).getCourseCount();
    }

    @Test
    @DisplayName("Should handle dashboard with zero statistics")
    void testDashboard_ZeroStats() {
        // Arrange
        when(userService.getUserCount()).thenReturn(0L);
        when(courseService.getCourseCount()).thenReturn(0L);

        // Act
        String viewName = pageController.dashboard(model);

        // Assert
        assertEquals("dashboard", viewName);
        verify(model, times(1)).addAttribute("totalUsers", 0L);
        verify(model, times(1)).addAttribute("totalCourses", 0L);
    }

    // ========== root Tests ==========

    @Test
    @DisplayName("Should load index page with latest courses")
    void testRoot_Success() {
        // Arrange
        List<CourseDto> latestCourses = Arrays.asList(
                createMockCourseDto(1L, "Course 1"),
                createMockCourseDto(2L, "Course 2")
        );
        PageRequest pageRequest = PageRequest.of(0, 8);
        when(courseService.getLatestCourses(pageRequest)).thenReturn(latestCourses);

        // Act
        String viewName = pageController.root(model);

        // Assert
        assertEquals("index", viewName);
        verify(model, times(1)).addAttribute("latestCourses", latestCourses);
        verify(courseService, times(1)).getLatestCourses(any(PageRequest.class));
    }

    @Test
    @DisplayName("Should load index page with empty courses list")
    void testRoot_NoCourses() {
        // Arrange
        when(courseService.getLatestCourses(any(PageRequest.class)))
                .thenReturn(Collections.emptyList());

        // Act
        String viewName = pageController.root(model);

        // Assert
        assertEquals("index", viewName);
        verify(model, times(1)).addAttribute(eq("latestCourses"), anyList());
    }

    // ========== courses Tests ==========

    @Test
    @DisplayName("Should load courses page with published courses")
    void testCourses_Success() {
        // Arrange
        List<CourseDto> publishedCourses = Arrays.asList(
                createMockCourseDto(1L, "Published Course 1"),
                createMockCourseDto(2L, "Published Course 2"),
                createMockCourseDto(3L, "Published Course 3")
        );
        when(courseService.getPublishedCourses()).thenReturn(publishedCourses);

        // Act
        String viewName = pageController.courses(model);

        // Assert
        assertEquals("courses", viewName);
        verify(model, times(1)).addAttribute("courses", publishedCourses);
        verify(courseService, times(1)).getPublishedCourses();
    }

    @Test
    @DisplayName("Should load courses page with empty list")
    void testCourses_EmptyList() {
        // Arrange
        when(courseService.getPublishedCourses()).thenReturn(Collections.emptyList());

        // Act
        String viewName = pageController.courses(model);

        // Assert
        assertEquals("courses", viewName);
        verify(model, times(1)).addAttribute(eq("courses"), anyList());
    }

    // ========== myCourses Tests ==========

    @Test
    @DisplayName("Should load my courses page with enrollments")
    void testMyCourses_Success() {
        // Arrange
        List<EnrollmentDto> enrollments = Arrays.asList(
                createMockEnrollmentDto(1L),
                createMockEnrollmentDto(2L)
        );
        when(enrollmentService.getStudentEnrollments()).thenReturn(enrollments);

        // Act
        String viewName = pageController.myCourses(model);

        // Assert
        assertEquals("my-courses", viewName);
        verify(model, times(1)).addAttribute("enrollments", enrollments);
        verify(enrollmentService, times(1)).getStudentEnrollments();
    }

    @Test
    @DisplayName("Should load my courses page with empty list on exception")
    void testMyCourses_ExceptionHandled() {
        // Arrange
        when(enrollmentService.getStudentEnrollments())
                .thenThrow(new RuntimeException("User not authenticated"));

        // Act
        String viewName = pageController.myCourses(model);

        // Assert
        assertEquals("my-courses", viewName);
        verify(model, times(1)).addAttribute("enrollments", Collections.emptyList());
        verify(enrollmentService, times(1)).getStudentEnrollments();
    }

    @Test
    @DisplayName("Should handle null pointer exception in my courses")
    void testMyCourses_NullPointerException() {
        // Arrange
        when(enrollmentService.getStudentEnrollments())
                .thenThrow(new NullPointerException("Null user"));

        // Act
        String viewName = pageController.myCourses(model);

        // Assert
        assertEquals("my-courses", viewName);
        verify(model, times(1)).addAttribute("enrollments", Collections.emptyList());
    }

    // ========== Other Pages Tests ==========

    @Test
    @DisplayName("Should load assignments page")
    void testAssignments() {
        // Act
        String viewName = pageController.assignments();

        // Assert
        assertEquals("assignments", viewName);
    }

    @Test
    @DisplayName("Should load quizzes page")
    void testQuizzes() {
        // Act
        String viewName = pageController.quizzes();

        // Assert
        assertEquals("quizzes", viewName);
    }

    @Test
    @DisplayName("Should load achievements page")
    void testAchievements() {
        // Act
        String viewName = pageController.achievements();

        // Assert
        assertEquals("achievements", viewName);
    }

    @Test
    @DisplayName("Should load profile page")
    void testProfile() {
        // Act
        String viewName = pageController.profile();

        // Assert
        assertEquals("profile", viewName);
    }

    // ========== Helper Methods ==========

    private CourseDto createMockCourseDto(Long id, String title) {
        CourseDto dto = new CourseDto();
        dto.setId(id);
        dto.setTitle(title);
        return dto;
    }

    private EnrollmentDto createMockEnrollmentDto(Long id) {
        EnrollmentDto dto = new EnrollmentDto();
        // Set necessary fields based on your EnrollmentDto structure
        return dto;
    }
}
