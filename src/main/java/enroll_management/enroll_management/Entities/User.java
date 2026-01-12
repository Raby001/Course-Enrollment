package enroll_management.enroll_management.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;
import enroll_management.enroll_management.enums.UserStatus;

@Entity
@Table(name = "users",
       uniqueConstraints = {
           @UniqueConstraint(columnNames = "password_hash", name = "uk_password_hash"),
           @UniqueConstraint(columnNames = "email", name = "uk_email")
       },
       indexes = {
           @Index(columnList = "role_id", name = "idx_user_role"),
           @Index(columnList = "status", name = "idx_user_status")
       })
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    
    @Column(name = "username", nullable = false, length = 50)
    private String username;
    
    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;
    
    @Column(name = "email", nullable = false, length = 100)
    private String email;
    
    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;
    
    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(name = "dob", nullable = false)
    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dob;

    @Column(name = "profile_image", length = 500, nullable = true)
    private String profileImage;

    public String getFormattedDob() {
        if (dob == null) {
            return "";
        }
        return dob.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    // ===== RELATIONSHIPS =====
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "role_id", nullable = false, 
                foreignKey = @ForeignKey(name = "fk_user_role"))
    private Role role;
    
    // ===== STATUS & TIMESTAMPS =====
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private UserStatus status = UserStatus.ACTIVE;
    
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "user_image", nullable = true, length = 500)
    private String userImage;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // ===== BIDIRECTIONAL RELATIONSHIPS =====
    @OneToMany(mappedBy = "lecturer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Course> taughtCourses = new HashSet<>();
    
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
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
    public User() {}
    
    public User(String username, String passwordHash, String email, 
                String firstName, String lastName, LocalDate dob, Role role) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.role = role;
    }
    
    // ===== GETTERS & SETTERS =====
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getUserImage(){return userImage;}
    public void setUserImage(String userImage){this.userImage = userImage; }
    
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public LocalDate getDob() { return dob; }
    public void setDob(LocalDate dob) { this.dob = dob; }
    
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    
    public UserStatus getStatus() { return status; }
    public void setStatus(UserStatus status) { this.status = status; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public Set<Course> getTaughtCourses() { return taughtCourses; }
    public void setTaughtCourses(Set<Course> taughtCourses) { this.taughtCourses = taughtCourses; }
    
    public Set<Enrollment> getEnrollments() { return enrollments; }
    public void setEnrollments(Set<Enrollment> enrollments) { this.enrollments = enrollments; }
    
    // ===== HELPER METHODS =====
    public String getFullName() {
        return firstName + " " + lastName;
    }
    
    public void addCourse(Course course) {
        taughtCourses.add(course);
        course.setLecturer(this);
    }
    
    public void removeCourse(Course course) {
        taughtCourses.remove(course);
        course.setLecturer(null);
    }
    
    public void addEnrollment(Enrollment enrollment) {
        enrollments.add(enrollment);
        enrollment.setStudent(this);
    }
    
    public void removeEnrollment(Enrollment enrollment) {
        enrollments.remove(enrollment);
        enrollment.setStudent(null);
    }
    
    @Override
    public String toString() {
        return "User{id=" + id + ", username='" + username + "', email='" + email + 
               "', name='" + getFullName() + "', role=" + (role != null ? role.getName() : "null") + "}";
    }
}
