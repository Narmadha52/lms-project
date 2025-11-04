package com.lms.service;

import com.lms.dto.CourseDto;
import com.lms.model.*;
import com.lms.repository.CourseRepository;
import com.lms.repository.LessonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
    private User admin;
    private Course sampleCourse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        instructor = new User();
        instructor.setId(1L);
        instructor.setRole(Role.INSTRUCTOR);
        instructor.setIsApproved(true);

        admin = new User();
        admin.setId(2L);
        admin.setRole(Role.ADMIN);
        admin.setIsApproved(true);

        sampleCourse = new Course();
        sampleCourse.setId(10L);
        sampleCourse.setTitle("Java Basics");
        sampleCourse.setDescription("Intro to Java");
        sampleCourse.setInstructor(instructor);
        sampleCourse.setCategory("Programming");
        sampleCourse.setDifficultyLevel(DifficultyLevel.BEGINNER);
        sampleCourse.setPrice(BigDecimal.valueOf(100));
        sampleCourse.setIsPublished(false);
    }

    // ---------- getAllCourses ----------
    @Test
    void testGetAllCourses() {
        when(courseRepository.findAll()).thenReturn(List.of(sampleCourse));

        var result = courseService.getAllCourses();

        assertEquals(1, result.size());
        assertEquals("Java Basics", result.get(0).getTitle());
    }

    // ---------- getPublishedCourses ----------
    @Test
    void testGetPublishedCourses() {
        when(courseRepository.findByIsPublished(true)).thenReturn(List.of(sampleCourse));

        var result = courseService.getPublishedCourses();

        assertEquals(1, result.size());
        verify(courseRepository).findByIsPublished(true);
    }

    // ---------- getCourseById ----------
    @Test
    void testGetCourseById_Success() {
        when(courseRepository.findById(10L)).thenReturn(Optional.of(sampleCourse));

        var result = courseService.getCourseById(10L);

        assertNotNull(result);
        assertEquals("Java Basics", result.getTitle());
    }

    @Test
    void testGetCourseById_NotFound() {
        when(courseRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> courseService.getCourseById(99L));
    }

    // ---------- createCourse ----------
    @Test
    void testCreateCourse_ByInstructor_Success() {
        when(authService.getCurrentUser()).thenReturn(instructor);
        when(courseRepository.save(any(Course.class))).thenAnswer(i -> {
            Course c = i.getArgument(0);
            c.setId(1L);
            return c;
        });

        CourseDto dto = new CourseDto();
        dto.setTitle("Spring Boot");
        dto.setDescription("Learn Spring Boot");
        dto.setCategory("Backend");
        dto.setDifficultyLevel(DifficultyLevel.INTERMEDIATE);
        dto.setPrice(BigDecimal.valueOf(200));
        dto.setIsPublished(false);
        dto.setThumbnailUrl("image.jpg");

        CourseDto result = courseService.createCourse(dto);

        assertEquals("Spring Boot", result.getTitle());
        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    void testCreateCourse_ByStudent_ThrowsError() {
        User student = new User();
        student.setRole(Role.STUDENT);
        when(authService.getCurrentUser()).thenReturn(student);

        CourseDto dto = new CourseDto();
        dto.setTitle("Unauthorized Course");

        assertThrows(RuntimeException.class, () -> courseService.createCourse(dto));
        verify(courseRepository, never()).save(any());
    }

    // ---------- updateCourse ----------
    @Test
    void testUpdateCourse_Success_ByInstructor() {
        when(courseRepository.findById(10L)).thenReturn(Optional.of(sampleCourse));
        when(authService.getCurrentUser()).thenReturn(instructor);
        when(courseRepository.save(any(Course.class))).thenAnswer(i -> i.getArgument(0));

        CourseDto dto = new CourseDto();
        dto.setTitle("Updated Title");
        dto.setDescription("Updated Description");

        CourseDto updated = courseService.updateCourse(10L, dto);

        assertEquals("Updated Title", updated.getTitle());
        verify(courseRepository).save(any(Course.class));
    }

    @Test
    void testUpdateCourse_ThrowsIfNotOwner() {
        User anotherInstructor = new User();
        anotherInstructor.setId(5L);
        anotherInstructor.setRole(Role.INSTRUCTOR);

        when(courseRepository.findById(10L)).thenReturn(Optional.of(sampleCourse));
        when(authService.getCurrentUser()).thenReturn(anotherInstructor);

        CourseDto dto = new CourseDto();
        dto.setTitle("Invalid Update");

        assertThrows(RuntimeException.class, () -> courseService.updateCourse(10L, dto));
        verify(courseRepository, never()).save(any());
    }

    // ---------- deleteCourse ----------
    @Test
    void testDeleteCourse_ByAdmin_Success() {
        when(courseRepository.findById(10L)).thenReturn(Optional.of(sampleCourse));
        when(authService.getCurrentUser()).thenReturn(admin);

        courseService.deleteCourse(10L);

        verify(courseRepository).deleteById(10L);
    }

    @Test
    void testDeleteCourse_ThrowsIfNotAuthorized() {
        User anotherInstructor = new User();
        anotherInstructor.setId(5L);
        anotherInstructor.setRole(Role.INSTRUCTOR);

        when(courseRepository.findById(10L)).thenReturn(Optional.of(sampleCourse));
        when(authService.getCurrentUser()).thenReturn(anotherInstructor);

        assertThrows(RuntimeException.class, () -> courseService.deleteCourse(10L));
        verify(courseRepository, never()).deleteById(any());
    }

    // ---------- publishCourse ----------
    @Test
    void testPublishCourse_Success() {
        when(courseRepository.findById(10L)).thenReturn(Optional.of(sampleCourse));
        when(authService.getCurrentUser()).thenReturn(instructor);
        when(courseRepository.save(any(Course.class))).thenAnswer(i -> i.getArgument(0));

        CourseDto dto = courseService.publishCourse(10L);

        assertTrue(dto.getIsPublished());
        verify(courseRepository).save(any());
    }

    // ---------- unpublishCourse ----------
    @Test
    void testUnpublishCourse_Success() {
        sampleCourse.setIsPublished(true);
        when(courseRepository.findById(10L)).thenReturn(Optional.of(sampleCourse));
        when(authService.getCurrentUser()).thenReturn(instructor);
        when(courseRepository.save(any(Course.class))).thenAnswer(i -> i.getArgument(0));

        CourseDto dto = courseService.unpublishCourse(10L);

        assertFalse(dto.getIsPublished());
    }

    // ---------- getCourseCount ----------
    @Test
    void testGetCourseCount() {
        when(courseRepository.count()).thenReturn(5L);
        assertEquals(5L, courseService.getCourseCount());
    }

    // ---------- getLatestCourses ----------
    @Test
    void testGetLatestCourses() {
        when(courseRepository.findLatestPublishedCourses(any(PageRequest.class)))
                .thenReturn(List.of(sampleCourse));

        var result = courseService.getLatestCourses(PageRequest.of(0, 5));
        assertEquals(1, result.size());
    }
}
