package com.lms.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "lesson_progress", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"enrollment_id", "lesson_id"}))
@EntityListeners(AuditingEntityListener.class)
public class LessonProgress {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enrollment_id", nullable = false)
    private Enrollment enrollment;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;
    
    @Column(name = "is_completed")
    private Boolean isCompleted = false;
    
    @Column(name = "completed_at")
    private LocalDateTime completedAt;
    
    @Column(name = "time_spent_minutes")
    private Integer timeSpentMinutes = 0;
    
    @Column(name = "last_accessed_at")
    private LocalDateTime lastAccessedAt;
    
    // Constructors
    public LessonProgress() {}
    
    public LessonProgress(Enrollment enrollment, Lesson lesson) {
        this.enrollment = enrollment;
        this.lesson = lesson;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Enrollment getEnrollment() {
        return enrollment;
    }
    
    public void setEnrollment(Enrollment enrollment) {
        this.enrollment = enrollment;
    }
    
    public Lesson getLesson() {
        return lesson;
    }
    
    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }
    
    public Boolean getIsCompleted() {
        return isCompleted;
    }
    
    public void setIsCompleted(Boolean isCompleted) {
        this.isCompleted = isCompleted;
        if (isCompleted && this.completedAt == null) {
            this.completedAt = LocalDateTime.now();
        }
    }
    
    public LocalDateTime getCompletedAt() {
        return completedAt;
    }
    
    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }
    
    public Integer getTimeSpentMinutes() {
        return timeSpentMinutes;
    }
    
    public void setTimeSpentMinutes(Integer timeSpentMinutes) {
        this.timeSpentMinutes = timeSpentMinutes;
    }
    
    public LocalDateTime getLastAccessedAt() {
        return lastAccessedAt;
    }
    
    public void setLastAccessedAt(LocalDateTime lastAccessedAt) {
        this.lastAccessedAt = lastAccessedAt;
    }
    
    // Helper methods
    public void markAsCompleted() {
        this.isCompleted = true;
        if (this.completedAt == null) {
            this.completedAt = LocalDateTime.now();
        }
    }
    
    public void markAsAccessed() {
        this.lastAccessedAt = LocalDateTime.now();
    }
    
    public void addTimeSpent(int minutes) {
        if (this.timeSpentMinutes == null) {
            this.timeSpentMinutes = 0;
        }
        this.timeSpentMinutes += minutes;
    }
    
    public boolean isCompleted() {
        return isCompleted != null && isCompleted;
    }
    
    public String getFormattedTimeSpent() {
        if (timeSpentMinutes == null || timeSpentMinutes == 0) {
            return "0m";
        }
        int hours = timeSpentMinutes / 60;
        int minutes = timeSpentMinutes % 60;
        if (hours > 0) {
            return String.format("%dh %dm", hours, minutes);
        } else {
            return String.format("%dm", minutes);
        }
    }


       public void setCompleted(boolean b) {
		// TODO Auto-generated method stub
		
	}

	public void setTimeSpent(int i) {
		// TODO Auto-generated method stub
		
	}

	public Boolean getCompleted() {
		// TODO Auto-generated method stub
		return null;
	}

	public Integer getTimeSpent() {
		// TODO Auto-generated method stub
		return null;
	}
       
}

