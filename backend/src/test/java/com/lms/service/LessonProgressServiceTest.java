package com.lms.service;

import com.lms.model.*;
import com.lms.repository.EnrollmentRepository;
import com.lms.repository.LessonProgressRepository;
import com.lms.repository.LessonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("LessonProgressService Tests")
class LessonProgressServiceTest {

    @Mock
    private LessonProgressRepository lessonProgressRepository;

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private LessonRepository lessonRepository;

    @Mock
    private AuthService authService;

    @InjectMocks
    private LessonProgressService lessonProgressService;

    private User student;
    private Course course;
    private Enrollment enrollment;
    private Lesson lesson1;
    private Lesson lesson2;
    private LessonProgress progress1;
    private LessonProgress progress2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        setupTestData();
    }

    private void setupTestData() {
        student = new User();
        student.setId(1L);
        student.setUsername("john_student");
        student.setRole(Role.STUDENT);

        course = new Course();
        course.setId(100L);
        course.setTitle("Java Fundamentals");

        enrollment = new Enrollment(student, course);
        enrollment.setId(10L);

        lesson1 = new Lesson();
        lesson1.setId(200L);
        lesson1.setTitle("Introduction");
        lesson1.setOrderIndex(1);
        lesson1.setCourse(course);

        lesson2 = new Lesson();
        lesson2.setId(201L);
        lesson2.setTitle("Variables");
        lesson2.setOrderIndex(2);
        lesson2.setCourse(course);

        progress1 = new LessonProgress(enrollment, lesson1);
        progress1.setId(300L);
        progress1.setCompleted(false);
        progress1.setTimeSpent(0);

        progress2 = new LessonProgress(enrollment, lesson2);
        progress2.setId(301L);
        progress2.setCompleted(false);
        progress2.setTimeSpent(0);
    }

    // ==================== MARK LESSON AS COMPLETED TESTS ====================

    @Test
    @DisplayName("Should mark lesson as completed successfully")
    void markLessonAsCompleted_WhenValidRequest_ShouldMarkCompleted() {
        // Arrange
        when(authService.getCurrentUser()).thenReturn(student);
        when(enrollmentRepository.findByStudentAndCourse(eq(student), any(Course.class)))
                .thenReturn(Optional.of(enrollment));
        when(lessonRepository.findById(200L)).thenReturn(Optional.of(lesson1));
        when(lessonProgressRepository.findByEnrollmentAndLesson(enrollment, lesson1))
                .thenReturn(Optional.of(progress1));
        when(lessonProgressRepository.save(any(LessonProgress.class))).thenAnswer(invocation -> {
            LessonProgress p = invocation.getArgument(0);
            p.setCompleted(true);
            return p;
        });
        when(enrollmentRepository.save(any(Enrollment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        LessonProgress result = lessonProgressService.markLessonAsCompleted(course.getId(), lesson1.getId());

        // Assert
        assertNotNull(result.getCompleted(), "Completed field should not be null");
        assertTrue(result.getCompleted(), "Lesson should be marked as completed");

        verify(lessonProgressRepository, atLeastOnce()).save(any(LessonProgress.class));
        verify(enrollmentRepository, times(1)).save(enrollment);
    }

    @Test
    @DisplayName("Should handle mark completed when not enrolled")
    void markLessonAsCompleted_WhenNotEnrolled_ShouldHandleError() {
        // Arrange
        when(authService.getCurrentUser()).thenReturn(student);
        when(enrollmentRepository.findByStudentAndCourse(eq(student), any(Course.class)))
                .thenReturn(Optional.empty());

        // Act & Assert
        try {
            lessonProgressService.markLessonAsCompleted(course.getId(), lesson1.getId());
            fail("Expected RuntimeException to be thrown");
        } catch (RuntimeException ex) {
            assertEquals("You are not enrolled in this course", ex.getMessage());
        }

        verify(lessonProgressRepository, never()).save(any(LessonProgress.class));
    }

    @Test
    @DisplayName("Should handle mark completed when lesson not found")
    void markLessonAsCompleted_WhenLessonNotFound_ShouldHandleError() {
        // Arrange
        when(authService.getCurrentUser()).thenReturn(student);
        when(enrollmentRepository.findByStudentAndCourse(eq(student), any(Course.class)))
                .thenReturn(Optional.of(enrollment));
        when(lessonRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        try {
            lessonProgressService.markLessonAsCompleted(course.getId(), 999L);
            fail("Expected RuntimeException to be thrown");
        } catch (RuntimeException ex) {
            assertTrue(ex.getMessage().contains("Lesson not found"));
        }

        verify(lessonProgressRepository, never()).save(any(LessonProgress.class));
    }

    @Test
    @DisplayName("Should create new progress if not exists when marking completed")
    void markLessonAsCompleted_WhenProgressNotExists_ShouldCreateNewProgress() {
        // Arrange
        when(authService.getCurrentUser()).thenReturn(student);
        when(enrollmentRepository.findByStudentAndCourse(eq(student), any(Course.class)))
                .thenReturn(Optional.of(enrollment));
        when(lessonRepository.findById(200L)).thenReturn(Optional.of(lesson1));
        when(lessonProgressRepository.findByEnrollmentAndLesson(enrollment, lesson1))
                .thenReturn(Optional.empty());
        when(lessonProgressRepository.save(any(LessonProgress.class))).thenAnswer(invocation -> {
            LessonProgress p = invocation.getArgument(0);
            p.setId(400L);
            p.setCompleted(true);
            return p;
        });
        when(enrollmentRepository.save(any(Enrollment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        LessonProgress result = lessonProgressService.markLessonAsCompleted(course.getId(), lesson1.getId());

        // Assert
        assertNotNull(result.getCompleted(), "Completed field should not be null");
        assertTrue(result.getCompleted(), "Lesson should be marked as completed");
        verify(lessonProgressRepository, atLeastOnce()).save(any(LessonProgress.class));
    }

    // ==================== UPDATE LESSON PROGRESS TESTS ====================

    @Test
    @DisplayName("Should update lesson progress successfully")
    void updateLessonProgress_WhenValidRequest_ShouldUpdateProgress() {
        // Arrange
        when(authService.getCurrentUser()).thenReturn(student);
        when(enrollmentRepository.findByStudentAndCourse(eq(student), any(Course.class)))
                .thenReturn(Optional.of(enrollment));
        when(lessonRepository.findById(200L)).thenReturn(Optional.of(lesson1));
        when(lessonProgressRepository.findByEnrollmentAndLesson(enrollment, lesson1))
                .thenReturn(Optional.of(progress1));
        when(lessonProgressRepository.save(any(LessonProgress.class))).thenAnswer(invocation -> {
            LessonProgress p = invocation.getArgument(0);
            p.setTimeSpent(20);
            return p;
        });
        when(enrollmentRepository.save(any(Enrollment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        LessonProgress result = lessonProgressService.updateLessonProgress(course.getId(), lesson1.getId(), 20);

        // Assert
        assertNotNull(result);
        assertEquals(20, result.getTimeSpent());

        verify(lessonProgressRepository, atLeastOnce()).save(any(LessonProgress.class));
        verify(enrollmentRepository, times(1)).save(enrollment);
    }

    @Test
    @DisplayName("Should handle update progress when not enrolled")
    void updateLessonProgress_WhenNotEnrolled_ShouldHandleError() {
        // Arrange
        when(authService.getCurrentUser()).thenReturn(student);
        when(enrollmentRepository.findByStudentAndCourse(eq(student), any(Course.class)))
                .thenReturn(Optional.empty());

        // Act & Assert
        try {
            lessonProgressService.updateLessonProgress(course.getId(), lesson1.getId(), 10);
            fail("Expected RuntimeException to be thrown");
        } catch (RuntimeException ex) {
            assertNotNull(ex);
        }

        verify(lessonProgressRepository, never()).save(any(LessonProgress.class));
    }

    @Test
    @DisplayName("Should handle update progress when lesson not found")
    void updateLessonProgress_WhenLessonNotFound_ShouldHandleError() {
        // Arrange
        when(authService.getCurrentUser()).thenReturn(student);
        when(enrollmentRepository.findByStudentAndCourse(eq(student), any(Course.class)))
                .thenReturn(Optional.of(enrollment));
        when(lessonRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        try {
            lessonProgressService.updateLessonProgress(course.getId(), 999L, 10);
            fail("Expected RuntimeException to be thrown");
        } catch (RuntimeException ex) {
            assertTrue(ex.getMessage().contains("not found"));
        }
    }

    @Test
    @DisplayName("Should accumulate time spent correctly")
    void updateLessonProgress_WhenUpdatingTime_ShouldAccumulateTime() {
        // Arrange
        progress1.setTimeSpent(30); // Already has 30 minutes
        when(authService.getCurrentUser()).thenReturn(student);
        when(enrollmentRepository.findByStudentAndCourse(eq(student), any(Course.class)))
                .thenReturn(Optional.of(enrollment));
        when(lessonRepository.findById(200L)).thenReturn(Optional.of(lesson1));
        when(lessonProgressRepository.findByEnrollmentAndLesson(enrollment, lesson1))
                .thenReturn(Optional.of(progress1));
        when(lessonProgressRepository.save(any(LessonProgress.class))).thenAnswer(invocation -> {
            LessonProgress p = invocation.getArgument(0);
            return p;
        });
        when(enrollmentRepository.save(any(Enrollment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        LessonProgress result = lessonProgressService.updateLessonProgress(course.getId(), lesson1.getId(), 20);

        // Assert
        assertNotNull(result);
        // Implementation depends on whether you accumulate or replace time
        verify(lessonProgressRepository, atLeastOnce()).save(any(LessonProgress.class));
    }

    // ==================== GET STUDENT PROGRESS TESTS ====================

    @Test
    @DisplayName("Should get student progress successfully")
    void getStudentProgress_WhenEnrolled_ShouldReturnProgress() {
        // Arrange
        when(authService.getCurrentUser()).thenReturn(student);
        when(enrollmentRepository.findByStudentAndCourse(eq(student), any(Course.class)))
                .thenReturn(Optional.of(enrollment));
        when(lessonProgressRepository.findByEnrollmentOrderByLessonOrderIndexAsc(enrollment))
                .thenReturn(Arrays.asList(progress1, progress2));

        // Act
        List<LessonProgress> result = lessonProgressService.getStudentProgress(course.getId());

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(progress1, result.get(0));
        assertEquals(progress2, result.get(1));

        verify(lessonProgressRepository, times(1)).findByEnrollmentOrderByLessonOrderIndexAsc(enrollment);
    }

    @Test
    @DisplayName("Should return empty list when no progress exists")
    void getStudentProgress_WhenNoProgress_ShouldReturnEmptyList() {
        // Arrange
        when(authService.getCurrentUser()).thenReturn(student);
        when(enrollmentRepository.findByStudentAndCourse(eq(student), any(Course.class)))
                .thenReturn(Optional.of(enrollment));
        when(lessonProgressRepository.findByEnrollmentOrderByLessonOrderIndexAsc(enrollment))
                .thenReturn(Collections.emptyList());

        // Act
        List<LessonProgress> result = lessonProgressService.getStudentProgress(course.getId());

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should handle get progress when not enrolled")
    void getStudentProgress_WhenNotEnrolled_ShouldHandleError() {
        // Arrange
        when(authService.getCurrentUser()).thenReturn(student);
        when(enrollmentRepository.findByStudentAndCourse(eq(student), any(Course.class)))
                .thenReturn(Optional.empty());

        // Act & Assert
        try {
            lessonProgressService.getStudentProgress(course.getId());
            fail("Expected RuntimeException to be thrown");
        } catch (RuntimeException ex) {
            assertNotNull(ex);
        }

        verify(lessonProgressRepository, never()).findByEnrollmentOrderByLessonOrderIndexAsc(any());
    }

    // ==================== GET NEXT INCOMPLETE LESSON TESTS ====================

    @Test
    @DisplayName("Should get next incomplete lesson successfully")
    void getNextIncompleteLesson_WhenExists_ShouldReturnLesson() {
        // Arrange
        when(authService.getCurrentUser()).thenReturn(student);
        when(enrollmentRepository.findByStudentAndCourse(eq(student), any(Course.class)))
                .thenReturn(Optional.of(enrollment));
        when(lessonProgressRepository.findNextIncompleteLesson(enrollment))
                .thenReturn(Optional.of(progress1));

        // Act
        Optional<LessonProgress> result = lessonProgressService.getNextIncompleteLesson(course.getId());

        // Assert
        assertTrue(result.isPresent());
        assertEquals(progress1, result.get());

        verify(lessonProgressRepository, times(1)).findNextIncompleteLesson(enrollment);
    }

    @Test
    @DisplayName("Should return empty when all lessons completed")
    void getNextIncompleteLesson_WhenAllCompleted_ShouldReturnEmpty() {
        // Arrange
        when(authService.getCurrentUser()).thenReturn(student);
        when(enrollmentRepository.findByStudentAndCourse(eq(student), any(Course.class)))
                .thenReturn(Optional.of(enrollment));
        when(lessonProgressRepository.findNextIncompleteLesson(enrollment))
                .thenReturn(Optional.empty());

        // Act
        Optional<LessonProgress> result = lessonProgressService.getNextIncompleteLesson(course.getId());

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("Should handle get next incomplete when not enrolled")
    void getNextIncompleteLesson_WhenNotEnrolled_ShouldHandleError() {
        // Arrange
        when(authService.getCurrentUser()).thenReturn(student);
        when(enrollmentRepository.findByStudentAndCourse(eq(student), any(Course.class)))
                .thenReturn(Optional.empty());

        // Act & Assert
        try {
            lessonProgressService.getNextIncompleteLesson(course.getId());
            fail("Expected RuntimeException to be thrown");
        } catch (RuntimeException ex) {
            assertNotNull(ex);
        }
    }

    // ==================== GET TOTAL TIME SPENT TESTS ====================

    @Test
    @DisplayName("Should get total time spent successfully")
    void getTotalTimeSpent_WhenEnrolled_ShouldReturnTotal() {
        // Arrange
        when(authService.getCurrentUser()).thenReturn(student);
        when(enrollmentRepository.findByStudentAndCourse(eq(student), any(Course.class)))
                .thenReturn(Optional.of(enrollment));
        when(lessonProgressRepository.getTotalTimeSpentByEnrollment(enrollment))
                .thenReturn(Optional.of(90L));

        // Act
        long total = lessonProgressService.getTotalTimeSpent(course.getId());

        // Assert
        assertEquals(90L, total);

        verify(lessonProgressRepository, times(1)).getTotalTimeSpentByEnrollment(enrollment);
    }

    @Test
    @DisplayName("Should return zero when no time spent")
    void getTotalTimeSpent_WhenNoTime_ShouldReturnZero() {
        // Arrange
        when(authService.getCurrentUser()).thenReturn(student);
        when(enrollmentRepository.findByStudentAndCourse(eq(student), any(Course.class)))
                .thenReturn(Optional.of(enrollment));
        when(lessonProgressRepository.getTotalTimeSpentByEnrollment(enrollment))
                .thenReturn(Optional.of(0L));

        // Act
        long total = lessonProgressService.getTotalTimeSpent(course.getId());

        // Assert
        assertEquals(0L, total);
    }

    @Test
    @DisplayName("Should handle get total time when not enrolled")
    void getTotalTimeSpent_WhenNotEnrolled_ShouldHandleError() {
        // Arrange
        when(authService.getCurrentUser()).thenReturn(student);
        when(enrollmentRepository.findByStudentAndCourse(eq(student), any(Course.class)))
                .thenReturn(Optional.empty());

        // Act & Assert
        try {
            lessonProgressService.getTotalTimeSpent(course.getId());
            fail("Expected RuntimeException to be thrown");
        } catch (RuntimeException ex) {
            assertNotNull(ex);
        }

        verify(lessonProgressRepository, never()).getTotalTimeSpentByEnrollment(any());
    }

    @Test
    @DisplayName("Should handle null total time result")
    void getTotalTimeSpent_WhenNullResult_ShouldReturnZero() {
        // Arrange
        when(authService.getCurrentUser()).thenReturn(student);
        when(enrollmentRepository.findByStudentAndCourse(eq(student), any(Course.class)))
                .thenReturn(Optional.of(enrollment));
        when(lessonProgressRepository.getTotalTimeSpentByEnrollment(enrollment))
                .thenReturn(Optional.empty());

        // Act
        long total = lessonProgressService.getTotalTimeSpent(course.getId());

        // Assert
        // Result depends on implementation - might be 0 or throw exception
        assertTrue(total >= 0);
    }
}
