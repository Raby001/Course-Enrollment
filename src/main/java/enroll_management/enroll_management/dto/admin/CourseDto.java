package enroll_management.enroll_management.dto.admin;

import java.time.LocalDateTime;

public class CourseDto {
    private Long id;
    private String courseCode;
    private String courseName;
    private String description;
    private Integer credits;
    private Integer maxCapacity;
    private Long lecturerId;
    private String lecturerName; // Assuming User has a getFullName() method
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer currentEnrollmentCount;
    private String courseImage;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }

    public String getCourseImage() {return courseImage;}

    public void setCourseImage(String courseImage) {this.courseImage = courseImage;}

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getCredits() { return credits; }
    public void setCredits(Integer credits) { this.credits = credits; }

    public Integer getMaxCapacity() { return maxCapacity; }
    public void setMaxCapacity(Integer maxCapacity) { this.maxCapacity = maxCapacity; }

    public Long getLecturerId() { return lecturerId; }
    public void setLecturerId(Long lecturerId) { this.lecturerId = lecturerId; }

    public String getLecturerName() { return lecturerName; }
    public void setLecturerName(String lecturerName) { this.lecturerName = lecturerName; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public Integer getCurrentEnrollmentCount() { return currentEnrollmentCount; }
    public void setCurrentEnrollmentCount(Integer currentEnrollmentCount) { this.currentEnrollmentCount = currentEnrollmentCount; }
}