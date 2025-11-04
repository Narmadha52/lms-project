package com.lms.service;

import com.lms.dto.EnrollmentDto;
import com.lms.model.*;
import com.lms.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("EnrollmentService Unit Tests")
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
void setUp() {
    MockitoAnnotations.openMocks(this);

    student = new User();
    student.setId(1L);
    student.setUsername("john_student");
    student.setRole(Role.STUDENT);

    course = new Course();
    course.setId(10L);
    course.setTitle("Spring Boot Fundamentals");
    course.setIsPublished(true);
}

// ==================== ENROLL IN COURSE TESTS ====================

@Test
@DisplayName("Should enroll in course successfully")
void enrollInCourse_WhenValidRequest_ShouldEnrollSuccessfully() {
    // Arrange
    when(authService.getCurrentUser()).thenReturn(student);
    when(courseRepository.findById(10L)).thenReturn(Optional.of(course));
    when(enrollmentRepository.existsByStudentAndCourse(student, course)).thenReturn(false);

    Lesson lesson = new Lesson();
    lesson.setId(100L);
    when(lessonRepository.findByCourseAndIsPublished(course, true))
            .thenReturn(List.of(lesson));

    Enrollment savedEnrollment = new Enrollment(student, course);
    savedEnrollment.setId(500L);
    when(enrollmentRepository.save(any(Enrollment.class))).thenReturn(savedEnrollment);

    // Act
    EnrollmentDto result = enrollmentService.enrollInCourse(10L);

    // Assert
    assertNotNull(result, "EnrollmentDto should not be null");
    assertEquals(savedEnrollment.getId(), result.getId(), "Enrollment ID should match");
    verify(lessonProgressRepository, times(1)).save(any(LessonProgress.class));
    verify(enrollmentRepository, times(1)).save(any(Enrollment.class));
}

@Test
@DisplayName("Should throw when course not found")
void enrollInCourse_WhenCourseNotFound_ShouldThrowException() {
    // Arrange
    when(courseRepository.findById(10L)).thenReturn(Optional.empty());

    // Act & Assert
    RuntimeException ex = assertThrows(RuntimeException.class,
            () -> enrollmentService.enrollInCourse(10L),
            "Expected RuntimeException when course not found");

    assertTrue(ex.getMessage().toLowerCase().contains("not found"));
    verify(enrollmentRepository, never()).save(any());
}

@Test
@DisplayName("Should throw when already enrolled")
void enrollInCourse_WhenAlreadyEnrolled_ShouldThrowException() {
    // Arrange
    when(authService.getCurrentUser()).thenReturn(student);
    when(courseRepository.findById(10L)).thenReturn(Optional.of(course));
    when(enrollmentRepository.existsByStudentAndCourse(student, course)).thenReturn(true);

    // Act & Assert
    RuntimeException ex = assertThrows(RuntimeException.class,
            () -> enrollmentService.enrollInCourse(10L),
            "Expected RuntimeException when already enrolled");

    assertTrue(ex.getMessage().toLowerCase().contains("already"));
    verify(enrollmentRepository, never()).save(any());
}

@Test
@DisplayName("Should throw when course is unpublished")
void enrollInCourse_WhenCourseUnpublished_ShouldThrowException() {
    // Arrange
    course.setIsPublished(false);
    when(authService.getCurrentUser()).thenReturn(student);
    when(courseRepository.findById(10L)).thenReturn(Optional.of(course));

    // Act & Assert
    RuntimeException ex = assertThrows(RuntimeException.class,
            () -> enrollmentService.enrollInCourse(10L),
            "Expected RuntimeException when course is unpublished");

    assertTrue(ex.getMessage().toLowerCase().contains("unpublished"));
    verify(enrollmentRepository, never()).save(any());
}

// ==================== UNENROLL FROM COURSE TESTS ====================

@Test
@DisplayName("Should unenroll from course successfully")
void unenrollFromCourse_WhenEnrolled_ShouldDeleteEnrollment() {
    // Arrange
    when(authService.getCurrentUser()).thenReturn(student);
    when(courseRepository.findById(10L)).thenReturn(Optional.of(course));

    Enrollment enrollment = new Enrollment(student, course);
    when(enrollmentRepository.findByStudentAndCourse(student, course))
            .thenReturn(Optional.of(enrollment));

    // Act
    enrollmentService.unenrollFromCourse(10L);

    // Assert
    verify(enrollmentRepository, times(1)).delete(enrollment);
}

@Test
@DisplayName("Should throw when unenrolling without enrollment")
void unenrollFromCourse_WhenNotEnrolled_ShouldThrowException() {
    // Arrange
    when(authService.getCurrentUser()).thenReturn(student);
    when(courseRepository.findById(10L)).thenReturn(Optional.of(course));
    when(enrollmentRepository.findByStudentAndCourse(student, course))
            .thenReturn(Optional.empty());

    // Act & Assert
    RuntimeException ex = assertThrows(RuntimeException.class,
            () -> enrollmentService.unenrollFromCourse(10L),
            "Expected RuntimeException when not enrolled");

    assertTrue(ex.getMessage().toLowerCase().contains("not enrolled"));
    verify(enrollmentRepository, never()).delete(any());
}

@Test
@DisplayName("Should throw when unenrolling and course not found")
void unenrollFromCourse_WhenCourseNotFound_ShouldThrowException() {
    // Arrange
    when(authService.getCurrentUser()).thenReturn(student);
    when(courseRepository.findById(10L)).thenReturn(Optional.empty());

    // Act & Assert
    RuntimeException ex = assertThrows(RuntimeException.class,
            () -> enrollmentService.unenrollFromCourse(10L),
            "Expected RuntimeException when course not found during unenroll");

    assertTrue(ex.getMessage().toLowerCase().contains("not found"));
    verify(enrollmentRepository, never()).delete(any());
}
}
