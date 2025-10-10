package com.lms.service;

import com.lms.model.Enrollment;
import com.lms.model.Lesson;
import com.lms.model.LessonProgress;
import com.lms.model.User;
import com.lms.model.Course;
import com.lms.repository.EnrollmentRepository;
import com.lms.repository.LessonProgressRepository;
import com.lms.repository.LessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LessonProgressService {

    @Autowired
    private LessonProgressRepository lessonProgressRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private AuthService authService;

    public LessonProgress markLessonAsCompleted(Long courseId, Long lessonId) {
        User currentUser = authService.getCurrentUser();
        
        // Get enrollment
        Enrollment enrollment = enrollmentRepository.findByStudentAndCourse(
                currentUser, 
                new Course() {{ setId(courseId); }}
        ).orElseThrow(() -> new RuntimeException("You are not enrolled in this course"));

        // Get lesson
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found with id: " + lessonId));

        // Get or create lesson progress
        LessonProgress lessonProgress = lessonProgressRepository
                .findByEnrollmentAndLesson(enrollment, lesson)
                .orElseGet(() -> {
                    LessonProgress newProgress = new LessonProgress(enrollment, lesson);
                    return lessonProgressRepository.save(newProgress);
                });

        // Mark as completed
        lessonProgress.markAsCompleted();
        lessonProgress.markAsAccessed();
        
        LessonProgress savedProgress = lessonProgressRepository.save(lessonProgress);

        // Update enrollment progress
        enrollment.updateProgress();
        enrollment.markAsAccessed();
        enrollmentRepository.save(enrollment);

        return savedProgress;
    }

    public LessonProgress updateLessonProgress(Long courseId, Long lessonId, int timeSpentMinutes) {
        User currentUser = authService.getCurrentUser();
        
        // Get enrollment
        Enrollment enrollment = enrollmentRepository.findByStudentAndCourse(
                currentUser, 
                new Course() {{ setId(courseId); }}
        ).orElseThrow(() -> new RuntimeException("You are not enrolled in this course"));

        // Get lesson
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found with id: " + lessonId));

        // Get or create lesson progress
        LessonProgress lessonProgress = lessonProgressRepository
                .findByEnrollmentAndLesson(enrollment, lesson)
                .orElseGet(() -> {
                    LessonProgress newProgress = new LessonProgress(enrollment, lesson);
                    return lessonProgressRepository.save(newProgress);
                });

        // Update progress
        lessonProgress.addTimeSpent(timeSpentMinutes);
        lessonProgress.markAsAccessed();
        
        LessonProgress savedProgress = lessonProgressRepository.save(lessonProgress);

        // Update enrollment progress
        enrollment.updateProgress();
        enrollment.markAsAccessed();
        enrollmentRepository.save(enrollment);

        return savedProgress;
    }

    public List<LessonProgress> getStudentProgress(Long courseId) {
        User currentUser = authService.getCurrentUser();
        
        Enrollment enrollment = enrollmentRepository.findByStudentAndCourse(
                currentUser, 
                new Course() {{ setId(courseId); }}
        ).orElseThrow(() -> new RuntimeException("You are not enrolled in this course"));

        return lessonProgressRepository.findByEnrollmentOrderByLessonOrderIndexAsc(enrollment);
    }

    public Optional<LessonProgress> getNextIncompleteLesson(Long courseId) {
        User currentUser = authService.getCurrentUser();
        
        Enrollment enrollment = enrollmentRepository.findByStudentAndCourse(
                currentUser, 
                new Course() {{ setId(courseId); }}
        ).orElseThrow(() -> new RuntimeException("You are not enrolled in this course"));

        return lessonProgressRepository.findNextIncompleteLesson(enrollment);
    }

    public long getTotalTimeSpent(Long courseId) {
        User currentUser = authService.getCurrentUser();
        
        Enrollment enrollment = enrollmentRepository.findByStudentAndCourse(
                currentUser, 
                new Course() {{ setId(courseId); }}
        ).orElseThrow(() -> new RuntimeException("You are not enrolled in this course"));

        return lessonProgressRepository.getTotalTimeSpentByEnrollment(enrollment).orElse(0L);
    }
}

