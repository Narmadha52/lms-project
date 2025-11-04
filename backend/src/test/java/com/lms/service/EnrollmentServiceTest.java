package com.lms.service;

import com.lms.dto.EnrollmentDto;
import com.lms.model.*;
import com.lms.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EnrollmentServiceTest {

    @Mock private EnrollmentRepository enrollmentRepository;
    @Mock private CourseRepository courseRepository;
    @Mock private LessonRepository lessonRepository;
    @Mock private LessonProgressRepository lessonProgressRepository;
    @Mock private AuthService authService;

    @InjectMocks
    private EnrollmentService enrollmentService;

    private User student;
    private Course course;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        student = new User();
        student.setId(1L);
        student.setUsername("john");

        course = new Course();
        course.setId(10L);
        course.setIsPublished(true);
    }

    // ✅ Test enroll in course
    @Test
    void enrollInCourse_shouldEnrollSuccessfully() {
        when(authService.getCurrentUser()).thenReturn(student);
        when(courseRepository.findById(10L)).thenReturn(Optional.of(course));
        when(enrollmentRepository.existsByStudentAndCourse(student, course)).thenReturn(false);

        Lesson lesson = new Lesson();
        lesson.setId(100L);
        when(lessonRepository.findByCourseAndIsPublished(course, true)).thenReturn(List.of(lesson));

        Enrollment savedEnrollment = new Enrollment(student, course);
        savedEnrollment.setId(500L);
        when(enrollmentRepository.save(any(Enrollment.class))).thenReturn(savedEnrollment);

        EnrollmentDto dto = enrollmentService.enrollInCourse(10L);

        assertEquals(savedEnrollment.getId(), dto.getId());
        verify(lessonProgressRepository, times(1)).save(any(LessonProgress.class));
    }

    // ❌ Course not found
    @Test
    void enrollInCourse_shouldThrowWhenCourseNotFound() {
        when(courseRepository.findById(10L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> enrollmentService.enrollInCourse(10L));
    }

    // ❌ Already enrolled
    @Test
    void enrollInCourse_shouldThrowWhenAlreadyEnrolled() {
        when(authService.getCurrentUser()).thenReturn(student);
        when(courseRepository.findById(10L)).thenReturn(Optional.of(course));
        when(enrollmentRepository.existsByStudentAndCourse(student, course)).thenReturn(true);

        assertThrows(RuntimeException.class, () -> enrollmentService.enrollInCourse(10L));
    }

    // ✅ Unenroll
    @Test
    void unenrollFromCourse_shouldDeleteEnrollment() {
        when(authService.getCurrentUser()).thenReturn(student);
        when(courseRepository.findById(10L)).thenReturn(Optional.of(course));

        Enrollment enrollment = new Enrollment(student, course);
        when(enrollmentRepository.findByStudentAndCourse(student, course))
                .thenReturn(Optional.of(enrollment));

        enrollmentService.unenrollFromCourse(10L);

        verify(enrollmentRepository, times(1)).delete(enrollment);
    }
}
