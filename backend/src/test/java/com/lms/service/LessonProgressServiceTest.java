package com.lms.service;

import com.lms.model.*;
import com.lms.repository.EnrollmentRepository;
import com.lms.repository.LessonProgressRepository;
import com.lms.repository.LessonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

    private User user;
    private Course course;
    private Enrollment enrollment;
    private Lesson lesson;
    private LessonProgress progress;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);

        course = new Course();
        course.setId(100L);

        enrollment = new Enrollment(user, course);
        enrollment.setId(10L);

        lesson = new Lesson();
        lesson.setId(200L);

        progress = new LessonProgress(enrollment, lesson);
        progress.setId(300L);
    }

    // ✅ markLessonAsCompleted: success
    @Test
    void testMarkLessonAsCompleted_Success() {
        when(authService.getCurrentUser()).thenReturn(user);
        when(enrollmentRepository.findByStudentAndCourse(eq(user), any(Course.class)))
                .thenReturn(Optional.of(enrollment));
        when(lessonRepository.findById(lesson.getId())).thenReturn(Optional.of(lesson));
        when(lessonProgressRepository.findByEnrollmentAndLesson(enrollment, lesson))
                .thenReturn(Optional.of(progress));
        when(lessonProgressRepository.save(any(LessonProgress.class))).thenReturn(progress);

        LessonProgress result = lessonProgressService.markLessonAsCompleted(course.getId(), lesson.getId());

        assertNotNull(result);
        verify(lessonProgressRepository, atLeastOnce()).save(any(LessonProgress.class));
        verify(enrollmentRepository, times(1)).save(enrollment);
    }

    // ❌ markLessonAsCompleted: not enrolled
    @Test
    void testMarkLessonAsCompleted_NotEnrolled() {
        when(authService.getCurrentUser()).thenReturn(user);
        when(enrollmentRepository.findByStudentAndCourse(eq(user), any(Course.class)))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> lessonProgressService.markLessonAsCompleted(course.getId(), lesson.getId()));
        assertEquals("You are not enrolled in this course", ex.getMessage());
    }

    // ❌ markLessonAsCompleted: lesson not found
    @Test
    void testMarkLessonAsCompleted_LessonNotFound() {
        when(authService.getCurrentUser()).thenReturn(user);
        when(enrollmentRepository.findByStudentAndCourse(eq(user), any(Course.class)))
                .thenReturn(Optional.of(enrollment));
        when(lessonRepository.findById(lesson.getId())).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> lessonProgressService.markLessonAsCompleted(course.getId(), lesson.getId()));
        assertEquals("Lesson not found with id: " + lesson.getId(), ex.getMessage());
    }

    // ✅ updateLessonProgress: success
    @Test
    void testUpdateLessonProgress_Success() {
        when(authService.getCurrentUser()).thenReturn(user);
        when(enrollmentRepository.findByStudentAndCourse(eq(user), any(Course.class)))
                .thenReturn(Optional.of(enrollment));
        when(lessonRepository.findById(lesson.getId())).thenReturn(Optional.of(lesson));
        when(lessonProgressRepository.findByEnrollmentAndLesson(enrollment, lesson))
                .thenReturn(Optional.of(progress));
        when(lessonProgressRepository.save(any(LessonProgress.class))).thenReturn(progress);

        LessonProgress result = lessonProgressService.updateLessonProgress(course.getId(), lesson.getId(), 20);

        assertNotNull(result);
        verify(lessonProgressRepository, atLeastOnce()).save(any(LessonProgress.class));
        verify(enrollmentRepository, times(1)).save(enrollment);
    }

    // ❌ updateLessonProgress: not enrolled
    @Test
    void testUpdateLessonProgress_NotEnrolled() {
        when(authService.getCurrentUser()).thenReturn(user);
        when(enrollmentRepository.findByStudentAndCourse(eq(user), any(Course.class)))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> lessonProgressService.updateLessonProgress(course.getId(), lesson.getId(), 10));
    }

    // ✅ getStudentProgress: success
    @Test
    void testGetStudentProgress_Success() {
        when(authService.getCurrentUser()).thenReturn(user);
        when(enrollmentRepository.findByStudentAndCourse(eq(user), any(Course.class)))
                .thenReturn(Optional.of(enrollment));
        when(lessonProgressRepository.findByEnrollmentOrderByLessonOrderIndexAsc(enrollment))
                .thenReturn(List.of(progress));

        List<LessonProgress> result = lessonProgressService.getStudentProgress(course.getId());

        assertEquals(1, result.size());
        verify(lessonProgressRepository).findByEnrollmentOrderByLessonOrderIndexAsc(enrollment);
    }

    // ❌ getStudentProgress: not enrolled
    @Test
    void testGetStudentProgress_NotEnrolled() {
        when(authService.getCurrentUser()).thenReturn(user);
        when(enrollmentRepository.findByStudentAndCourse(eq(user), any(Course.class)))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> lessonProgressService.getStudentProgress(course.getId()));
    }

    // ✅ getNextIncompleteLesson: success
    @Test
    void testGetNextIncompleteLesson_Success() {
        when(authService.getCurrentUser()).thenReturn(user);
        when(enrollmentRepository.findByStudentAndCourse(eq(user), any(Course.class)))
                .thenReturn(Optional.of(enrollment));
        when(lessonProgressRepository.findNextIncompleteLesson(enrollment))
                .thenReturn(Optional.of(progress));

        Optional<LessonProgress> result = lessonProgressService.getNextIncompleteLesson(course.getId());

        assertTrue(result.isPresent());
        verify(lessonProgressRepository).findNextIncompleteLesson(enrollment);
    }

    // ✅ getTotalTimeSpent: success
    @Test
    void testGetTotalTimeSpent_Success() {
        when(authService.getCurrentUser()).thenReturn(user);
        when(enrollmentRepository.findByStudentAndCourse(eq(user), any(Course.class)))
                .thenReturn(Optional.of(enrollment));
        when(lessonProgressRepository.getTotalTimeSpentByEnrollment(enrollment))
                .thenReturn(Optional.of(90L));

        long total = lessonProgressService.getTotalTimeSpent(course.getId());

        assertEquals(90L, total);
        verify(lessonProgressRepository).getTotalTimeSpentByEnrollment(enrollment);
    }
}
