package enroll_management.enroll_management.dto.admin;

import jakarta.validation.constraints.*;


public class EnrollmentCreateDto {

    @NotNull(message="Student ID is required")
    private Long studentId;
    @NotNull(message="Course ID is required")
    private Long courseId;

    // Getters and Setters
    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    
    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }
}