package com.lms.dto;

import com.lms.model.DifficultyLevel;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public class CourseDto {
    
    private Long id;
    private String title;
    private String description;
    private Long instructorId;
    private String instructorName;
    private String category;
    private DifficultyLevel difficultyLevel;
    private BigDecimal price;
    private Boolean isPublished;
    private String thumbnailUrl;
    private Instant createdAt;
    private Instant updatedAt;
    private int enrollmentCount;
    private int lessonCount;
    private List<LessonDto> lessons;
    private List<CategoryDto> categories;
    
    public CourseDto() {}
    
    public CourseDto(Long id, String title, String description, Long instructorId, String instructorName,
                    String category, DifficultyLevel difficultyLevel, BigDecimal price, Boolean isPublished,
                    String thumbnailUrl, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.instructorId = instructorId;
        this.instructorName = instructorName;
        this.category = category;
        this.difficultyLevel = difficultyLevel;
        this.price = price;
        this.isPublished = isPublished;
        this.thumbnailUrl = thumbnailUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
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
    
    public Long getInstructorId() {
        return instructorId;
    }
    
    public void setInstructorId(Long instructorId) {
        this.instructorId = instructorId;
    }
    
    public String getInstructorName() {
        return instructorName;
    }
    
    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public DifficultyLevel getDifficultyLevel() {
        return difficultyLevel;
    }
    
    public void setDifficultyLevel(DifficultyLevel difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }
    
    public BigDecimal getPrice() {
        return price;
    }
    
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
    public Boolean getIsPublished() {
        return isPublished;
    }
    
    public void setIsPublished(Boolean isPublished) {
        this.isPublished = isPublished;
    }
    
    public String getThumbnailUrl() {
        return thumbnailUrl;
    }
    
    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
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
    
    public int getEnrollmentCount() {
        return enrollmentCount;
    }
    
    public void setEnrollmentCount(int enrollmentCount) {
        this.enrollmentCount = enrollmentCount;
    }
    
    public int getLessonCount() {
        return lessonCount;
    }
    
    public void setLessonCount(int lessonCount) {
        this.lessonCount = lessonCount;
    }
    
    public List<LessonDto> getLessons() {
        return lessons;
    }
    
    public void setLessons(List<LessonDto> lessons) {
        this.lessons = lessons;
    }
    
    public List<CategoryDto> getCategories() {
        return categories;
    }
    
    public void setCategories(List<CategoryDto> categories) {
        this.categories = categories;
    }
    
    public boolean isFree() {
        return price == null || price.compareTo(BigDecimal.ZERO) == 0;
    }

    public void setPublished(boolean published) {
		// TODO Auto-generated method stub
		
	}

	public void setDifficulty(DifficultyLevel beginner) {
		// TODO Auto-generated method stub
		
	}

	public void setDuration(int i) {
		// TODO Auto-generated method stub
		
	}
}

