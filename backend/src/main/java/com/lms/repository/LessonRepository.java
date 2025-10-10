package com.lms.repository;

import com.lms.model.Course;
import com.lms.model.Lesson;
import com.lms.model.LessonType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    
    List<Lesson> findByCourse(Course course);
    
    List<Lesson> findByCourseAndIsPublished(Course course, Boolean isPublished);
    
    List<Lesson> findByCourseOrderByOrderIndexAsc(Course course);
    
    List<Lesson> findByCourseAndIsPublishedOrderByOrderIndexAsc(Course course, Boolean isPublished);
    
    List<Lesson> findByLessonType(LessonType lessonType);
    
    List<Lesson> findByCourseAndLessonType(Course course, LessonType lessonType);
    
    @Query("SELECT l FROM Lesson l WHERE l.course = :course AND l.orderIndex = :orderIndex")
    Optional<Lesson> findByCourseAndOrderIndex(@Param("course") Course course, @Param("orderIndex") Integer orderIndex);
    
    @Query("SELECT MAX(l.orderIndex) FROM Lesson l WHERE l.course = :course")
    Optional<Integer> findMaxOrderIndexByCourse(@Param("course") Course course);
    
    @Query("SELECT l FROM Lesson l WHERE l.course = :course AND " +
           "(LOWER(l.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(l.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    List<Lesson> searchLessonsByCourse(@Param("course") Course course, @Param("searchTerm") String searchTerm);
    
    @Query("SELECT l FROM Lesson l WHERE l.course = :course AND " +
           "(LOWER(l.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(l.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) AND " +
           "l.isPublished = :isPublished")
    List<Lesson> searchLessonsByCourseAndPublished(@Param("course") Course course, @Param("searchTerm") String searchTerm, @Param("isPublished") Boolean isPublished);
    
    @Query("SELECT COUNT(l) FROM Lesson l WHERE l.course = :course")
    long countByCourse(@Param("course") Course course);
    
    @Query("SELECT COUNT(l) FROM Lesson l WHERE l.course = :course AND l.isPublished = :isPublished")
    long countByCourseAndIsPublished(@Param("course") Course course, @Param("isPublished") Boolean isPublished);
    
    @Query("SELECT COUNT(l) FROM Lesson l WHERE l.course = :course AND l.lessonType = :lessonType")
    long countByCourseAndLessonType(@Param("course") Course course, @Param("lessonType") LessonType lessonType);
    
    @Query("SELECT l FROM Lesson l WHERE l.course = :course AND l.orderIndex > :orderIndex ORDER BY l.orderIndex ASC")
    List<Lesson> findLessonsAfterOrderIndex(@Param("course") Course course, @Param("orderIndex") Integer orderIndex);
    
    @Query("SELECT l FROM Lesson l WHERE l.course = :course AND l.orderIndex < :orderIndex ORDER BY l.orderIndex DESC")
    List<Lesson> findLessonsBeforeOrderIndex(@Param("course") Course course, @Param("orderIndex") Integer orderIndex);
    
    @Query("SELECT l FROM Lesson l WHERE l.course = :course AND l.isPublished = true AND l.orderIndex = " +
           "(SELECT MIN(l2.orderIndex) FROM Lesson l2 WHERE l2.course = :course AND l2.isPublished = true)")
    Optional<Lesson> findFirstPublishedLessonByCourse(@Param("course") Course course);
    
    @Query("SELECT l FROM Lesson l WHERE l.course = :course AND l.isPublished = true AND l.orderIndex = " +
           "(SELECT MAX(l2.orderIndex) FROM Lesson l2 WHERE l2.course = :course AND l2.isPublished = true)")
    Optional<Lesson> findLastPublishedLessonByCourse(@Param("course") Course course);
}

