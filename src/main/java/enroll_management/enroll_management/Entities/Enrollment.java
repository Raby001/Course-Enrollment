package enroll_management.enroll_management.Entities;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import enroll_management.enroll_management.enums.EnrollmentStatus;

@Entity
@Table(name = "enrollments",
       uniqueConstraints = {
           @UniqueConstraint(columnNames = {"student_id", "course_id"}, 
                            name = "uk_student_course")
       },
       indexes = {
           @Index(columnList = "student_id", name = "idx_enrollment_student"),
           @Index(columnList = "course_id", name = "idx_enrollment_course"),
           @Index(columnList = "status", name = "idx_enrollment_status")
       })
public class Enrollment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "enrollment_id")
    private Long id;
    
    // ===== RELATIONSHIPS (JOINING TABLE) =====
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_enrollment_student"))
    private User student;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "course_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_enrollment_course"))
    private Course course;
    
    // ===== ENROLLMENT DETAILS =====
    @Column(name = "enrollment_date", updatable = false)
    private LocalDateTime enrollmentDate;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private EnrollmentStatus status = EnrollmentStatus.PENDING;
    
    @Column(name = "grade", length = 2)
    private String grade;
    
    @Column(name = "final_score", precision = 5, scale = 2)
    private BigDecimal finalScore;
    
    @Column(name = "attendance_percentage", precision = 5, scale = 2)
    private BigDecimal attendancePercentage;
    
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // ===== LIFECYCLE CALLBACKS =====
    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        if (enrollmentDate == null) {
            enrollmentDate = now;
        }
        createdAt = now;
        updatedAt = now;
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    // ===== CONSTRUCTORS =====
    public Enrollment() {}
    
    public Enrollment(User student, Course course) {
        this.student = student;
        this.course = course;
        // Automatically set dates in @PrePersist
    }
    
    // ===== GETTERS & SETTERS =====
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getStudent() { return student; }
    public void setStudent(User student) { this.student = student; }
    
    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }
    
    public LocalDateTime getEnrollmentDate() { return enrollmentDate; }
    public void setEnrollmentDate(LocalDateTime enrollmentDate) { this.enrollmentDate = enrollmentDate; }
    
    public EnrollmentStatus getStatus() { return status; }
    public void setStatus(EnrollmentStatus status) { this.status = status; }
    
    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }
    
    public BigDecimal getFinalScore() { return finalScore; }
    public void setFinalScore(BigDecimal finalScore) { this.finalScore = finalScore; }
    
    public BigDecimal getAttendancePercentage() { return attendancePercentage; }
    public void setAttendancePercentage(BigDecimal attendancePercentage) { this.attendancePercentage = attendancePercentage; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    // ===== HELPER METHODS =====
    public boolean isActive() {
        return EnrollmentStatus.APPROVED.equals(status);
    }
    
    public boolean isCompleted() {
        return EnrollmentStatus.COMPLETED.equals(status);
    }
    
    @Override
    public String toString() {
        return "Enrollment{id=" + id + ", student=" + 
               (student != null ? student.getUsername() : "null") + 
               ", course=" + (course != null ? course.getCourseCode() : "null") + 
               ", status='" + status + "', grade='" + grade + "'}";
    }
}