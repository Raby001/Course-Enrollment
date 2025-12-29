package enroll_management.enroll_management.services.admin;

import enroll_management.enroll_management.Entities.Course;
import enroll_management.enroll_management.Entities.User;
import enroll_management.enroll_management.dto.admin.CourseCreateUpdateDto;
import enroll_management.enroll_management.dto.admin.CourseDto;
import enroll_management.enroll_management.enums.CourseStatus;
import enroll_management.enroll_management.exception.ResourceNotFoundException;
import enroll_management.enroll_management.repositories.CourseRepository;
import enroll_management.enroll_management.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

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

    // =========================
    // CREATE
    // =========================

    public CourseDto createCourse(CourseCreateUpdateDto dto) {

        Course course = convertToEntity(dto);

        // lecturer is REQUIRED in your DB
        User lecturer = userRepository.findById(dto.getLecturerId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("User", "id", dto.getLecturerId()));

        course.setLecturer(lecturer);

        // default status when created
        course.setStatus(CourseStatus.DRAFT);

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

        course.setCourseCode(dto.getCourseCode());
        course.setCourseName(dto.getCourseName());
        course.setDescription(dto.getDescription());
        course.setCredits(dto.getCredits());
        course.setMaxCapacity(dto.getMaxCapacity());
        course.setCourseImage(dto.getCourseImage());

        // update lecturer only if changed
        if (dto.getLecturerId() != null &&
                !course.getLecturer().getId().equals(dto.getLecturerId())) {

            User lecturer = userRepository.findById(dto.getLecturerId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException("User", "id", dto.getLecturerId()));

            course.setLecturer(lecturer);
        }

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
    // MAPPING METHODS
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

    private Course convertToEntity(CourseCreateUpdateDto dto) {

        Course course = new Course();

        course.setCourseCode(dto.getCourseCode());
        course.setCourseName(dto.getCourseName());
        course.setDescription(dto.getDescription());
        course.setCredits(dto.getCredits());
        course.setMaxCapacity(dto.getMaxCapacity());
        course.setCourseImage(dto.getCourseImage());

        return course;
    }
}
