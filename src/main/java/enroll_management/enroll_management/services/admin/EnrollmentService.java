package enroll_management.enroll_management.services.admin;

import enroll_management.enroll_management.Entities.Course;
import enroll_management.enroll_management.Entities.Enrollment;
import enroll_management.enroll_management.Entities.User;
import enroll_management.enroll_management.dto.admin.EnrollmentCreateDto;
import enroll_management.enroll_management.dto.admin.EnrollmentDto;
import enroll_management.enroll_management.enums.EnrollmentStatus;
import enroll_management.enroll_management.exception.ResourceNotFoundException;
import enroll_management.enroll_management.repositories.CourseRepository;
import enroll_management.enroll_management.repositories.EnrollmentRepository;
import enroll_management.enroll_management.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;



    // =========================
    // READ OPERATIONS
    // =========================

    // ADMIN: view all enrollments
    public List<EnrollmentDto> getAllEnrollments() {
        return enrollmentRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // ADMIN: view enrollments of any student
    public List<EnrollmentDto> getEnrollmentsByStudentId(Long studentId) {
        return enrollmentRepository.findByStudentId(studentId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // STUDENT: view own enrollments
    public List<EnrollmentDto> getMyEnrollments(Authentication authentication) {
        User student = getLoggedInUser(authentication);

        return enrollmentRepository.findByStudentId(student.getId())
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // ADMIN: get enrollment by ID
    public EnrollmentDto getEnrollmentById(Long id) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Enrollment", "id", id));
        return convertToDto(enrollment);
    }

    // =========================
    // CREATE ENROLLMENT
    // =========================
public EnrollmentDto createEnrollment(EnrollmentCreateDto dto,
                                      Authentication authentication) {

    boolean isAdmin = hasRole(authentication, "ROLE_ADMIN");
    User student;

    // =========================
    // DETERMINE STUDENT
    // =========================
    if (!isAdmin) {
        student = getLoggedInUser(authentication);
    } else {
        student = userRepository.findById(dto.getStudentId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("User", "id", dto.getStudentId()));
    }

    Course course = courseRepository.findById(dto.getCourseId())
            .orElseThrow(() ->
                    new ResourceNotFoundException("Course", "id", dto.getCourseId()));

    // =========================
    // BUSINESS RULE 1: DUPLICATE
    // =========================
    if (enrollmentRepository.existsByStudentIdAndCourseId(
            student.getId(), course.getId())) {

        throw new IllegalStateException(
                "Student is already enrolled in this course.");
    }

    // =========================
    // BUSINESS RULE 2: CAPACITY (CRITICAL FIX)
    // =========================
    long enrolledCount =
            enrollmentRepository.countByCourseIdAndStatus(
                    course.getId(), EnrollmentStatus.ENROLLED);

    if (enrolledCount >= course.getMaxCapacity()) {
        throw new IllegalStateException(
                "Course '" + course.getCourseName() + "' is already full.");
    }

    // =========================
    // CREATE ENROLLMENT
    // =========================
    Enrollment enrollment = new Enrollment();
    enrollment.setStudent(student);
    enrollment.setCourse(course);
    enrollment.setStatus(EnrollmentStatus.ENROLLED);
    enrollment.setEnrollmentDate(LocalDateTime.now());

    return convertToDto(enrollmentRepository.save(enrollment));
}

   
    // =========================
    // DELETE ENROLLMENT
    // =========================

    public void deleteEnrollment(Long id, Authentication authentication) {

        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Enrollment", "id", id));

        // STUDENT: ownership check
        if (hasRole(authentication, "ROLE_STUDENT")) {
            User student = getLoggedInUser(authentication);

            if (!enrollment.getStudent().getId().equals(student.getId())) {
                throw new AccessDeniedException(
                        "You can only delete your own enrollment");
            }
        }

        enrollmentRepository.delete(enrollment);
    }

    // =========================
    // HELPER METHODS
    // =========================

    private User getLoggedInUser(Authentication authentication) {
        return userRepository.findByUsername(authentication.getName())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User", "username", authentication.getName()));
    }

    private boolean hasRole(Authentication authentication, String role) {
        return authentication.getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals(role));
    }

  private EnrollmentDto convertToDto(Enrollment enrollment) {

    EnrollmentDto dto = new EnrollmentDto();

    dto.setId(enrollment.getId());
    dto.setEnrollmentDate(enrollment.getEnrollmentDate());

    dto.setStatus(enrollment.getStatus());

    // Student info
    dto.setStudentId(enrollment.getStudent().getId());
    dto.setStudentName(enrollment.getStudent().getFullName());

    // Course info
    dto.setCourseId(enrollment.getCourse().getId());
    dto.setCourseCode(enrollment.getCourse().getCourseCode());
    dto.setCourseName(enrollment.getCourse().getCourseName());

    return dto;
}


}
