package com.lms.repository;

import com.lms.model.Enrollment;
import com.lms.model.Lesson;
import com.lms.model.LessonProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LessonProgressRepository extends JpaRepository<LessonProgress, Long> {
    
    Optional<LessonProgress> findByEnrollmentAndLesson(Enrollment enrollment, Lesson lesson);
    
    List<LessonProgress> findByEnrollment(Enrollment enrollment);
    
    List<LessonProgress> findByLesson(Lesson lesson);
    
    List<LessonProgress> findByEnrollmentOrderByLessonOrderIndexAsc(Enrollment enrollment);
    
    @Query("SELECT lp FROM LessonProgress lp WHERE lp.enrollment = :enrollment AND lp.isCompleted = :isCompleted")
    List<LessonProgress> findByEnrollmentAndIsCompleted(@Param("enrollment") Enrollment enrollment, @Param("isCompleted") Boolean isCompleted);
    
    @Query("SELECT lp FROM LessonProgress lp WHERE lp.lesson = :lesson AND lp.isCompleted = :isCompleted")
    List<LessonProgress> findByLessonAndIsCompleted(@Param("lesson") Lesson lesson, @Param("isCompleted") Boolean isCompleted);
    
    @Query("SELECT COUNT(lp) FROM LessonProgress lp WHERE lp.enrollment = :enrollment")
    long countByEnrollment(@Param("enrollment") Enrollment enrollment);
    
    @Query("SELECT COUNT(lp) FROM LessonProgress lp WHERE lp.enrollment = :enrollment AND lp.isCompleted = :isCompleted")
    long countByEnrollmentAndIsCompleted(@Param("enrollment") Enrollment enrollment, @Param("isCompleted") Boolean isCompleted);
    
    @Query("SELECT COUNT(lp) FROM LessonProgress lp WHERE lp.lesson = :lesson AND lp.isCompleted = :isCompleted")
    long countByLessonAndIsCompleted(@Param("lesson") Lesson lesson, @Param("isCompleted") Boolean isCompleted);
    
    @Query("SELECT lp FROM LessonProgress lp WHERE lp.enrollment = :enrollment AND lp.lesson.course = :courseId")
    List<LessonProgress> findByEnrollmentAndCourse(@Param("enrollment") Enrollment enrollment, @Param("courseId") Long courseId);
    
    @Query("SELECT lp FROM LessonProgress lp WHERE lp.enrollment.student.id = :studentId AND lp.lesson.course.id = :courseId")
    List<LessonProgress> findByStudentAndCourse(@Param("studentId") Long studentId, @Param("courseId") Long courseId);
    
    @Query("SELECT lp FROM LessonProgress lp WHERE lp.enrollment.student.id = :studentId AND lp.lesson.course.id = :courseId AND lp.isCompleted = :isCompleted")
    List<LessonProgress> findByStudentAndCourseAndIsCompleted(@Param("studentId") Long studentId, @Param("courseId") Long courseId, @Param("isCompleted") Boolean isCompleted);
    
    @Query("SELECT lp FROM LessonProgress lp WHERE lp.enrollment.student.id = :studentId ORDER BY lp.lastAccessedAt DESC")
    List<LessonProgress> findRecentlyAccessedByStudent(@Param("studentId") Long studentId);
    
    @Query("SELECT lp FROM LessonProgress lp WHERE lp.lesson.course.instructor.id = :instructorId")
    List<LessonProgress> findByInstructor(@Param("instructorId") Long instructorId);
    
    @Query("SELECT lp FROM LessonProgress lp WHERE lp.lesson.course.instructor.id = :instructorId AND lp.isCompleted = :isCompleted")
    List<LessonProgress> findByInstructorAndIsCompleted(@Param("instructorId") Long instructorId, @Param("isCompleted") Boolean isCompleted);
    
    @Query("SELECT SUM(lp.timeSpentMinutes) FROM LessonProgress lp WHERE lp.enrollment = :enrollment")
    Optional<Long> getTotalTimeSpentByEnrollment(@Param("enrollment") Enrollment enrollment);
    
    @Query("SELECT SUM(lp.timeSpentMinutes) FROM LessonProgress lp WHERE lp.enrollment.student.id = :studentId AND lp.lesson.course.id = :courseId")
    Optional<Long> getTotalTimeSpentByStudentAndCourse(@Param("studentId") Long studentId, @Param("courseId") Long courseId);
    
    @Query("SELECT AVG(lp.timeSpentMinutes) FROM LessonProgress lp WHERE lp.lesson = :lesson AND lp.isCompleted = true")
    Optional<Double> getAverageTimeSpentByLesson(@Param("lesson") Lesson lesson);
    
    @Query("SELECT lp FROM LessonProgress lp WHERE lp.enrollment = :enrollment AND lp.lesson.orderIndex = " +
           "(SELECT MIN(lp2.lesson.orderIndex) FROM LessonProgress lp2 WHERE lp2.enrollment = :enrollment AND lp2.isCompleted = false)")
    Optional<LessonProgress> findNextIncompleteLesson(@Param("enrollment") Enrollment enrollment);
    
    @Query("SELECT lp FROM LessonProgress lp WHERE lp.enrollment = :enrollment AND lp.lesson.orderIndex = " +
           "(SELECT MAX(lp2.lesson.orderIndex) FROM LessonProgress lp2 WHERE lp2.enrollment = :enrollment AND lp2.isCompleted = true)")
    Optional<LessonProgress> findLastCompletedLesson(@Param("enrollment") Enrollment enrollment);
    
    boolean existsByEnrollmentAndLesson(Enrollment enrollment, Lesson lesson);
}

