package com.lms.controller;

import com.lms.dto.ApiResponse;
import com.lms.dto.CourseDto;
import com.lms.model.DifficultyLevel;
import com.lms.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping("/public")
    public ResponseEntity<ApiResponse<List<CourseDto>>> getPublishedCourses() {
        try {
            List<CourseDto> courses = courseService.getPublishedCourses();
            return ResponseEntity.ok(ApiResponse.success("Published courses retrieved successfully", courses));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error: " + e.getMessage()));
        }
    }

    @GetMapping("/public/search")
    public ResponseEntity<ApiResponse<List<CourseDto>>> searchCourses(@RequestParam String q) {
        try {
            List<CourseDto> courses = courseService.searchCourses(q);
            return ResponseEntity.ok(ApiResponse.success("Search results retrieved successfully", courses));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error: " + e.getMessage()));
        }
    }

    @GetMapping("/public/category/{category}")
    public ResponseEntity<ApiResponse<List<CourseDto>>> getCoursesByCategory(@PathVariable String category) {
        try {
            List<CourseDto> courses = courseService.getCoursesByCategory(category);
            return ResponseEntity.ok(ApiResponse.success("Courses retrieved successfully", courses));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error: " + e.getMessage()));
        }
    }

    @GetMapping("/public/difficulty/{difficulty}")
    public ResponseEntity<ApiResponse<List<CourseDto>>> getCoursesByDifficulty(@PathVariable DifficultyLevel difficulty) {
        try {
            List<CourseDto> courses = courseService.getCoursesByDifficulty(difficulty);
            return ResponseEntity.ok(ApiResponse.success("Courses retrieved successfully", courses));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error: " + e.getMessage()));
        }
    }

    @GetMapping("/public/free")
    public ResponseEntity<ApiResponse<List<CourseDto>>> getFreeCourses() {
        try {
            List<CourseDto> courses = courseService.getFreeCourses();
            return ResponseEntity.ok(ApiResponse.success("Free courses retrieved successfully", courses));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error: " + e.getMessage()));
        }
    }

    @GetMapping("/public/latest")
    public ResponseEntity<ApiResponse<List<CourseDto>>> getLatestCourses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            List<CourseDto> courses = courseService.getLatestCourses(pageable);
            return ResponseEntity.ok(ApiResponse.success("Latest courses retrieved successfully", courses));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error: " + e.getMessage()));
        }
    }

    @GetMapping("/public/popular")
    public ResponseEntity<ApiResponse<List<CourseDto>>> getMostPopularCourses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            List<CourseDto> courses = courseService.getMostPopularCourses(pageable);
            return ResponseEntity.ok(ApiResponse.success("Popular courses retrieved successfully", courses));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CourseDto>> getCourseById(@PathVariable Long id) {
        try {
            CourseDto course = courseService.getCourseById(id);
            return ResponseEntity.ok(ApiResponse.success("Course retrieved successfully", course));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error: " + e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CourseDto>> createCourse(@RequestBody CourseDto courseDto) {
        try {
            CourseDto course = courseService.createCourse(courseDto);
            return ResponseEntity.ok(ApiResponse.success("Course created successfully", course));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CourseDto>> updateCourse(@PathVariable Long id, @RequestBody CourseDto courseDto) {
        try {
            CourseDto course = courseService.updateCourse(id, courseDto);
            return ResponseEntity.ok(ApiResponse.success("Course updated successfully", course));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCourse(@PathVariable Long id) {
        try {
            courseService.deleteCourse(id);
            return ResponseEntity.ok(ApiResponse.success("Course deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error: " + e.getMessage()));
        }
    }

    @PostMapping("/{id}/publish")
    public ResponseEntity<ApiResponse<CourseDto>> publishCourse(@PathVariable Long id) {
        try {
            CourseDto course = courseService.publishCourse(id);
            return ResponseEntity.ok(ApiResponse.success("Course published successfully", course));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error: " + e.getMessage()));
        }
    }

    @PostMapping("/{id}/unpublish")
    public ResponseEntity<ApiResponse<CourseDto>> unpublishCourse(@PathVariable Long id) {
        try {
            CourseDto course = courseService.unpublishCourse(id);
            return ResponseEntity.ok(ApiResponse.success("Course unpublished successfully", course));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error: " + e.getMessage()));
        }
    }
}

