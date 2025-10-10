package com.lms.repository;

import com.lms.model.Assignment;
import com.lms.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    
    List<Assignment> findByCourse(Course course);
    
    List<Assignment> findByCourseAndIsPublished(Course course, Boolean isPublished);
    
    List<Assignment> findByCourseOrderByCreatedAtDesc(Course course);
    
    List<Assignment> findByCourseAndIsPublishedOrderByCreatedAtDesc(Course course, Boolean isPublished);
    
    @Query("SELECT a FROM Assignment a WHERE a.course = :course AND a.dueDate > :now ORDER BY a.dueDate ASC")
    List<Assignment> findUpcomingAssignmentsByCourse(@Param("course") Course course, @Param("now") LocalDateTime now);
    
    @Query("SELECT a FROM Assignment a WHERE a.course = :course AND a.dueDate < :now ORDER BY a.dueDate DESC")
    List<Assignment> findPastAssignmentsByCourse(@Param("course") Course course, @Param("now") LocalDateTime now);
    
    @Query("SELECT a FROM Assignment a WHERE a.course = :course AND a.dueDate BETWEEN :startDate AND :endDate ORDER BY a.dueDate ASC")
    List<Assignment> findAssignmentsByCourseAndDateRange(@Param("course") Course course, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT a FROM Assignment a WHERE a.course = :course AND " +
           "(LOWER(a.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(a.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    List<Assignment> searchAssignmentsByCourse(@Param("course") Course course, @Param("searchTerm") String searchTerm);
    
    @Query("SELECT a FROM Assignment a WHERE a.course = :course AND " +
           "(LOWER(a.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(a.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) AND " +
           "a.isPublished = :isPublished")
    List<Assignment> searchAssignmentsByCourseAndPublished(@Param("course") Course course, @Param("searchTerm") String searchTerm, @Param("isPublished") Boolean isPublished);
    
    @Query("SELECT a FROM Assignment a WHERE a.course.instructor.id = :instructorId")
    List<Assignment> findByInstructor(@Param("instructorId") Long instructorId);
    
    @Query("SELECT a FROM Assignment a WHERE a.course.instructor.id = :instructorId AND a.isPublished = :isPublished")
    List<Assignment> findByInstructorAndIsPublished(@Param("instructorId") Long instructorId, @Param("isPublished") Boolean isPublished);
    
    @Query("SELECT a FROM Assignment a WHERE a.course.instructor.id = :instructorId AND a.dueDate > :now ORDER BY a.dueDate ASC")
    List<Assignment> findUpcomingAssignmentsByInstructor(@Param("instructorId") Long instructorId, @Param("now") LocalDateTime now);
    
    @Query("SELECT a FROM Assignment a WHERE a.course.instructor.id = :instructorId AND a.dueDate < :now ORDER BY a.dueDate DESC")
    List<Assignment> findPastAssignmentsByInstructor(@Param("instructorId") Long instructorId, @Param("now") LocalDateTime now);
    
    @Query("SELECT COUNT(a) FROM Assignment a WHERE a.course = :course")
    long countByCourse(@Param("course") Course course);
    
    @Query("SELECT COUNT(a) FROM Assignment a WHERE a.course = :course AND a.isPublished = :isPublished")
    long countByCourseAndIsPublished(@Param("course") Course course, @Param("isPublished") Boolean isPublished);
    
    @Query("SELECT COUNT(a) FROM Assignment a WHERE a.course.instructor.id = :instructorId")
    long countByInstructor(@Param("instructorId") Long instructorId);
    
    @Query("SELECT COUNT(a) FROM Assignment a WHERE a.course.instructor.id = :instructorId AND a.isPublished = :isPublished")
    long countByInstructorAndIsPublished(@Param("instructorId") Long instructorId, @Param("isPublished") Boolean isPublished);
    
    @Query("SELECT a FROM Assignment a WHERE a.dueDate BETWEEN :startDate AND :endDate ORDER BY a.dueDate ASC")
    List<Assignment> findAssignmentsByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT a FROM Assignment a WHERE a.dueDate < :now AND a.isPublished = true ORDER BY a.dueDate DESC")
    List<Assignment> findOverdueAssignments(@Param("now") LocalDateTime now);
    
    @Query("SELECT a FROM Assignment a WHERE a.dueDate BETWEEN :now AND :oneDayFromNow AND a.isPublished = true ORDER BY a.dueDate ASC")
    List<Assignment> findAssignmentsDueSoon(@Param("now") LocalDateTime now, @Param("oneDayFromNow") LocalDateTime oneDayFromNow);
}

