package com.lms.dto;

import com.lms.model.LessonType;
import java.time.Instant;

public class LessonDto {
    
    private Long id;
    private Long courseId;
    private String title;
    private String description;
    private LessonType lessonType;
    private String content;
    private String fileUrl;
    private String fileName;
    private Long fileSize;
    private Integer durationMinutes;
    private Integer orderIndex;
    private Boolean isPublished;
    private Instant createdAt;
    private Instant updatedAt;
    private String formattedDuration;
    
    public LessonDto() {}
    
    public LessonDto(Long id, Long courseId, String title, String description, LessonType lessonType,
                    String content, String fileUrl, String fileName, Long fileSize, Integer durationMinutes,
                    Integer orderIndex, Boolean isPublished, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.courseId = courseId;
        this.title = title;
        this.description = description;
        this.lessonType = lessonType;
        this.content = content;
        this.fileUrl = fileUrl;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.durationMinutes = durationMinutes;
        this.orderIndex = orderIndex;
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
    
    public Instant getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
    
    public Instant getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public String getFormattedDuration() {
        return formattedDuration;
    }
    
    public void setFormattedDuration(String formattedDuration) {
        this.formattedDuration = formattedDuration;
    }
    
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
}

