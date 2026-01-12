package enroll_management.enroll_management.services.student;

import enroll_management.enroll_management.Entities.Enrollment;
import enroll_management.enroll_management.Entities.User;
import enroll_management.enroll_management.dto.admin.CourseDto;
import enroll_management.enroll_management.dto.admin.EnrollmentDto;
import enroll_management.enroll_management.repositories.EnrollmentRepository;
import enroll_management.enroll_management.repositories.UserRepository;
import enroll_management.enroll_management.services.admin.CourseService;
import enroll_management.enroll_management.services.common.ImageUploadService;

import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class StudentProfileService {

    private final CourseService courseService;

    private final UserRepository userRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final ImageUploadService imageUploadService;

    public StudentProfileService(
            UserRepository userRepository,
            EnrollmentRepository enrollmentRepository,
            ImageUploadService imageUploadService,
            CourseService courseService) {
        this.userRepository = userRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.imageUploadService = imageUploadService;
        this.courseService = courseService; 
    }

    // =========================
    // GET CURRENT LOGGED-IN USER
    // =========================
    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    // =========================
    // UPDATE PROFILE INFO
    // =========================
    public void updateProfile(String firstName, String lastName, String email, LocalDate dob) {
        User user = getCurrentUser();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);

        // ✅ Only update DOB if provided
        if (dob != null) {
            user.setDob(dob);
        }

        userRepository.save(user);
    }

    // =========================
    // UPLOAD PROFILE IMAGE
    // =========================
    public void uploadProfileImage(MultipartFile image) throws IOException {
        User user = getCurrentUser();
        String imageUrl = imageUploadService.uploadImage(image);
        user.setUserImage(imageUrl);
        userRepository.save(user);
    }

    // =========================
    // CHECK IF STUDENT IS ENROLLED IN A COURSE
    // =========================
    public boolean isEnrolled(Long courseId) {
        User student = getCurrentUser();
        return enrollmentRepository.existsByStudentIdAndCourseId(student.getId(), courseId);
    }

    public List<CourseDto> getEnrolledCourses() {
        User student = getCurrentUser();
        List<Enrollment> enrollments = enrollmentRepository.findByStudentId(student.getId());
        
        return enrollments.stream()
                .map(enrollment -> courseService.getCourseById(enrollment.getCourse().getId()))
                .collect(Collectors.toList());
    }

    public List<EnrollmentDto> getMyEnrollments() {
        User student = getCurrentUser();
        List<Enrollment> enrollments = enrollmentRepository.findByStudentId(student.getId());
        return enrollments.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public void dropEnrollment(Long enrollmentId) {
        User student = getCurrentUser();
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new IllegalStateException("Enrollment not found"));

        if (!enrollment.getStudent().getId().equals(student.getId())) {
            throw new IllegalStateException("You can only drop your own enrollments");
        }

        enrollmentRepository.delete(enrollment);
    }

    private EnrollmentDto convertToDto(Enrollment enrollment) {
        EnrollmentDto dto = new EnrollmentDto();
        dto.setId(enrollment.getId());
        dto.setEnrollmentDate(enrollment.getEnrollmentDate());
        dto.setStatus(enrollment.getStatus());
        
        // Student
        dto.setStudentId(enrollment.getStudent().getId());
        dto.setStudentName(enrollment.getStudent().getFullName());
        
        // Course
        dto.setCourseId(enrollment.getCourse().getId());
        dto.setCourseCode(enrollment.getCourse().getCourseCode());
        dto.setCourseName(enrollment.getCourse().getCourseName());
        dto.setCredits(enrollment.getCourse().getCredits());
        
        return dto;
    }

}
