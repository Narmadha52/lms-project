package com.lms.repository;

import com.lms.model.Course;
import com.lms.model.DifficultyLevel;
import com.lms.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    
    List<Course> findByInstructor(User instructor);
    
    List<Course> findByInstructorAndIsPublished(User instructor, Boolean isPublished);
    
    List<Course> findByIsPublished(Boolean isPublished);
    
    List<Course> findByCategory(String category);
    
    List<Course> findByDifficultyLevel(DifficultyLevel difficultyLevel);
    
    List<Course> findByIsPublishedAndCategory(Boolean isPublished, String category);
    
    List<Course> findByIsPublishedAndDifficultyLevel(Boolean isPublished, DifficultyLevel difficultyLevel);
    
    @Query("SELECT c FROM Course c WHERE c.isPublished = true AND " +
           "(LOWER(c.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(c.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(c.category) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    List<Course> searchPublishedCourses(@Param("searchTerm") String searchTerm);
    
    @Query("SELECT c FROM Course c WHERE c.isPublished = true AND " +
           "c.price BETWEEN :minPrice AND :maxPrice")
    List<Course> findByPriceRange(@Param("minPrice") BigDecimal minPrice, @Param("maxPrice") BigDecimal maxPrice);
    
   @Query("SELECT c FROM Course c WHERE c.isPublished = true AND c.price = 0.00")
List<Course> findFreeCourses();

    
    @Query("SELECT c FROM Course c WHERE c.isPublished = true AND c.price > 0")
    List<Course> findPaidCourses();
    
    @Query("SELECT c FROM Course c WHERE c.instructor = :instructor AND " +
           "(LOWER(c.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(c.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    List<Course> searchCoursesByInstructor(@Param("instructor") User instructor, @Param("searchTerm") String searchTerm);
    
    @Query("SELECT c FROM Course c WHERE c.isPublished = true ORDER BY c.createdAt DESC")
    List<Course> findLatestPublishedCourses(Pageable pageable);
    
    @Query("SELECT c FROM Course c WHERE c.isPublished = true ORDER BY SIZE(c.enrollments) DESC")
    List<Course> findMostPopularCourses(Pageable pageable);
    
    @Query("SELECT c FROM Course c WHERE c.isPublished = true AND c.difficultyLevel = :difficulty ORDER BY c.createdAt DESC")
    List<Course> findCoursesByDifficulty(@Param("difficulty") DifficultyLevel difficulty, Pageable pageable);
    
    @Query("SELECT c FROM Course c WHERE c.isPublished = true AND c.category = :category ORDER BY c.createdAt DESC")
    List<Course> findCoursesByCategory(@Param("category") String category, Pageable pageable);
    
    @Query("SELECT COUNT(c) FROM Course c WHERE c.instructor = :instructor")
    long countByInstructor(@Param("instructor") User instructor);
    
    @Query("SELECT COUNT(c) FROM Course c WHERE c.instructor = :instructor AND c.isPublished = :isPublished")
    long countByInstructorAndIsPublished(@Param("instructor") User instructor, @Param("isPublished") Boolean isPublished);
    
    @Query("SELECT c FROM Course c WHERE c.isPublished = true AND " +
           "c.id IN (SELECT e.course.id FROM Enrollment e WHERE e.student.id = :studentId)")
    List<Course> findEnrolledCoursesByStudent(@Param("studentId") Long studentId);
    
    @Query("SELECT c FROM Course c WHERE c.isPublished = true AND " +
           "c.id NOT IN (SELECT e.course.id FROM Enrollment e WHERE e.student.id = :studentId)")
    List<Course> findUnenrolledCoursesByStudent(@Param("studentId") Long studentId);
    
    @Query("SELECT c FROM Course c WHERE c.isPublished = true AND " +
           "(LOWER(c.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(c.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(c.category) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) AND " +
           "c.id NOT IN (SELECT e.course.id FROM Enrollment e WHERE e.student.id = :studentId)")
    List<Course> searchUnenrolledCoursesByStudent(@Param("studentId") Long studentId, @Param("searchTerm") String searchTerm);
}

