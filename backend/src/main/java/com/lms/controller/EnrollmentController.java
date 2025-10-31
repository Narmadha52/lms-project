package com.lms.controller;

import com.lms.dto.ApiResponse;
import com.lms.dto.EnrollmentDto;
import com.lms.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/enrollments")
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    @PostMapping("/{courseId}")
    public ResponseEntity<ApiResponse<EnrollmentDto>> enrollInCourse(@PathVariable Long courseId) {
        try {
            EnrollmentDto enrollment = enrollmentService.enrollInCourse(courseId);
            return ResponseEntity.ok(ApiResponse.success("Successfully enrolled in course", enrollment));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<ApiResponse<Void>> unenrollFromCourse(@PathVariable Long courseId) {
        try {
            enrollmentService.unenrollFromCourse(courseId);
            return ResponseEntity.ok(ApiResponse.success("Successfully unenrolled from course", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error: " + e.getMessage()));
        }
    }

    @GetMapping("/my-enrollments")
    public ResponseEntity<ApiResponse<List<EnrollmentDto>>> getMyEnrollments() {
        try {
            List<EnrollmentDto> enrollments = enrollmentService.getStudentEnrollments();
            return ResponseEntity.ok(ApiResponse.success("Enrollments retrieved successfully", enrollments));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error: " + e.getMessage()));
        }
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<ApiResponse<List<EnrollmentDto>>> getCourseEnrollments(@PathVariable Long courseId) {
        try {
            List<EnrollmentDto> enrollments = enrollmentService.getCourseEnrollments(courseId);
            return ResponseEntity.ok(ApiResponse.success("Course enrollments retrieved successfully", enrollments));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error: " + e.getMessage()));
        }
    }

    @GetMapping("/{courseId}/status")
    public ResponseEntity<ApiResponse<Boolean>> isEnrolled(@PathVariable Long courseId) {
        try {
            boolean isEnrolled = enrollmentService.isEnrolled(courseId);
            return ResponseEntity.ok(ApiResponse.success("Enrollment status retrieved successfully", isEnrolled));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error: " + e.getMessage()));
        }
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<ApiResponse<EnrollmentDto>> getEnrollment(@PathVariable Long courseId) {
        try {
            EnrollmentDto enrollment = enrollmentService.getEnrollment(courseId);
            return ResponseEntity.ok(ApiResponse.success("Enrollment retrieved successfully", enrollment));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error: " + e.getMessage()));
        }
    }
}

