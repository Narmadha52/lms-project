package com.lms.repository;

import com.lms.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByName(String name);

    @Query("SELECT c FROM Category c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Category> searchByName(@Param("searchTerm") String searchTerm);

    @Query("SELECT c FROM Category c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(c.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Category> searchByNameOrDescription(@Param("searchTerm") String searchTerm);

    @Query("SELECT c FROM Category c ORDER BY c.name ASC")
    List<Category> findAllOrderByName();

    @Query("SELECT c FROM Category c ORDER BY SIZE(c.courses) DESC")
    List<Category> findAllOrderByCourseCount();

    @Query("SELECT c FROM Category c WHERE SIZE(c.courses) > 0 ORDER BY c.name ASC")
    List<Category> findCategoriesWithCourses();

    @Query("SELECT c FROM Category c WHERE SIZE(c.courses) = 0 ORDER BY c.name ASC")
    List<Category> findEmptyCategories();

    @Query("SELECT COUNT(c) FROM Category c")
    long countAll();

    @Query("SELECT COUNT(c) FROM Category c WHERE SIZE(c.courses) > 0")
    long countCategoriesWithCourses();

    @Query("SELECT COUNT(c) FROM Category c WHERE SIZE(c.courses) = 0")
    long countEmptyCategories();

    @Query("SELECT DISTINCT c FROM Category c JOIN c.courses co WHERE co.isPublished = true")
    List<Category> findCategoriesWithPublishedCourses();

    @Query("SELECT c FROM Category c JOIN c.courses co WHERE co.isPublished = true " +
            "GROUP BY c ORDER BY COUNT(co) DESC")
    List<Category> findCategoriesWithPublishedCoursesOrderByCount();

    boolean existsByName(String name);
}