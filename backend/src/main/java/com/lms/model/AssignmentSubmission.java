package com.lms.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "assignment_submissions", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"assignment_id", "student_id"}))
@EntityListeners(AuditingEntityListener.class)
public class AssignmentSubmission {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignment_id", nullable = false)
    private Assignment assignment;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;
    
    @Column(name = "submission_text", columnDefinition = "TEXT")
    private String submissionText;
    
    @Size(max = 500)
    @Column(name = "file_url")
    private String fileUrl;
    
    @Size(max = 255)
    @Column(name = "file_name")
    private String fileName;
    
    @CreatedDate
    @Column(name = "submitted_at", nullable = false, updatable = false)
    private LocalDateTime submittedAt;
    
    @DecimalMin(value = "0.0", inclusive = true)
    @DecimalMax(value = "100.0", inclusive = true)
    @Column(precision = 5, scale = 2)
    private BigDecimal grade;
    
    @Column(columnDefinition = "TEXT")
    private String feedback;
    
    @Column(name = "graded_at")
    private LocalDateTime gradedAt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "graded_by")
    private User gradedBy;
    
    // Constructors
    public AssignmentSubmission() {}
    
    public AssignmentSubmission(Assignment assignment, User student, String submissionText) {
        this.assignment = assignment;
        this.student = student;
        this.submissionText = submissionText;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Assignment getAssignment() {
        return assignment;
    }
    
    public void setAssignment(Assignment assignment) {
        this.assignment = assignment;
    }
    
    public User getStudent() {
        return student;
    }
    
    public void setStudent(User student) {
        this.student = student;
    }
    
    public String getSubmissionText() {
        return submissionText;
    }
    
    public void setSubmissionText(String submissionText) {
        this.submissionText = submissionText;
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
    
    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }
    
    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }
    
    public BigDecimal getGrade() {
        return grade;
    }
    
    public void setGrade(BigDecimal grade) {
        this.grade = grade;
    }
    
    public String getFeedback() {
        return feedback;
    }
    
    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
    
    public LocalDateTime getGradedAt() {
        return gradedAt;
    }
    
    public void setGradedAt(LocalDateTime gradedAt) {
        this.gradedAt = gradedAt;
    }
    
    public User getGradedBy() {
        return gradedBy;
    }
    
    public void setGradedBy(User gradedBy) {
        this.gradedBy = gradedBy;
    }
    
    // Helper methods
    public boolean isGraded() {
        return grade != null;
    }
    
    public boolean isLate() {
        if (assignment == null || assignment.getDueDate() == null) {
            return false;
        }
        return submittedAt.isAfter(assignment.getDueDate());
    }
    
    public void grade(BigDecimal grade, String feedback, User grader) {
        this.grade = grade;
        this.feedback = feedback;
        this.gradedBy = grader;
        this.gradedAt = LocalDateTime.now();
    }
    
    public String getGradeLetter() {
        if (grade == null) {
            return "N/A";
        }
        
        double gradeValue = grade.doubleValue();
        if (gradeValue >= 97) return "A+";
        if (gradeValue >= 93) return "A";
        if (gradeValue >= 90) return "A-";
        if (gradeValue >= 87) return "B+";
        if (gradeValue >= 83) return "B";
        if (gradeValue >= 80) return "B-";
        if (gradeValue >= 77) return "C+";
        if (gradeValue >= 73) return "C";
        if (gradeValue >= 70) return "C-";
        if (gradeValue >= 67) return "D+";
        if (gradeValue >= 63) return "D";
        if (gradeValue >= 60) return "D-";
        return "F";
    }
    
    public boolean hasFileSubmission() {
        return fileUrl != null && !fileUrl.trim().isEmpty();
    }
    
    public boolean hasTextSubmission() {
        return submissionText != null && !submissionText.trim().isEmpty();
    }
}

