package com.lms.dto;

import java.time.LocalDateTime;

public class AssignmentDto {
    
    private Long id;
    private Long courseId;
    private String courseTitle;
    private String title;
    private String description;
    private LocalDateTime dueDate;
    private Integer maxPoints;
    private Boolean isPublished;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int submissionCount;
    private int gradedSubmissionCount;
    private double averageGrade;
    private boolean isOverdue;
    private boolean isDueSoon;
    private long daysUntilDue;
    
    public AssignmentDto() {}
    
    public AssignmentDto(Long id, Long courseId, String courseTitle, String title, String description,
                        LocalDateTime dueDate, Integer maxPoints, Boolean isPublished,
                        LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.courseId = courseId;
        this.courseTitle = courseTitle;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.maxPoints = maxPoints;
        this.isPublished = isPublished;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
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
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public LocalDateTime getDueDate() {
        return dueDate;
    }
    
    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }
    
    public Integer getMaxPoints() {
        return maxPoints;
    }
    
    public void setMaxPoints(Integer maxPoints) {
        this.maxPoints = maxPoints;
    }
    
    public Boolean getIsPublished() {
        return isPublished;
    }
    
    public void setIsPublished(Boolean isPublished) {
        this.isPublished = isPublished;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public int getSubmissionCount() {
        return submissionCount;
    }
    
    public void setSubmissionCount(int submissionCount) {
        this.submissionCount = submissionCount;
    }
    
    public int getGradedSubmissionCount() {
        return gradedSubmissionCount;
    }
    
    public void setGradedSubmissionCount(int gradedSubmissionCount) {
        this.gradedSubmissionCount = gradedSubmissionCount;
    }
    
    public double getAverageGrade() {
        return averageGrade;
    }
    
    public void setAverageGrade(double averageGrade) {
        this.averageGrade = averageGrade;
    }
    
    public boolean isOverdue() {
        return isOverdue;
    }
    
    public void setOverdue(boolean overdue) {
        isOverdue = overdue;
    }
    
    public boolean isDueSoon() {
        return isDueSoon;
    }
    
    public void setDueSoon(boolean dueSoon) {
        isDueSoon = dueSoon;
    }
    
    public long getDaysUntilDue() {
        return daysUntilDue;
    }
    
    public void setDaysUntilDue(long daysUntilDue) {
        this.daysUntilDue = daysUntilDue;
    }
}

