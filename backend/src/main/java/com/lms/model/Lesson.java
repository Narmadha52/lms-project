package com.lms.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "lessons")
@EntityListeners(AuditingEntityListener.class)
public class Lesson extends Auditable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;
    
    @NotBlank
    @Size(max = 200)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "lesson_type", nullable = false)
    private LessonType lessonType;
    
    @Column(columnDefinition = "TEXT")
    private String content;
    
    @Size(max = 500)
    @Column(name = "file_url")
    private String fileUrl;
    
    @Size(max = 255)
    @Column(name = "file_name")
    private String fileName;
    
    @Column(name = "file_size")
    private Long fileSize;
    
    @Column(name = "duration_minutes")
    private Integer durationMinutes = 0;
    
    @NotNull
    @Column(name = "order_index")
    private Integer orderIndex;
    
    @Column(name = "is_published")
    private Boolean isPublished = false;
    
    // createdAt/updatedAt inherited from Auditable
    
    // Relationships
    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<LessonProgress> lessonProgresses = new java.util.ArrayList<>();
    
    // Constructors
    public Lesson() {}
    
    public Lesson(Course course, String title, String description, LessonType lessonType, String content, Integer orderIndex) {
        this.course = course;
        this.title = title;
        this.description = description;
        this.lessonType = lessonType;
        this.content = content;
        this.orderIndex = orderIndex;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Course getCourse() {
        return course;
    }
    
    public void setCourse(Course course) {
        this.course = course;
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
    
    public LessonType getLessonType() {
        return lessonType;
    }
    
    public void setLessonType(LessonType lessonType) {
        this.lessonType = lessonType;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public String getFileUrl() {
        return fileUrl;
    }
    
    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
    
    public String getFileName() {
        return fileName;
    }
    
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    public Long getFileSize() {
        return fileSize;
    }
    
    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }
    
    public Integer getDurationMinutes() {
        return durationMinutes;
    }
    
    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }
    
    public Integer getOrderIndex() {
        return orderIndex;
    }
    
    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }
    
    public Boolean getIsPublished() {
        return isPublished;
    }
    
    public void setIsPublished(Boolean isPublished) {
        this.isPublished = isPublished;
    }
    
    // Auditing getters/setters provided by Auditable
    
    public java.util.List<LessonProgress> getLessonProgresses() {
        return lessonProgresses;
    }
    
    public void setLessonProgresses(java.util.List<LessonProgress> lessonProgresses) {
        this.lessonProgresses = lessonProgresses;
    }
    
    // Helper methods
    public boolean isTextLesson() {
        return lessonType == LessonType.TEXT;
    }
    
    public boolean isVideoLesson() {
        return lessonType == LessonType.VIDEO;
    }
    
    public boolean isAudioLesson() {
        return lessonType == LessonType.AUDIO;
    }
    
    public boolean isPdfLesson() {
        return lessonType == LessonType.PDF;
    }
    
    public String getFormattedDuration() {
        if (durationMinutes == null || durationMinutes == 0) {
            return "N/A";
        }
        int hours = durationMinutes / 60;
        int minutes = durationMinutes % 60;
        if (hours > 0) {
            return String.format("%dh %dm", hours, minutes);
        } else {
            return String.format("%dm", minutes);
        }
    }
}

