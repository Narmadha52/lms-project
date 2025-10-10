package com.lms.controller;

import com.lms.dto.ApiResponse;
import com.lms.dto.CourseDto;
import com.lms.dto.EnrollmentDto;
import com.lms.model.Role;
import com.lms.service.CourseService;
import com.lms.service.EnrollmentService;
import com.lms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class PageController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private UserService userService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalUsers", userService.getUserCount());
        model.addAttribute("totalCourses", courseService.getCourseCount());
        return "dashboard";
    }

    @GetMapping("/")
    public String root(Model model) {
        model.addAttribute("latestCourses", courseService.getLatestCourses(org.springframework.data.domain.PageRequest.of(0, 8)));
        return "index";
    }

    @GetMapping("/courses")
    public String courses(Model model) {
        List<CourseDto> courses = courseService.getPublishedCourses();
        model.addAttribute("courses", courses);
        return "courses";
    }

    @GetMapping("/my-courses")
    public String myCourses(Model model) {
        try {
            List<EnrollmentDto> enrollments = enrollmentService.getStudentEnrollments();
            model.addAttribute("enrollments", enrollments);
        } catch (Exception ignored) {
            model.addAttribute("enrollments", java.util.List.of());
        }
        return "my-courses";
    }

    @GetMapping("/assignments")
    public String assignments() { return "assignments"; }

    @GetMapping("/quizzes")
    public String quizzes() { return "quizzes"; }

    @GetMapping("/achievements")
    public String achievements() { return "achievements"; }

    @GetMapping("/profile")
    public String profile() { return "profile"; }
}


