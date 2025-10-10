package com.lms.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class EnrollmentDto {
    
    private Long id;
    private Long studentId;
    private String studentName;
    private Long courseId;
    private String courseTitle;
    private LocalDateTime enrolledAt;
    private BigDecimal progressPercentage;
    private LocalDateTime lastAccessedAt;
    private Boolean isCompleted;
    private LocalDateTime completedAt;
    private int completedLessonsCount;
    private int totalLessonsCount;
    
    public EnrollmentDto() {}
    
    public EnrollmentDto(Long id, Long studentId, String studentName, Long courseId, String courseTitle,
                        LocalDateTime enrolledAt, BigDecimal progressPercentage, LocalDateTime lastAccessedAt,
                        Boolean isCompleted, LocalDateTime completedAt) {
        this.id = id;
        this.studentId = studentId;
        this.studentName = studentName;
        this.courseId = courseId;
        this.courseTitle = courseTitle;
        this.enrolledAt = enrolledAt;
        this.progressPercentage = progressPercentage;
        this.lastAccessedAt = lastAccessedAt;
        this.isCompleted = isCompleted;
        this.completedAt = completedAt;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getStudentId() {
        return studentId;
    }
    
    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }
    
    public String getStudentName() {
        return studentName;
    }
    
    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
    
    public Long getCourseId() {
        return courseId;
    }
    
    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }
    
    public String getCourseTitle() {
        return courseTitle;
    }
    
    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }
    
    public LocalDateTime getEnrolledAt() {
        return enrolledAt;
    }
    
    public void setEnrolledAt(LocalDateTime enrolledAt) {
        this.enrolledAt = enrolledAt;
    }
    
    public BigDecimal getProgressPercentage() {
        return progressPercentage;
    }
    
    public void setProgressPercentage(BigDecimal progressPercentage) {
        this.progressPercentage = progressPercentage;
    }
    
    public LocalDateTime getLastAccessedAt() {
        return lastAccessedAt;
    }
    
    public void setLastAccessedAt(LocalDateTime lastAccessedAt) {
        this.lastAccessedAt = lastAccessedAt;
    }
    
    public Boolean getIsCompleted() {
        return isCompleted;
    }
    
    public void setIsCompleted(Boolean isCompleted) {
        this.isCompleted = isCompleted;
    }
    
    public LocalDateTime getCompletedAt() {
        return completedAt;
    }
    
    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }
    
    public int getCompletedLessonsCount() {
        return completedLessonsCount;
    }
    
    public void setCompletedLessonsCount(int completedLessonsCount) {
        this.completedLessonsCount = completedLessonsCount;
    }
    
    public int getTotalLessonsCount() {
        return totalLessonsCount;
    }
    
    public void setTotalLessonsCount(int totalLessonsCount) {
        this.totalLessonsCount = totalLessonsCount;
    }
    
    public boolean isCompleted() {
        return isCompleted != null && isCompleted;
    }
}

