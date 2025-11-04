package com.lms.controller;

import com.lms.dto.*;
import com.lms.model.DifficultyLevel;
import com.lms.service.CourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.mockito.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CourseControllerTest {

    @InjectMocks
    private CourseController courseController;

    @Mock
    private CourseService courseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ========== getPublishedCourses Tests ==========
    
    @Nested
    @DisplayName("Get Published Courses Tests")
    class GetPublishedCoursesTests {
        
        @Test
        @DisplayName("Should retrieve published courses successfully")
        void testGetPublishedCourses_Success() {
            // Arrange
            List<CourseDto> courses = Arrays.asList(
                    createMockCourse(1L, "Java Basics", true),
                    createMockCourse(2L, "Python Advanced", true)
            );
            when(courseService.getPublishedCourses()).thenReturn(courses);

            // Act
            ResponseEntity<ApiResponse<List<CourseDto>>> response = 
                    courseController.getPublishedCourses();

            // Assert
            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertTrue(response.getBody().isSuccess());
            assertEquals("Published courses retrieved successfully", response.getBody().getMessage());
            assertEquals(2, response.getBody().getData().size());
            
            verify(courseService, times(1)).getPublishedCourses();
        }

        @Test
        @DisplayName("Should return empty list when no published courses")
        void testGetPublishedCourses_EmptyList() {
            // Arrange
            when(courseService.getPublishedCourses()).thenReturn(Collections.emptyList());

            // Act
            ResponseEntity<ApiResponse<List<CourseDto>>> response = 
                    courseController.getPublishedCourses();

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertTrue(response.getBody().isSuccess());
            assertTrue(response.getBody().getData().isEmpty());
            
            verify(courseService, times(1)).getPublishedCourses();
        }

        @Test
        @DisplayName("Should handle error when retrieving published courses")
        void testGetPublishedCourses_Error() {
            // Arrange
            when(courseService.getPublishedCourses())
                    .thenThrow(new RuntimeException("Database error"));

            // Act
            ResponseEntity<ApiResponse<List<CourseDto>>> response = 
                    courseController.getPublishedCourses();

            // Assert
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertFalse(response.getBody().isSuccess());
            assertTrue(response.getBody().getMessage().contains("Database error"));
            
            verify(courseService, times(1)).getPublishedCourses();
        }
    }

    // ========== searchCourses Tests ==========
    
    @Nested
    @DisplayName("Search Courses Tests")
    class SearchCoursesTests {
        
        @Test
        @DisplayName("Should search courses by query successfully")
        void testSearchCourses_Success() {
            // Arrange
            String query = "java";
            List<CourseDto> courses = Arrays.asList(
                    createMockCourse(1L, "Java Basics", true),
                    createMockCourse(2L, "Advanced Java", true)
            );
            when(courseService.searchCourses(query)).thenReturn(courses);

            // Act
            ResponseEntity<ApiResponse<List<CourseDto>>> response = 
                    courseController.searchCourses(query);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertTrue(response.getBody().isSuccess());
            assertEquals("Search results retrieved successfully", response.getBody().getMessage());
            assertEquals(2, response.getBody().getData().size());
            
            verify(courseService, times(1)).searchCourses(query);
        }

        @Test
        @DisplayName("Should return empty list when no matches found")
        void testSearchCourses_NoResults() {
            // Arrange
            String query = "nonexistent";
            when(courseService.searchCourses(query)).thenReturn(Collections.emptyList());

            // Act
            ResponseEntity<ApiResponse<List<CourseDto>>> response = 
                    courseController.searchCourses(query);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertTrue(response.getBody().getData().isEmpty());
            
            verify(courseService, times(1)).searchCourses(query);
        }

        @Test
        @DisplayName("Should handle empty query string")
        void testSearchCourses_EmptyQuery() {
            // Arrange
            when(courseService.searchCourses("")).thenReturn(Collections.emptyList());

            // Act
            ResponseEntity<ApiResponse<List<CourseDto>>> response = 
                    courseController.searchCourses("");

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            verify(courseService, times(1)).searchCourses("");
        }

        @Test
        @DisplayName("Should handle search error")
        void testSearchCourses_Error() {
            // Arrange
            when(courseService.searchCourses("java"))
                    .thenThrow(new RuntimeException("Search service error"));

            // Act
            ResponseEntity<ApiResponse<List<CourseDto>>> response = 
                    courseController.searchCourses("java");

            // Assert
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertFalse(response.getBody().isSuccess());
            
            verify(courseService, times(1)).searchCourses("java");
        }
    }

    // ========== getCoursesByCategory Tests ==========
    
    @Nested
    @DisplayName("Get Courses By Category Tests")
    class GetCoursesByCategoryTests {
        
        @Test
        @DisplayName("Should retrieve courses by category successfully")
        void testGetCoursesByCategory_Success() {
            // Arrange
            String category = "Programming";
            List<CourseDto> courses = Arrays.asList(
                    createMockCourse(1L, "Java Course", true),
                    createMockCourse(2L, "Python Course", true)
            );
            when(courseService.getCoursesByCategory(category)).thenReturn(courses);

            // Act
            ResponseEntity<ApiResponse<List<CourseDto>>> response = 
                    courseController.getCoursesByCategory(category);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertTrue(response.getBody().isSuccess());
             assertEquals("Courses retrieved successfully", response.getBody().getMessage());
            assertEquals(2, response.getBody().getData().size());
            
            verify(courseService, times(1)).getCoursesByCategory(category);
        }

        @Test
        @DisplayName("Should handle invalid category")
        void testGetCoursesByCategory_InvalidCategory() {
            // Arrange
            String category = "InvalidCategory";
            when(courseService.getCoursesByCategory(category))
                    .thenThrow(new RuntimeException("Category not found"));

            // Act
            ResponseEntity<ApiResponse<List<CourseDto>>> response = 
                    courseController.getCoursesByCategory(category);

            // Assert
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertFalse(response.getBody().isSuccess());
            
            verify(courseService, times(1)).getCoursesByCategory(category);
        }
    }

    // ========== getCoursesByDifficulty Tests ==========
    
    @Nested
    @DisplayName("Get Courses By Difficulty Tests")
    class GetCoursesByDifficultyTests {
        
        @Test
        @DisplayName("Should retrieve beginner courses successfully")
        void testGetCoursesByDifficulty_Beginner() {
            // Arrange
            List<CourseDto> courses = Arrays.asList(createMockCourse(1L, "Beginner Course", true));
            when(courseService.getCoursesByDifficulty(DifficultyLevel.BEGINNER))
                    .thenReturn(courses);

            // Act
            ResponseEntity<ApiResponse<List<CourseDto>>> response = 
                    courseController.getCoursesByDifficulty(DifficultyLevel.BEGINNER);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertTrue(response.getBody().isSuccess());
            assertEquals(1, response.getBody().getData().size());
            
            verify(courseService, times(1)).getCoursesByDifficulty(DifficultyLevel.BEGINNER);
        }

        @Test
        @DisplayName("Should retrieve intermediate courses successfully")
        void testGetCoursesByDifficulty_Intermediate() {
            // Arrange
            List<CourseDto> courses = Arrays.asList(createMockCourse(1L, "Intermediate Course", true));
            when(courseService.getCoursesByDifficulty(DifficultyLevel.INTERMEDIATE))
                    .thenReturn(courses);

            // Act
            ResponseEntity<ApiResponse<List<CourseDto>>> response = 
                    courseController.getCoursesByDifficulty(DifficultyLevel.INTERMEDIATE);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            verify(courseService, times(1)).getCoursesByDifficulty(DifficultyLevel.INTERMEDIATE);
        }

        @Test
        @DisplayName("Should retrieve advanced courses successfully")
        void testGetCoursesByDifficulty_Advanced() {
            // Arrange
            List<CourseDto> courses = Arrays.asList(createMockCourse(1L, "Advanced Course", true));
            when(courseService.getCoursesByDifficulty(DifficultyLevel.ADVANCED))
                    .thenReturn(courses);

            // Act
            ResponseEntity<ApiResponse<List<CourseDto>>> response = 
                    courseController.getCoursesByDifficulty(DifficultyLevel.ADVANCED);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            verify(courseService, times(1)).getCoursesByDifficulty(DifficultyLevel.ADVANCED);
        }

        @Test
        @DisplayName("Should handle error when difficulty level is null")
        void testGetCoursesByDifficulty_NullDifficulty() {
            // Arrange
            when(courseService.getCoursesByDifficulty(null))
                    .thenThrow(new RuntimeException("Difficulty level cannot be null"));

            // Act
            ResponseEntity<ApiResponse<List<CourseDto>>> response = 
                    courseController.getCoursesByDifficulty(null);

            // Assert
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertFalse(response.getBody().isSuccess());
        }
    }

    // ========== getFreeCourses Tests ==========
    
    @Test
    @DisplayName("Should retrieve free courses successfully")
    void testGetFreeCourses_Success() {
        // Arrange
        List<CourseDto> courses = Arrays.asList(
                createMockCourse(1L, "Free Course 1", true),
                createMockCourse(2L, "Free Course 2", true)
        );
        when(courseService.getFreeCourses()).thenReturn(courses);

        // Act
        ResponseEntity<ApiResponse<List<CourseDto>>> response = 
                courseController.getFreeCourses();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Free courses retrieved successfully", response.getBody().getMessage());
        
        verify(courseService, times(1)).getFreeCourses();
    }

    // ========== getLatestCourses Tests ==========
    
    @Nested
    @DisplayName("Get Latest Courses Tests")
    class GetLatestCoursesTests {
        
        @Test
        @DisplayName("Should retrieve latest courses with default pagination")
        void testGetLatestCourses_Success() {
            // Arrange
            List<CourseDto> courses = Arrays.asList(createMockCourse(1L, "Latest Course", true));
            when(courseService.getLatestCourses(PageRequest.of(0, 10))).thenReturn(courses);

            // Act
            ResponseEntity<ApiResponse<List<CourseDto>>> response = 
                    courseController.getLatestCourses(0, 10);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertTrue(response.getBody().isSuccess());
            
            verify(courseService, times(1)).getLatestCourses(PageRequest.of(0, 10));
        }

        @Test
        @DisplayName("Should handle custom pagination parameters")
        void testGetLatestCourses_CustomPagination() {
            // Arrange
            List<CourseDto> courses = Arrays.asList(createMockCourse(1L, "Course", true));
            when(courseService.getLatestCourses(PageRequest.of(2, 5))).thenReturn(courses);

            // Act
            ResponseEntity<ApiResponse<List<CourseDto>>> response = 
                    courseController.getLatestCourses(2, 5);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            verify(courseService, times(1)).getLatestCourses(PageRequest.of(2, 5));
        }

        @Test
        @DisplayName("Should handle invalid pagination parameters")
        void testGetLatestCourses_InvalidPagination() {
            // Arrange
            when(courseService.getLatestCourses(any(PageRequest.class)))
                    .thenThrow(new RuntimeException("Invalid page parameters"));

            // Act
            ResponseEntity<ApiResponse<List<CourseDto>>> response = 
                    courseController.getLatestCourses(-1, 0);

            // Assert
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertFalse(response.getBody().isSuccess());
        }
    }

    // ========== getCourseById Tests ==========
    
    @Nested
    @DisplayName("Get Course By ID Tests")
    class GetCourseByIdTests {
        
        @Test
        @DisplayName("Should retrieve course by ID successfully")
        void testGetCourseById_Success() {
            // Arrange
            Long courseId = 1L;
            CourseDto course = createMockCourse(courseId, "Java Course", true);
            when(courseService.getCourseById(courseId)).thenReturn(course);

            // Act
            ResponseEntity<ApiResponse<CourseDto>> response = 
                    courseController.getCourseById(courseId);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertTrue(response.getBody().isSuccess());
            assertEquals("Course retrieved successfully", response.getBody().getMessage());
            assertEquals(courseId, response.getBody().getData().getId());
            
            verify(courseService, times(1)).getCourseById(courseId);
        }

        @Test
        @DisplayName("Should return error when course not found")
        void testGetCourseById_NotFound() {
            // Arrange
            Long courseId = 999L;
            when(courseService.getCourseById(courseId))
                    .thenThrow(new RuntimeException("Course not found with id: " + courseId));

            // Act
            ResponseEntity<ApiResponse<CourseDto>> response = 
                    courseController.getCourseById(courseId);

            // Assert
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertFalse(response.getBody().isSuccess());
            assertTrue(response.getBody().getMessage().contains("Course not found"));
            
            verify(courseService, times(1)).getCourseById(courseId);
        }

        @Test
        @DisplayName("Should handle null course ID")
        void testGetCourseById_NullId() {
            // Arrange
            when(courseService.getCourseById(null))
                    .thenThrow(new RuntimeException("Course ID cannot be null"));

            // Act
            ResponseEntity<ApiResponse<CourseDto>> response = 
                    courseController.getCourseById(null);

            // Assert
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertFalse(response.getBody().isSuccess());
        }
    }

    // ========== createCourse Tests ==========
    
    @Nested
    @DisplayName("Create Course Tests")
    class CreateCourseTests {
        
        @Test
        @DisplayName("Should create course successfully")
        void testCreateCourse_Success() {
            // Arrange
            CourseDto newCourse = createMockCourse(null, "New Course", false);
            CourseDto savedCourse = createMockCourse(1L, "New Course", false);
            when(courseService.createCourse(newCourse)).thenReturn(savedCourse);

            // Act
            ResponseEntity<ApiResponse<CourseDto>> response = 
                    courseController.createCourse(newCourse);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertTrue(response.getBody().isSuccess());
            assertEquals("Course created successfully", response.getBody().getMessage());
            assertNotNull(response.getBody().getData().getId());
            
            verify(courseService, times(1)).createCourse(newCourse);
        }

        @Test
        @DisplayName("Should handle validation error during creation")
        void testCreateCourse_ValidationError() {
            // Arrange
            CourseDto invalidCourse = new CourseDto();
            when(courseService.createCourse(invalidCourse))
                    .thenThrow(new RuntimeException("Course title is required"));

            // Act
            ResponseEntity<ApiResponse<CourseDto>> response = 
                    courseController.createCourse(invalidCourse);

            // Assert
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertFalse(response.getBody().isSuccess());
            assertTrue(response.getBody().getMessage().contains("title is required"));
            
            verify(courseService, times(1)).createCourse(invalidCourse);
        }

        @Test
        @DisplayName("Should handle duplicate course error")
        void testCreateCourse_DuplicateError() {
            // Arrange
            CourseDto course = createMockCourse(null, "Existing Course", false);
            when(courseService.createCourse(course))
                    .thenThrow(new RuntimeException("Course with this title already exists"));

            // Act
            ResponseEntity<ApiResponse<CourseDto>> response = 
                    courseController.createCourse(course);

            // Assert
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertFalse(response.getBody().isSuccess());
            assertTrue(response.getBody().getMessage().contains("already exists"));
        }
    }

    // ========== updateCourse Tests ==========
    
    @Nested
    @DisplayName("Update Course Tests")
    class UpdateCourseTests {
        
        @Test
        @DisplayName("Should update course successfully")
        void testUpdateCourse_Success() {
            // Arrange
            Long courseId = 1L;
            CourseDto updateDto = createMockCourse(courseId, "Updated Course", true);
            when(courseService.updateCourse(courseId, updateDto)).thenReturn(updateDto);

            // Act
            ResponseEntity<ApiResponse<CourseDto>> response = 
                    courseController.updateCourse(courseId, updateDto);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertTrue(response.getBody().isSuccess());
            assertEquals("Course updated successfully", response.getBody().getMessage());
            
            verify(courseService, times(1)).updateCourse(courseId, updateDto);
        }

        @Test
        @DisplayName("Should handle course not found during update")
        void testUpdateCourse_NotFound() {
            // Arrange
            Long courseId = 999L;
            CourseDto updateDto = createMockCourse(courseId, "Updated", true);
            when(courseService.updateCourse(courseId, updateDto))
                    .thenThrow(new RuntimeException("Course not found"));

            // Act
            ResponseEntity<ApiResponse<CourseDto>> response = 
                    courseController.updateCourse(courseId, updateDto);

            // Assert
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertFalse(response.getBody().isSuccess());
            
            verify(courseService, times(1)).updateCourse(courseId, updateDto);
        }

        @Test
        @DisplayName("Should handle validation error during update")
        void testUpdateCourse_ValidationError() {
            // Arrange
            Long courseId = 1L;
            CourseDto invalidDto = new CourseDto();
            when(courseService.updateCourse(courseId, invalidDto))
                    .thenThrow(new RuntimeException("Invalid course data"));

            // Act
            ResponseEntity<ApiResponse<CourseDto>> response = 
                    courseController.updateCourse(courseId, invalidDto);

            // Assert
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertFalse(response.getBody().isSuccess());
        }
    }

    // ========== deleteCourse Tests ==========
    
    @Nested
    @DisplayName("Delete Course Tests")
    class DeleteCourseTests {
        
        @Test
        @DisplayName("Should delete course successfully")
        void testDeleteCourse_Success() {
            // Arrange
            Long courseId = 1L;
            doNothing().when(courseService).deleteCourse(courseId);

            // Act
            ResponseEntity<ApiResponse<Void>> response = 
                    courseController.deleteCourse(courseId);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertTrue(response.getBody().isSuccess());
            assertEquals("Course deleted successfully", response.getBody().getMessage());
            
            verify(courseService, times(1)).deleteCourse(courseId);
        }

        @Test
        @DisplayName("Should handle course not found during deletion")
        void testDeleteCourse_NotFound() {
            // Arrange
            Long courseId = 999L;
            doThrow(new RuntimeException("Course not found"))
                    .when(courseService).deleteCourse(courseId);

            // Act
            ResponseEntity<ApiResponse<Void>> response = 
                    courseController.deleteCourse(courseId);

            // Assert
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertFalse(response.getBody().isSuccess());
            assertTrue(response.getBody().getMessage().contains("Course not found"));
            
            verify(courseService, times(1)).deleteCourse(courseId);
        }

        @Test
        @DisplayName("Should handle deletion constraint error")
        void testDeleteCourse_ConstraintError() {
            // Arrange
            Long courseId = 1L;
            doThrow(new RuntimeException("Cannot delete course with active enrollments"))
                    .when(courseService).deleteCourse(courseId);

            // Act
            ResponseEntity<ApiResponse<Void>> response = 
                    courseController.deleteCourse(courseId);

            // Assert
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertFalse(response.getBody().isSuccess());
            assertTrue(response.getBody().getMessage().contains("active enrollments"));
        }
    }

    // ========== Helper Methods ==========
    
    private CourseDto createMockCourse(Long id, String title, boolean published) {
        CourseDto dto = new CourseDto();
        dto.setId(id);
        dto.setTitle(title);
        dto.setDescription("Test description for " + title);
        dto.setPublished(published);
        dto.setDifficulty(DifficultyLevel.BEGINNER);
        dto.setCategory("Programming");
        dto.setInstructorName("Test Instructor");
        dto.setDuration(40);
        return dto;
    }
}
