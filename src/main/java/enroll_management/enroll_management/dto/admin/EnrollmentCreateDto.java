package enroll_management.enroll_management.dto.admin;


// The client only needs to provide the student and course IDs to create an enrollment.
// The system will handle the rest.
public class EnrollmentCreateDto {
    private Long studentId;
    private Long courseId;

    // Getters and Setters
    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    
    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }
}