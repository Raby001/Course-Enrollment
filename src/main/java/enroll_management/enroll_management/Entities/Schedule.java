package enroll_management.enroll_management.Entities;

import jakarta.persistence.*;

import java.time.LocalTime;
import java.time.DayOfWeek;

import java.time.LocalDateTime;

@Entity
@Table(name = "schedules",
       uniqueConstraints = {
        // Prevent double-booking a classroom
        @UniqueConstraint(columnNames = {"classroom_id", "academic_year", 
                                        "semester", "day_of_week", "start_time"},
                        name = "uk_classroom_schedule"),
        // Prevent scheduling same course at same time
        @UniqueConstraint(columnNames = {"course_id", "academic_year", 
                                        "semester", "day_of_week", "start_time"},
                        name = "uk_course_schedule")
       },
       indexes = {
           @Index(columnList = "course_id", name = "idx_schedule_course"),
           @Index(columnList = "classroom_id", name = "idx_schedule_classroom"),
           @Index(columnList = "academic_year,semester", name = "idx_schedule_term"),
           @Index(columnList = "day_of_week,start_time", name = "idx_schedule_time"),
           @Index(columnList = "status", name = "idx_schedule_status")
       })
public class Schedule {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long id;
    
    // ===== RELATIONSHIPS =====
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "course_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_schedule_course"))
    private Course course;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "classroom_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_schedule_classroom"))
    private Classroom classroom;
    
    // ===== SCHEDULE DETAILS =====
    @Column(name = "academic_year", nullable = false)
    private Integer academicYear; 
    
    @Column(name = "semester", nullable = false, length = 20)
    private String semester; 
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DayOfWeek dayOfWeek;
    
    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;
    
    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;
    
    @Column(name = "status", length = 20)
    private String status = "SCHEDULED"; 
    
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    // ===== LIFECYCLE CALLBACKS =====
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    
    // ===== CONSTRUCTORS =====
    public Schedule() {}
    
    public Schedule(Course course, Classroom classroom, Integer academicYear, 
                   String semester, DayOfWeek dayOfWeek, 
                   LocalTime startTime, LocalTime endTime) {
        this.course = course;
        this.classroom = classroom;
        this.academicYear = academicYear;
        this.semester = semester;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }
    
    // ===== GETTERS & SETTERS =====
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }
    
    public Classroom getClassroom() { return classroom; }
    public void setClassroom(Classroom classroom) { this.classroom = classroom; }
    
    public Integer getAcademicYear() { return academicYear; }
    public void setAcademicYear(Integer academicYear) { this.academicYear = academicYear; }
    
    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }
    
    public DayOfWeek getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(DayOfWeek dayOfWeek) { this.dayOfWeek = dayOfWeek; }
    
    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }
    
    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    // ===== BUSINESS LOGIC METHODS =====
    public boolean isActive() {
        return "SCHEDULED".equals(status);
    }
    
    public boolean isCancelled() {
        return "CANCELLED".equals(status);
    }
    
    public boolean overlapsWith(LocalTime otherStart, LocalTime otherEnd) {
        // Check if time ranges overlap
        return startTime.isBefore(otherEnd) && endTime.isAfter(otherStart);
    }
    
    public boolean isSameTimeSlot(DayOfWeek otherDay, LocalTime otherStart) {
        return dayOfWeek == otherDay && startTime.equals(otherStart);
    }
    
    public String getTimeSlot() {
        return startTime + " - " + endTime;
    }
    
    public String getTerm() {
        return semester + " " + academicYear;
    }
    
    public Integer getDurationInHours() {
        return endTime.getHour() - startTime.getHour();
    }
    
    // Validate that end time is after start time
    public boolean isValidTimeRange() {
        return endTime.isAfter(startTime);
    }
    
    // Check if classroom has required capacity
    public boolean classroomHasSufficientCapacity() {
        return classroom != null && 
               classroom.getCapacity() >= course.getMaxCapacity();
    }
    
    public boolean classroomHasRequiredEquipment() {
        return true;
    }
    
    @Override
    public String toString() {
        return "Schedule{id=" + id + 
               ", course=" + (course != null ? course.getCourseCode() : "null") +
               ", classroom=" + (classroom != null ? classroom.getFullLocation() : "null") +
               ", day=" + dayOfWeek + 
               ", time=" + getTimeSlot() +
               ", term='" + getTerm() + "'}";
    }
}
