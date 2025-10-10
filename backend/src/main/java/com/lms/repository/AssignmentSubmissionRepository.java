package com.lms.repository;

import com.lms.model.Assignment;
import com.lms.model.AssignmentSubmission;
import com.lms.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface AssignmentSubmissionRepository extends JpaRepository<AssignmentSubmission, Long> {
    
    Optional<AssignmentSubmission> findByAssignmentAndStudent(Assignment assignment, User student);
    
    List<AssignmentSubmission> findByAssignment(Assignment assignment);
    
    List<AssignmentSubmission> findByStudent(User student);
    
    List<AssignmentSubmission> findByAssignmentOrderBySubmittedAtDesc(Assignment assignment);
    
    List<AssignmentSubmission> findByStudentOrderBySubmittedAtDesc(User student);
    
    @Query("SELECT s FROM AssignmentSubmission s WHERE s.assignment = :assignment AND s.grade IS NOT NULL")
    List<AssignmentSubmission> findGradedSubmissionsByAssignment(@Param("assignment") Assignment assignment);
    
    @Query("SELECT s FROM AssignmentSubmission s WHERE s.assignment = :assignment AND s.grade IS NULL")
    List<AssignmentSubmission> findUngradedSubmissionsByAssignment(@Param("assignment") Assignment assignment);
    
    @Query("SELECT s FROM AssignmentSubmission s WHERE s.student = :student AND s.grade IS NOT NULL")
    List<AssignmentSubmission> findGradedSubmissionsByStudent(@Param("student") User student);
    
    @Query("SELECT s FROM AssignmentSubmission s WHERE s.student = :student AND s.grade IS NULL")
    List<AssignmentSubmission> findUngradedSubmissionsByStudent(@Param("student") User student);
    
    @Query("SELECT s FROM AssignmentSubmission s WHERE s.assignment.course.instructor.id = :instructorId")
    List<AssignmentSubmission> findByInstructor(@Param("instructorId") Long instructorId);
    
    @Query("SELECT s FROM AssignmentSubmission s WHERE s.assignment.course.instructor.id = :instructorId AND s.grade IS NOT NULL")
    List<AssignmentSubmission> findGradedSubmissionsByInstructor(@Param("instructorId") Long instructorId);
    
    @Query("SELECT s FROM AssignmentSubmission s WHERE s.assignment.course.instructor.id = :instructorId AND s.grade IS NULL")
    List<AssignmentSubmission> findUngradedSubmissionsByInstructor(@Param("instructorId") Long instructorId);
    
    @Query("SELECT s FROM AssignmentSubmission s WHERE s.assignment.course.instructor.id = :instructorId AND s.gradedBy.id = :graderId")
    List<AssignmentSubmission> findByInstructorAndGrader(@Param("instructorId") Long instructorId, @Param("graderId") Long graderId);
    
    @Query("SELECT s FROM AssignmentSubmission s WHERE s.assignment = :assignment AND " +
           "(LOWER(s.student.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(s.student.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(s.student.username) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    List<AssignmentSubmission> searchSubmissionsByAssignment(@Param("assignment") Assignment assignment, @Param("searchTerm") String searchTerm);
    
    @Query("SELECT s FROM AssignmentSubmission s WHERE s.student = :student AND " +
           "(LOWER(s.assignment.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(s.assignment.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    List<AssignmentSubmission> searchSubmissionsByStudent(@Param("student") User student, @Param("searchTerm") String searchTerm);
    
    @Query("SELECT COUNT(s) FROM AssignmentSubmission s WHERE s.assignment = :assignment")
    long countByAssignment(@Param("assignment") Assignment assignment);
    
    @Query("SELECT COUNT(s) FROM AssignmentSubmission s WHERE s.assignment = :assignment AND s.grade IS NOT NULL")
    long countGradedSubmissionsByAssignment(@Param("assignment") Assignment assignment);
    
    @Query("SELECT COUNT(s) FROM AssignmentSubmission s WHERE s.assignment = :assignment AND s.grade IS NULL")
    long countUngradedSubmissionsByAssignment(@Param("assignment") Assignment assignment);
    
    @Query("SELECT COUNT(s) FROM AssignmentSubmission s WHERE s.student = :student")
    long countByStudent(@Param("student") User student);
    
    @Query("SELECT COUNT(s) FROM AssignmentSubmission s WHERE s.student = :student AND s.grade IS NOT NULL")
    long countGradedSubmissionsByStudent(@Param("student") User student);
    
    @Query("SELECT COUNT(s) FROM AssignmentSubmission s WHERE s.student = :student AND s.grade IS NULL")
    long countUngradedSubmissionsByStudent(@Param("student") User student);
    
    @Query("SELECT AVG(s.grade) FROM AssignmentSubmission s WHERE s.assignment = :assignment AND s.grade IS NOT NULL")
    Optional<BigDecimal> getAverageGradeByAssignment(@Param("assignment") Assignment assignment);
    
    @Query("SELECT AVG(s.grade) FROM AssignmentSubmission s WHERE s.student = :student AND s.grade IS NOT NULL")
    Optional<BigDecimal> getAverageGradeByStudent(@Param("student") User student);
    
    @Query("SELECT AVG(s.grade) FROM AssignmentSubmission s WHERE s.assignment.course = :courseId AND s.grade IS NOT NULL")
    Optional<BigDecimal> getAverageGradeByCourse(@Param("courseId") Long courseId);
    
    @Query("SELECT s FROM AssignmentSubmission s WHERE s.assignment = :assignment AND s.submittedAt > s.assignment.dueDate")
    List<AssignmentSubmission> findLateSubmissionsByAssignment(@Param("assignment") Assignment assignment);
    
    @Query("SELECT s FROM AssignmentSubmission s WHERE s.student = :student AND s.submittedAt > s.assignment.dueDate")
    List<AssignmentSubmission> findLateSubmissionsByStudent(@Param("student") User student);
    
    @Query("SELECT s FROM AssignmentSubmission s WHERE s.assignment.course.instructor.id = :instructorId AND s.submittedAt > s.assignment.dueDate")
    List<AssignmentSubmission> findLateSubmissionsByInstructor(@Param("instructorId") Long instructorId);
    
    @Query("SELECT s FROM AssignmentSubmission s WHERE s.assignment = :assignment ORDER BY s.grade DESC")
    List<AssignmentSubmission> findByAssignmentOrderByGradeDesc(@Param("assignment") Assignment assignment);
    
    @Query("SELECT s FROM AssignmentSubmission s WHERE s.assignment = :assignment ORDER BY s.grade ASC")
    List<AssignmentSubmission> findByAssignmentOrderByGradeAsc(@Param("assignment") Assignment assignment);
    
    @Query("SELECT s FROM AssignmentSubmission s WHERE s.assignment = :assignment AND s.grade >= :minGrade ORDER BY s.grade DESC")
    List<AssignmentSubmission> findByAssignmentAndMinGrade(@Param("assignment") Assignment assignment, @Param("minGrade") BigDecimal minGrade);
    
    @Query("SELECT s FROM AssignmentSubmission s WHERE s.assignment = :assignment AND s.grade <= :maxGrade ORDER BY s.grade DESC")
    List<AssignmentSubmission> findByAssignmentAndMaxGrade(@Param("assignment") Assignment assignment, @Param("maxGrade") BigDecimal maxGrade);
    
    boolean existsByAssignmentAndStudent(Assignment assignment, User student);
}

