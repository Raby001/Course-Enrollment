package enroll_management.enroll_management.services.admin;

import enroll_management.enroll_management.Entities.Course;
import enroll_management.enroll_management.Entities.User;
import enroll_management.enroll_management.dto.admin.CourseCreateUpdateDto;
import enroll_management.enroll_management.dto.admin.CourseDto;
import enroll_management.enroll_management.enums.CourseStatus;
import enroll_management.enroll_management.exception.ResourceNotFoundException;
import enroll_management.enroll_management.repositories.CourseRepository;
import enroll_management.enroll_management.repositories.UserRepository;
import enroll_management.enroll_management.services.common.ImageUploadService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImageUploadService imageUploadService;

    // =========================
    // READ
    // =========================

    public List<CourseDto> getAllCourses() {
        return courseRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public CourseDto getCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Course", "id", id));
        return convertToDto(course);
    }

    public List<CourseDto> getActiveCourses() {
        return courseRepository.findByStatus(CourseStatus.ACTIVE)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // =========================
    // CREATE
    // =========================

    public CourseDto createCourse(CourseCreateUpdateDto dto) {

        Course course = new Course();

        // ===== BASIC FIELDS =====
        course.setCourseCode(dto.getCourseCode());
        course.setCourseName(dto.getCourseName());
        course.setDescription(dto.getDescription());
        course.setCredits(dto.getCredits());
        course.setMaxCapacity(dto.getMaxCapacity());

        // ===== STATUS =====
        course.setStatus(
                dto.getStatus() != null ? dto.getStatus() : CourseStatus.DRAFT
        );

        // ===== LECTURER =====
        User lecturer = userRepository.findById(dto.getLecturerId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("User", "id", dto.getLecturerId()));
        course.setLecturer(lecturer);

        // ===== IMAGE UPLOAD (TRY–CATCH) =====
        if (dto.getImageFile() != null && !dto.getImageFile().isEmpty()) {
            try {
                String imageUrl = imageUploadService.uploadImage(dto.getImageFile());
                course.setCourseImage(imageUrl);
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload course image", e);
            }
        }

        Course saved = courseRepository.save(course);
        return convertToDto(saved);
    }

    // =========================
    // UPDATE
    // =========================

    public CourseDto updateCourse(Long id, CourseCreateUpdateDto dto) {

        Course course = courseRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Course", "id", id));

        // ===== BASIC FIELDS =====
        course.setCourseCode(dto.getCourseCode());
        course.setCourseName(dto.getCourseName());
        course.setDescription(dto.getDescription());
        course.setCredits(dto.getCredits());
        course.setMaxCapacity(dto.getMaxCapacity());

        // ===== STATUS =====
        if (dto.getStatus() != null) {
            course.setStatus(dto.getStatus());
        }

        // ===== LECTURER =====
        if (dto.getLecturerId() != null &&
                !course.getLecturer().getId().equals(dto.getLecturerId())) {

            User lecturer = userRepository.findById(dto.getLecturerId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException("User", "id", dto.getLecturerId()));

            course.setLecturer(lecturer);
        }

        // ===== IMAGE REPLACEMENT (TRY–CATCH) =====
        if (dto.getImageFile() != null && !dto.getImageFile().isEmpty()) {
            try {
                String imageUrl = imageUploadService.uploadImage(dto.getImageFile());
                course.setCourseImage(imageUrl);
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload course image", e);
            }
        }
        // else → keep old image automatically

        Course updated = courseRepository.save(course);
        return convertToDto(updated);
    }

    // =========================
    // DELETE
    // =========================

    public void deleteCourse(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Course", "id", id));
        courseRepository.delete(course);
    }

    // =========================
    // SEARCH + PAGINATION
    // =========================

    public Page<CourseDto> searchCourses(String keyword, int page) {
        return courseRepository
                .findByCourseNameContainingIgnoreCase(
                        keyword,
                        PageRequest.of(page, 5)
                )
                .map(this::convertToDto);
    }

    // =========================
    // DTO MAPPING
    // =========================

    private CourseDto convertToDto(Course course) {

        CourseDto dto = new CourseDto();

        dto.setId(course.getId());
        dto.setCourseCode(course.getCourseCode());
        dto.setCourseName(course.getCourseName());
        dto.setDescription(course.getDescription());
        dto.setCredits(course.getCredits());
        dto.setMaxCapacity(course.getMaxCapacity());
        dto.setCourseImage(course.getCourseImage());

        if (course.getStatus() != null) {
            dto.setStatus(course.getStatus().name());
        }

        if (course.getLecturer() != null) {
            dto.setLecturerId(course.getLecturer().getId());
            dto.setLecturerName(course.getLecturer().getFullName());
        } else {
            dto.setLecturerName("Not assigned");
        }

        dto.setCurrentEnrollmentCount(course.getCurrentEnrollmentCount());

        return dto;
    }
}
