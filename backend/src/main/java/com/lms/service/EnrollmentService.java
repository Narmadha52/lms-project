package com.lms.service;

import com.lms.dto.EnrollmentDto;
import com.lms.model.Course;
import com.lms.model.Enrollment;
import com.lms.model.LessonProgress;
import com.lms.model.Lesson;
import com.lms.model.User;
import com.lms.repository.CourseRepository;
import com.lms.repository.EnrollmentRepository;
import com.lms.repository.LessonProgressRepository;
import com.lms.repository.LessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private LessonProgressRepository lessonProgressRepository;

    @Autowired
    private AuthService authService;

    public EnrollmentDto enrollInCourse(Long courseId) {
        User currentUser = authService.getCurrentUser();
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));

        if (!course.getIsPublished()) {
            throw new RuntimeException("Course is not published");
        }

        if (enrollmentRepository.existsByStudentAndCourse(currentUser, course)) {
            throw new RuntimeException("You are already enrolled in this course");
        }

        Enrollment enrollment = new Enrollment(currentUser, course);
        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);

        // Create lesson progress entries for all lessons
        List<Lesson> lessons = lessonRepository.findByCourseAndIsPublished(course, true);
        for (Lesson lesson : lessons) {
            LessonProgress lessonProgress = new LessonProgress(savedEnrollment, lesson);
            lessonProgressRepository.save(lessonProgress);
        }

        return convertToDto(savedEnrollment);
    }

    public void unenrollFromCourse(Long courseId) {
        User currentUser = authService.getCurrentUser();
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));

        Enrollment enrollment = enrollmentRepository.findByStudentAndCourse(currentUser, course)
                .orElseThrow(() -> new RuntimeException("You are not enrolled in this course"));

        enrollmentRepository.delete(enrollment);
    }

    public List<EnrollmentDto> getStudentEnrollments() {
        User currentUser = authService.getCurrentUser();
        return enrollmentRepository.findByStudentOrderByEnrolledAtDesc(currentUser).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<EnrollmentDto> getCourseEnrollments(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));

        User currentUser = authService.getCurrentUser();
        if (!currentUser.isAdmin() && !course.getInstructor().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You can only view enrollments for your own courses");
        }

        return enrollmentRepository.findByCourseOrderByEnrolledAtDesc(course).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public EnrollmentDto getEnrollment(Long courseId) {
        User currentUser = authService.getCurrentUser();
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));

        Enrollment enrollment = enrollmentRepository.findByStudentAndCourse(currentUser, course)
                .orElseThrow(() -> new RuntimeException("You are not enrolled in this course"));

        return convertToDto(enrollment);
    }

    public boolean isEnrolled(Long courseId) {
        try {
            User currentUser = authService.getCurrentUser();
            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));
            return enrollmentRepository.existsByStudentAndCourse(currentUser, course);
        } catch (Exception e) {
            return false;
        }
    }

    public long getEnrollmentCount(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));
        return enrollmentRepository.countByCourse(course);
    }

    private EnrollmentDto convertToDto(Enrollment enrollment) {
        EnrollmentDto dto = new EnrollmentDto(
                enrollment.getId(),
                enrollment.getStudent().getId(),
                enrollment.getStudent().getFullName(),
                enrollment.getCourse().getId(),
                enrollment.getCourse().getTitle(),
                enrollment.getEnrolledAt(),
                enrollment.getProgressPercentage(),
                enrollment.getLastAccessedAt(),
                enrollment.getIsCompleted(),
                enrollment.getCompletedAt()
        );
        dto.setCompletedLessonsCount(enrollment.getCompletedLessonsCount());
        dto.setTotalLessonsCount(enrollment.getTotalLessonsCount());
        return dto;
    }
}

