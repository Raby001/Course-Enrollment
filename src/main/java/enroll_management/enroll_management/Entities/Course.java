package enroll_management.enroll_management.Entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import enroll_management.enroll_management.enums.CourseStatus;

@Entity
@Table(name = "courses",
       uniqueConstraints = {
           @UniqueConstraint(columnNames = "course_code", name = "uk_course_code")
       },
       indexes = {
           @Index(columnList = "lecturer_id", name = "idx_course_lecturer"),
           @Index(columnList = "status", name = "idx_course_status")
       })
public class Course {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private Long id;
    
    @Column(name = "course_code", nullable = false, length = 20)
    private String courseCode;
    
    @Column(name = "course_image", nullable = true, length = 500)
    private String courseImage;
    

    @Column(name = "course_name", nullable = false, length = 100)
    private String courseName;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "credits", nullable = false)
    private Integer credits;
    
    @Column(name = "max_capacity", nullable = false)
    private Integer maxCapacity;

    // ===== RELATIONSHIPS =====
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "lecturer_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_course_lecturer"))
    private User lecturer;
    
    // ===== STATUS & TIMESTAMPS =====
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private CourseStatus status = CourseStatus.ACTIVE;
    
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // ===== BIDIRECTIONAL RELATIONSHIPS =====
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Schedule> schedules = new HashSet<>();
    
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Enrollment> enrollments = new HashSet<>();
    
    // ===== LIFECYCLE CALLBACKS =====
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    // ===== CONSTRUCTORS =====
    public Course() {}
    
    public Course(String courseCode, String courseName, Integer credits, 
                  Integer maxCapacity, User lecturer, String courseImage) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.credits = credits;
        this.maxCapacity = maxCapacity;
        this.lecturer = lecturer;
    }
    
    // ===== GETTERS & SETTERS =====
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }

    public String getCourseImage(){return courseImage;}
    public void setCourseImage(String courseImage){this.courseImage = courseImage; }
    
    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public Integer getCredits() { return credits; }
    public void setCredits(Integer credits) { this.credits = credits; }
    
    public Integer getMaxCapacity() { return maxCapacity; }
    public void setMaxCapacity(Integer maxCapacity) { this.maxCapacity = maxCapacity; }
    
    public User getLecturer() { return lecturer; }
    public void setLecturer(User lecturer) { this.lecturer = lecturer; }
    
    public CourseStatus getStatus() { return status; }
    public void setStatus(CourseStatus status) { this.status = status; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    
    public Set<Schedule> getSchedules() { return schedules; }
    public void setSchedules(Set<Schedule> schedules) { this.schedules = schedules; }
    
    public Set<Enrollment> getEnrollments() { return enrollments; }
    public void setEnrollments(Set<Enrollment> enrollments) { this.enrollments = enrollments; }
    
    // ===== HELPER METHODS =====
    @SuppressWarnings("unlikely-arg-type")
    public Integer getCurrentEnrollmentCount() {
        return (int) enrollments.stream()
                .filter(e -> "ENROLLED".equals(e.getStatus()))
                .count();
    }
    
    public Boolean isFull() {
        return getCurrentEnrollmentCount() >= maxCapacity;
    }
    
    public void addSchedule(Schedule schedule) {
        schedules.add(schedule);
        schedule.setCourse(this);
    }
    
    public void removeSchedule(Schedule schedule) {
        schedules.remove(schedule);
        schedule.setCourse(null);
    }
    
    public void addEnrollment(Enrollment enrollment) {
        enrollments.add(enrollment);
        enrollment.setCourse(this);
    }
    
    public void removeEnrollment(Enrollment enrollment) {
        enrollments.remove(enrollment);
        enrollment.setCourse(null);
    }
    
    @Override
    public String toString() {
        return "Course{id=" + id + ", code='" + courseCode + "', name='" + courseName + 
               "', credits=" + credits + ", lecturer=" + 
               (lecturer != null ? lecturer.getFullName() : "null") + "}";
    }
}
