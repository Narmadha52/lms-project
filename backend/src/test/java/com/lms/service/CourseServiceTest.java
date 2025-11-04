package com.lms.service;

import com.lms.dto.CourseDto;
import com.lms.model.*;
import com.lms.repository.CourseRepository;
import com.lms.repository.LessonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("CourseService Tests")
class CourseServiceTest {

    @InjectMocks
    private CourseService courseService;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private LessonRepository lessonRepository;

    @Mock
    private AuthService authService;

    private User instructor;
    private User anotherInstructor;
    private User student;
    private User admin;
    private Course publishedCourse;
    private Course unpublishedCourse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        setupTestData();
    }

    private void setupTestData() {
        instructor = new User();
        instructor.setId(1L);
        instructor.setRole(Role.INSTRUCTOR);
        instructor.setIsApproved(true);

        anotherInstructor = new User();
        anotherInstructor.setId(2L);
        anotherInstructor.setRole(Role.INSTRUCTOR);
        anotherInstructor.setIsApproved(true);

        student = new User();
        student.setId(3L);
        student.setRole(Role.STUDENT);
        student.setIsApproved(true);

        admin = new User();
        admin.setId(4L);
        admin.setRole(Role.ADMIN);
        admin.setIsApproved(true);

        publishedCourse = new Course();
        publishedCourse.setId(10L);
        publishedCourse.setTitle("Java Basics");
        publishedCourse.setDescription("Intro to Java");
        publishedCourse.setInstructor(instructor);
        publishedCourse.setCategory("Programming");
        publishedCourse.setDifficultyLevel(DifficultyLevel.BEGINNER);
        publishedCourse.setPrice(BigDecimal.valueOf(100));
        publishedCourse.setIsPublished(true);

        unpublishedCourse = new Course();
        unpublishedCourse.setId(20L);
        unpublishedCourse.setTitle("Advanced Spring");
        unpublishedCourse.setDescription("Spring Framework Advanced");
        unpublishedCourse.setInstructor(instructor);
        unpublishedCourse.setCategory("Backend");
        unpublishedCourse.setDifficultyLevel(DifficultyLevel.ADVANCED);
        unpublishedCourse.setPrice(BigDecimal.valueOf(200));
        unpublishedCourse.setIsPublished(false);
    }

    // ==================== GET ALL COURSES TESTS ====================

    @Test
    @DisplayName("Should get all courses successfully")
    void getAllCourses_WhenCalled_ShouldReturnAllCourses() {
        // Arrange
        when(courseRepository.findAll()).thenReturn(Arrays.asList(publishedCourse, unpublishedCourse));

        // Act
        List<CourseDto> result = courseService.getAllCourses();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Java Basics", result.get(0).getTitle());
        assertEquals("Advanced Spring", result.get(1).getTitle());

        verify(courseRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return empty list when no courses exist")
    void getAllCourses_WhenNoCourses_ShouldReturnEmptyList() {
        // Arrange
        when(courseRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<CourseDto> result = courseService.getAllCourses();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(courseRepository, times(1)).findAll();
    }

    // ==================== GET PUBLISHED COURSES TESTS ====================

    @Test
    @DisplayName("Should get only published courses")
    void getPublishedCourses_WhenCalled_ShouldReturnOnlyPublishedCourses() {
        // Arrange
        when(courseRepository.findByIsPublished(true)).thenReturn(Arrays.asList(publishedCourse));

        // Act
        List<CourseDto> result = courseService.getPublishedCourses();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).getIsPublished());
        assertEquals("Java Basics", result.get(0).getTitle());

        verify(courseRepository, times(1)).findByIsPublished(true);
    }

    @Test
    @DisplayName("Should return empty list when no published courses")
    void getPublishedCourses_WhenNoPublished_ShouldReturnEmptyList() {
        // Arrange
        when(courseRepository.findByIsPublished(true)).thenReturn(Collections.emptyList());

        // Act
        List<CourseDto> result = courseService.getPublishedCourses();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // ==================== GET COURSE BY ID TESTS ====================

    @Test
    @DisplayName("Should get course by ID successfully")
    void getCourseById_WhenCourseExists_ShouldReturnCourse() {
        // Arrange
        when(courseRepository.findById(10L)).thenReturn(Optional.of(publishedCourse));

        // Act
        CourseDto result = courseService.getCourseById(10L);

        // Assert
        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals("Java Basics", result.getTitle());

        verify(courseRepository, times(1)).findById(10L);
    }

    @Test
    @DisplayName("Should handle course not found by ID")
    void getCourseById_WhenCourseNotFound_ShouldHandleError() {
        // Arrange
        when(courseRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        try {
            courseService.getCourseById(999L);
            fail("Expected RuntimeException to be thrown");
        } catch (RuntimeException ex) {
            assertTrue(ex.getMessage().contains("not found"));
        }

        verify(courseRepository, times(1)).findById(999L);
    }

    // ==================== CREATE COURSE TESTS ====================

    @Test
    @DisplayName("Should create course successfully by instructor")
    void createCourse_WhenInstructor_ShouldCreateCourse() {
        // Arrange
        when(authService.getCurrentUser()).thenReturn(instructor);
        when(courseRepository.save(any(Course.class))).thenAnswer(invocation -> {
            Course c = invocation.getArgument(0);
            c.setId(30L);
            return c;
        });

        CourseDto dto = new CourseDto();
        dto.setTitle("Spring Boot");
        dto.setDescription("Learn Spring Boot");
        dto.setCategory("Backend");
        dto.setDifficultyLevel(DifficultyLevel.INTERMEDIATE);
        dto.setPrice(BigDecimal.valueOf(150));
        dto.setIsPublished(false);
        dto.setThumbnailUrl("image.jpg");

        // Act
        CourseDto result = courseService.createCourse(dto);

        // Assert
        assertNotNull(result);
        assertEquals("Spring Boot", result.getTitle());
        assertEquals("Backend", result.getCategory());
        assertFalse(result.getIsPublished());

        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    @DisplayName("Should create course successfully by admin")
    void createCourse_WhenAdmin_ShouldCreateCourse() {
        // Arrange
        when(authService.getCurrentUser()).thenReturn(admin);
        when(courseRepository.save(any(Course.class))).thenAnswer(invocation -> {
            Course c = invocation.getArgument(0);
            c.setId(40L);
            return c;
        });

        CourseDto dto = new CourseDto();
        dto.setTitle("Admin Course");
        dto.setDescription("Admin created course");
        dto.setCategory("Management");
        dto.setDifficultyLevel(DifficultyLevel.BEGINNER);
        dto.setPrice(BigDecimal.valueOf(50));

        // Act
        CourseDto result = courseService.createCourse(dto);

        // Assert
        assertNotNull(result);
        assertEquals("Admin Course", result.getTitle());
        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    @DisplayName("Should handle create course by student error")
    void createCourse_WhenStudent_ShouldHandleError() {
        // Arrange
        when(authService.getCurrentUser()).thenReturn(student);

        CourseDto dto = new CourseDto();
        dto.setTitle("Unauthorized Course");

        // Act & Assert
        try {
            courseService.createCourse(dto);
            fail("Expected RuntimeException to be thrown");
        } catch (RuntimeException ex) {
            assertTrue(ex.getMessage().contains("Only instructors") || 
                      ex.getMessage().contains("not authorized"));
        }

        verify(courseRepository, never()).save(any());
    }

    // ==================== UPDATE COURSE TESTS ====================

    @Test
    @DisplayName("Should update course successfully by owner")
    void updateCourse_WhenOwner_ShouldUpdateCourse() {
        // Arrange
        when(courseRepository.findById(10L)).thenReturn(Optional.of(publishedCourse));
        when(authService.getCurrentUser()).thenReturn(instructor);
        when(courseRepository.save(any(Course.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CourseDto dto = new CourseDto();
        dto.setTitle("Updated Java Basics");
        dto.setDescription("Updated Description");
        dto.setPrice(BigDecimal.valueOf(120));

        // Act
        CourseDto result = courseService.updateCourse(10L, dto);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Java Basics", result.getTitle());
        assertEquals("Updated Description", result.getDescription());

        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    @DisplayName("Should update course successfully by admin")
    void updateCourse_WhenAdmin_ShouldUpdateCourse() {
        // Arrange
        when(courseRepository.findById(10L)).thenReturn(Optional.of(publishedCourse));
        when(authService.getCurrentUser()).thenReturn(admin);
        when(courseRepository.save(any(Course.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CourseDto dto = new CourseDto();
        dto.setTitle("Admin Updated");

        // Act
        CourseDto result = courseService.updateCourse(10L, dto);

        // Assert
        assertNotNull(result);
        assertEquals("Admin Updated", result.getTitle());
        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    @DisplayName("Should handle update course by non-owner error")
    void updateCourse_WhenNotOwner_ShouldHandleError() {
        // Arrange
        when(courseRepository.findById(10L)).thenReturn(Optional.of(publishedCourse));
        when(authService.getCurrentUser()).thenReturn(anotherInstructor);

        CourseDto dto = new CourseDto();
        dto.setTitle("Invalid Update");

        // Act & Assert
        try {
            courseService.updateCourse(10L, dto);
            fail("Expected RuntimeException to be thrown");
        } catch (RuntimeException ex) {
            assertTrue(ex.getMessage().contains("not authorized") || 
                      ex.getMessage().contains("permission"));
        }

        verify(courseRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should handle update course not found")
    void updateCourse_WhenCourseNotFound_ShouldHandleError() {
        // Arrange
        when(courseRepository.findById(999L)).thenReturn(Optional.empty());

        CourseDto dto = new CourseDto();

        // Act & Assert
        try {
            courseService.updateCourse(999L, dto);
            fail("Expected RuntimeException to be thrown");
        } catch (RuntimeException ex) {
            assertTrue(ex.getMessage().contains("not found"));
        }

        verify(courseRepository, never()).save(any());
    }

    // ==================== DELETE COURSE TESTS ====================

    @Test
    @DisplayName("Should delete course successfully by admin")
    void deleteCourse_WhenAdmin_ShouldDeleteCourse() {
        // Arrange
        when(courseRepository.findById(10L)).thenReturn(Optional.of(publishedCourse));
        when(authService.getCurrentUser()).thenReturn(admin);
        doNothing().when(courseRepository).deleteById(10L);

        // Act
        courseService.deleteCourse(10L);

        // Assert
        verify(courseRepository, times(1)).deleteById(10L);
    }

    @Test
    @DisplayName("Should delete course successfully by owner")
    void deleteCourse_WhenOwner_ShouldDeleteCourse() {
        // Arrange
        when(courseRepository.findById(10L)).thenReturn(Optional.of(publishedCourse));
        when(authService.getCurrentUser()).thenReturn(instructor);
        doNothing().when(courseRepository).deleteById(10L);

        // Act
        courseService.deleteCourse(10L);

        // Assert
        verify(courseRepository, times(1)).deleteById(10L);
    }

    @Test
    @DisplayName("Should handle delete course by non-authorized user")
    void deleteCourse_WhenNotAuthorized_ShouldHandleError() {
        // Arrange
        when(courseRepository.findById(10L)).thenReturn(Optional.of(publishedCourse));
        when(authService.getCurrentUser()).thenReturn(anotherInstructor);

        // Act & Assert
        try {
            courseService.deleteCourse(10L);
            fail("Expected RuntimeException to be thrown");
        } catch (RuntimeException ex) {
            assertTrue(ex.getMessage().contains("not authorized") || 
                      ex.getMessage().contains("permission"));
        }

        verify(courseRepository, never()).deleteById(any());
    }

    // ==================== PUBLISH/UNPUBLISH TESTS ====================

    @Test
    @DisplayName("Should publish course successfully")
    void publishCourse_WhenOwner_ShouldPublishCourse() {
        // Arrange
        when(courseRepository.findById(20L)).thenReturn(Optional.of(unpublishedCourse));
        when(authService.getCurrentUser()).thenReturn(instructor);
        when(courseRepository.save(any(Course.class))).thenAnswer(invocation -> {
            Course c = invocation.getArgument(0);
            c.setIsPublished(true);
            return c;
        });

        // Act
        CourseDto result = courseService.publishCourse(20L);

        // Assert
        assertNotNull(result);
        assertTrue(result.getIsPublished());

        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    @DisplayName("Should unpublish course successfully")
    void unpublishCourse_WhenOwner_ShouldUnpublishCourse() {
        // Arrange
        when(courseRepository.findById(10L)).thenReturn(Optional.of(publishedCourse));
        when(authService.getCurrentUser()).thenReturn(instructor);
        when(courseRepository.save(any(Course.class))).thenAnswer(invocation -> {
            Course c = invocation.getArgument(0);
            c.setIsPublished(false);
            return c;
        });

        // Act
        CourseDto result = courseService.unpublishCourse(10L);

        // Assert
        assertNotNull(result);
        assertFalse(result.getIsPublished());

        verify(courseRepository, times(1)).save(any(Course.class));
    }

    // ==================== COUNT AND QUERY TESTS ====================

    @Test
    @DisplayName("Should get course count")
    void getCourseCount_WhenCalled_ShouldReturnCount() {
        // Arrange
        when(courseRepository.count()).thenReturn(5L);

        // Act
        long count = courseService.getCourseCount();

        // Assert
        assertEquals(5L, count);
        verify(courseRepository, times(1)).count();
    }

    @Test
    @DisplayName("Should get latest courses")
    void getLatestCourses_WhenCalled_ShouldReturnLatestCourses() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 5);
        when(courseRepository.findLatestPublishedCourses(pageable))
                .thenReturn(Arrays.asList(publishedCourse));

        // Act
        List<CourseDto> result = courseService.getLatestCourses(pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Java Basics", result.get(0).getTitle());

        verify(courseRepository, times(1)).findLatestPublishedCourses(pageable);
    }

    @Test
    @DisplayName("Should get courses by category")
    void getCoursesByCategory_WhenCalled_ShouldReturnCoursesByCategory() {
        // Arrange
        when(courseRepository.findByCategory("Programming"))
                .thenReturn(Arrays.asList(publishedCourse));

        // Act
        List<CourseDto> result = courseService.getCoursesByCategory("Programming");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Programming", result.get(0).getCategory());
    }

    @Test
    @DisplayName("Should get courses by difficulty level")
    void getCoursesByDifficultyLevel_WhenCalled_ShouldReturnCourses() {
        // Arrange
        when(courseRepository.findByDifficultyLevel(DifficultyLevel.BEGINNER))
                .thenReturn(Arrays.asList(publishedCourse));

        // Act
        List<CourseDto> result = courseService.getCoursesByDifficulty(DifficultyLevel.BEGINNER);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(DifficultyLevel.BEGINNER, result.get(0).getDifficultyLevel());
    }

    @Test
    @DisplayName("Should get courses by instructor")
    void getCoursesByInstructor_WhenCalled_ShouldReturnCourses() {
        // Arrange
        when(courseRepository.findByInstructor(instructor))
                .thenReturn(Arrays.asList(publishedCourse, unpublishedCourse));

        // Act
        List<CourseDto> result = courseService.getCoursesByInstructor(instructor.getId());

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
    }
}
