package com.lms.repository;

import com.lms.model.Course;
import com.lms.model.Enrollment;
import com.lms.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    
    Optional<Enrollment> findByStudentAndCourse(User student, Course course);
    
    List<Enrollment> findByStudent(User student);
    
    List<Enrollment> findByCourse(Course course);
    
    List<Enrollment> findByStudentOrderByEnrolledAtDesc(User student);
    
    List<Enrollment> findByCourseOrderByEnrolledAtDesc(Course course);
    
    @Query("SELECT e FROM Enrollment e WHERE e.student = :student AND e.isCompleted = :isCompleted")
    List<Enrollment> findByStudentAndIsCompleted(@Param("student") User student, @Param("isCompleted") Boolean isCompleted);
    
    @Query("SELECT e FROM Enrollment e WHERE e.course = :course AND e.isCompleted = :isCompleted")
    List<Enrollment> findByCourseAndIsCompleted(@Param("course") Course course, @Param("isCompleted") Boolean isCompleted);
    
    @Query("SELECT e FROM Enrollment e WHERE e.student = :student AND e.progressPercentage >= :minProgress")
    List<Enrollment> findByStudentAndMinProgress(@Param("student") User student, @Param("minProgress") BigDecimal minProgress);
    
    @Query("SELECT e FROM Enrollment e WHERE e.course = :course AND e.progressPercentage >= :minProgress")
    List<Enrollment> findByCourseAndMinProgress(@Param("course") Course course, @Param("minProgress") BigDecimal minProgress);
    
    @Query("SELECT e FROM Enrollment e WHERE e.student = :student AND " +
           "(LOWER(e.course.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(e.course.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    List<Enrollment> searchEnrollmentsByStudent(@Param("student") User student, @Param("searchTerm") String searchTerm);
    
    @Query("SELECT e FROM Enrollment e WHERE e.course = :course AND " +
           "(LOWER(e.student.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(e.student.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(e.student.username) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(e.student.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    List<Enrollment> searchEnrollmentsByCourse(@Param("course") Course course, @Param("searchTerm") String searchTerm);
    
    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.student = :student")
    long countByStudent(@Param("student") User student);
    
    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.course = :course")
    long countByCourse(@Param("course") Course course);
    
    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.student = :student AND e.isCompleted = :isCompleted")
    long countByStudentAndIsCompleted(@Param("student") User student, @Param("isCompleted") Boolean isCompleted);
    
    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.course = :course AND e.isCompleted = :isCompleted")
    long countByCourseAndIsCompleted(@Param("course") Course course, @Param("isCompleted") Boolean isCompleted);
    
    @Query("SELECT AVG(e.progressPercentage) FROM Enrollment e WHERE e.course = :course")
    Optional<BigDecimal> getAverageProgressByCourse(@Param("course") Course course);
    
    @Query("SELECT AVG(e.progressPercentage) FROM Enrollment e WHERE e.student = :student")
    Optional<BigDecimal> getAverageProgressByStudent(@Param("student") User student);
    
    @Query("SELECT e FROM Enrollment e WHERE e.student = :student AND e.course.instructor = :instructor")
    List<Enrollment> findByStudentAndInstructor(@Param("student") User student, @Param("instructor") User instructor);
    
    @Query("SELECT e FROM Enrollment e WHERE e.course.instructor = :instructor")
    List<Enrollment> findByInstructor(@Param("instructor") User instructor);
    
    @Query("SELECT e FROM Enrollment e WHERE e.course.instructor = :instructor AND e.isCompleted = :isCompleted")
    List<Enrollment> findByInstructorAndIsCompleted(@Param("instructor") User instructor, @Param("isCompleted") Boolean isCompleted);
    
    @Query("SELECT e FROM Enrollment e WHERE e.student = :student AND e.lastAccessedAt IS NOT NULL ORDER BY e.lastAccessedAt DESC")
    List<Enrollment> findRecentlyAccessedByStudent(@Param("student") User student);
    
    @Query("SELECT e FROM Enrollment e WHERE e.course = :course ORDER BY e.progressPercentage DESC")
    List<Enrollment> findByCourseOrderByProgressDesc(@Param("course") Course course);
    
    @Query("SELECT e FROM Enrollment e WHERE e.student = :student ORDER BY e.progressPercentage DESC")
    List<Enrollment> findByStudentOrderByProgressDesc(@Param("student") User student);
    
    boolean existsByStudentAndCourse(User student, Course course);
}

