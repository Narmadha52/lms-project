package com.lms.service;

import com.lms.dto.CourseDto;
import com.lms.dto.LessonDto;
import com.lms.model.*;
import com.lms.repository.CourseRepository;
import com.lms.repository.LessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private AuthService authService;

    public List<CourseDto> getAllCourses() {
        return courseRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<CourseDto> getPublishedCourses() {
        return courseRepository.findByIsPublished(true).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<CourseDto> getCoursesByInstructor(Long instructorId) {
        User instructor = new User();
        instructor.setId(instructorId);
        return courseRepository.findByInstructor(instructor).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<CourseDto> getCoursesByCategory(String category) {
        return courseRepository.findByCategory(category).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<CourseDto> getCoursesByDifficulty(DifficultyLevel difficultyLevel) {
        return courseRepository.findByDifficultyLevel(difficultyLevel).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<CourseDto> getFreeCourses() {
        return courseRepository.findFreeCourses().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<CourseDto> getPaidCourses() {
        return courseRepository.findPaidCourses().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<CourseDto> searchCourses(String searchTerm) {
        return courseRepository.searchPublishedCourses(searchTerm).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<CourseDto> getCoursesByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return courseRepository.findByPriceRange(minPrice, maxPrice).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<CourseDto> getLatestCourses(Pageable pageable) {
        return courseRepository.findLatestPublishedCourses(pageable).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<CourseDto> getMostPopularCourses(Pageable pageable) {
        return courseRepository.findMostPopularCourses(pageable).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public CourseDto getCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));
        return convertToDtoWithLessons(course);
    }

    public CourseDto createCourse(CourseDto courseDto) {
        User currentUser = authService.getCurrentUser();
        
        if (!currentUser.isInstructor() && !currentUser.isAdmin()) {
            throw new RuntimeException("Only instructors can create courses");
        }

        Course course = new Course();
        course.setTitle(courseDto.getTitle());
        course.setDescription(courseDto.getDescription());
        course.setInstructor(currentUser);
        course.setCategory(courseDto.getCategory());
        course.setDifficultyLevel(courseDto.getDifficultyLevel());
        course.setPrice(courseDto.getPrice());
        course.setIsPublished(courseDto.getIsPublished());
        course.setThumbnailUrl(courseDto.getThumbnailUrl());

        Course savedCourse = courseRepository.save(course);
        return convertToDto(savedCourse);
    }

    public CourseDto updateCourse(Long id, CourseDto courseDto) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));

        User currentUser = authService.getCurrentUser();
        if (!currentUser.isAdmin() && !course.getInstructor().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You can only update your own courses");
        }

        course.setTitle(courseDto.getTitle());
        course.setDescription(courseDto.getDescription());
        course.setCategory(courseDto.getCategory());
        course.setDifficultyLevel(courseDto.getDifficultyLevel());
        course.setPrice(courseDto.getPrice());
        course.setIsPublished(courseDto.getIsPublished());
        course.setThumbnailUrl(courseDto.getThumbnailUrl());

        Course updatedCourse = courseRepository.save(course);
        return convertToDto(updatedCourse);
    }

    public void deleteCourse(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));

        User currentUser = authService.getCurrentUser();
        if (!currentUser.isAdmin() && !course.getInstructor().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You can only delete your own courses");
        }

        courseRepository.deleteById(id);
    }

    public CourseDto publishCourse(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));

        User currentUser = authService.getCurrentUser();
        if (!currentUser.isAdmin() && !course.getInstructor().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You can only publish your own courses");
        }

        course.setIsPublished(true);
        Course updatedCourse = courseRepository.save(course);
        return convertToDto(updatedCourse);
    }

    public CourseDto unpublishCourse(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));

        User currentUser = authService.getCurrentUser();
        if (!currentUser.isAdmin() && !course.getInstructor().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You can only unpublish your own courses");
        }

        course.setIsPublished(false);
        Course updatedCourse = courseRepository.save(course);
        return convertToDto(updatedCourse);
    }

    public long getCourseCount() {
        return courseRepository.count();
    }

    public long getCourseCountByInstructor(Long instructorId) {
        User instructor = new User();
        instructor.setId(instructorId);
        return courseRepository.countByInstructor(instructor);
    }

    public long getPublishedCourseCountByInstructor(Long instructorId) {
        User instructor = new User();
        instructor.setId(instructorId);
        return courseRepository.countByInstructorAndIsPublished(instructor, true);
    }

    private CourseDto convertToDto(Course course) {
        CourseDto dto = new CourseDto(
                course.getId(),
                course.getTitle(),
                course.getDescription(),
                course.getInstructor().getId(),
                course.getInstructor().getFullName(),
                course.getCategory(),
                course.getDifficultyLevel(),
                course.getPrice(),
                course.getIsPublished(),
                course.getThumbnailUrl(),
                course.getCreatedAt(),
                course.getUpdatedAt()
        );
        dto.setEnrollmentCount(course.getEnrollmentCount());
        dto.setLessonCount(course.getLessonCount());
        return dto;
    }

    private CourseDto convertToDtoWithLessons(Course course) {
        CourseDto dto = convertToDto(course);
        
        List<LessonDto> lessons = course.getLessons().stream()
                .map(lesson -> {
                    LessonDto lessonDto = new LessonDto(
                            lesson.getId(),
                            lesson.getCourse().getId(),
                            lesson.getTitle(),
                            lesson.getDescription(),
                            lesson.getLessonType(),
                            lesson.getContent(),
                            lesson.getFileUrl(),
                            lesson.getFileName(),
                            lesson.getFileSize(),
                            lesson.getDurationMinutes(),
                            lesson.getOrderIndex(),
                            lesson.getIsPublished(),
                            lesson.getCreatedAt(),
                            lesson.getUpdatedAt()
                    );
                    lessonDto.setFormattedDuration(lesson.getFormattedDuration());
                    return lessonDto;
                })
                .collect(Collectors.toList());
        
        dto.setLessons(lessons);
        return dto;
    }
}

