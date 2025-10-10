package com.lms.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "enrollments", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "course_id"}))
@EntityListeners(AuditingEntityListener.class)
public class Enrollment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;
    
    @CreatedDate
    @Column(name = "enrolled_at", nullable = false, updatable = false)
    private LocalDateTime enrolledAt;
    
    @DecimalMin(value = "0.0", inclusive = true)
    @DecimalMax(value = "100.0", inclusive = true)
    @Column(name = "progress_percentage", precision = 5, scale = 2)
    private BigDecimal progressPercentage = BigDecimal.ZERO;
    
    @Column(name = "last_accessed_at")
    private LocalDateTime lastAccessedAt;
    
    @Column(name = "is_completed")
    private Boolean isCompleted = false;
    
    @Column(name = "completed_at")
    private LocalDateTime completedAt;
    
    // Relationships
    @OneToMany(mappedBy = "enrollment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LessonProgress> lessonProgresses = new ArrayList<>();
    
    // Constructors
    public Enrollment() {}
    
    public Enrollment(User student, Course course) {
        this.student = student;
        this.course = course;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public User getStudent() {
        return student;
    }
    
    public void setStudent(User student) {
        this.student = student;
    }
    
    public Course getCourse() {
        return course;
    }
    
    public void setCourse(Course course) {
        this.course = course;
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
    
    public List<LessonProgress> getLessonProgresses() {
        return lessonProgresses;
    }
    
    public void setLessonProgresses(List<LessonProgress> lessonProgresses) {
        this.lessonProgresses = lessonProgresses;
    }
    
    // Helper methods
    public void updateProgress() {
        if (lessonProgresses == null || lessonProgresses.isEmpty()) {
            this.progressPercentage = BigDecimal.ZERO;
            return;
        }
        
        long completedLessons = lessonProgresses.stream()
                .mapToLong(lp -> lp.getIsCompleted() ? 1 : 0)
                .sum();
        
        if (course.getLessons() != null && !course.getLessons().isEmpty()) {
            double progress = (double) completedLessons / course.getLessons().size() * 100;
            this.progressPercentage = BigDecimal.valueOf(progress).setScale(2, BigDecimal.ROUND_HALF_UP);
            
            // Mark as completed if all lessons are completed
            if (completedLessons == course.getLessons().size()) {
                this.isCompleted = true;
                if (this.completedAt == null) {
                    this.completedAt = LocalDateTime.now();
                }
            }
        }
    }
    
    public void markAsAccessed() {
        this.lastAccessedAt = LocalDateTime.now();
    }
    
    public boolean isCompleted() {
        return isCompleted != null && isCompleted;
    }
    
    public int getCompletedLessonsCount() {
        if (lessonProgresses == null) {
            return 0;
        }
        return (int) lessonProgresses.stream()
                .mapToLong(lp -> lp.getIsCompleted() ? 1 : 0)
                .sum();
    }
    
    public int getTotalLessonsCount() {
        return course != null && course.getLessons() != null ? course.getLessons().size() : 0;
    }
}

