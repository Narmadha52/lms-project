package com.lms.controller;

import com.lms.dto.CourseDto;
import com.lms.dto.EnrollmentDto;
import com.lms.service.CourseService;
import com.lms.service.EnrollmentService;
import com.lms.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.ui.Model;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PageControllerTest {

    @Mock
    private CourseService courseService;

    @Mock
    private EnrollmentService enrollmentService;

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @InjectMocks
    private PageController pageController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDashboard() {
        when(userService.getUserCount()).thenReturn(5L);
        when(courseService.getCourseCount()).thenReturn(10L);

        String viewName = pageController.dashboard(model);

        verify(model).addAttribute("totalUsers", 5L);
        verify(model).addAttribute("totalCourses", 10L);
        assertEquals("dashboard", viewName);
    }

    @Test
    void testRoot() {
        List<CourseDto> latestCourses = List.of(new CourseDto());
        when(courseService.getLatestCourses(PageRequest.of(0, 8))).thenReturn(latestCourses);

        String viewName = pageController.root(model);

        verify(model).addAttribute("latestCourses", latestCourses);
        assertEquals("index", viewName);
    }

    @Test
    void testCourses() {
        List<CourseDto> courses = List.of(new CourseDto());
        when(courseService.getPublishedCourses()).thenReturn(courses);

        String viewName = pageController.courses(model);

        verify(model).addAttribute("courses", courses);
        assertEquals("courses", viewName);
    }

    @Test
    void testMyCourses() {
        List<EnrollmentDto> enrollments = List.of(new EnrollmentDto());
        when(enrollmentService.getStudentEnrollments()).thenReturn(enrollments);

        String viewName = pageController.myCourses(model);

        verify(model).addAttribute("enrollments", enrollments);
        assertEquals("my-courses", viewName);
    }

    @Test
    void testMyCourses_WhenExceptionThrown() {
        when(enrollmentService.getStudentEnrollments()).thenThrow(new RuntimeException("Error"));
        String viewName = pageController.myCourses(model);

        verify(model).addAttribute(eq("enrollments"), anyList());
        assertEquals("my-courses", viewName);
    }

    @Test
    void testAssignments() {
        assertEquals("assignments", pageController.assignments());
    }

    @Test
    void testQuizzes() {
        assertEquals("quizzes", pageController.quizzes());
    }

    @Test
    void testAchievements() {
        assertEquals("achievements", pageController.achievements());
    }

    @Test
    void testProfile() {
        assertEquals("profile", pageController.profile());
    }
}
