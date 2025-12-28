package enroll_management.enroll_management.services.admin;

import enroll_management.enroll_management.Entities.Course;
import enroll_management.enroll_management.Entities.User;
import enroll_management.enroll_management.dto.admin.CourseCreateUpdateDto;
import enroll_management.enroll_management.dto.admin.CourseDto;
import enroll_management.enroll_management.exception.ResourceNotFoundException;
import enroll_management.enroll_management.repositories.CourseRepository;
import enroll_management.enroll_management.repositories.UserRepository; // You will need this
import org.modelmapper.ModelMapper;
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

    @Autowired
    private ModelMapper modelMapper;

    public List<CourseDto> getAllCourses() {
        return courseRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public CourseDto getCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", id));
        return convertToDto(course);
    }

    public CourseDto createCourse(CourseCreateUpdateDto courseDto) {
        // Convert DTO to Entity
        Course newCourse = convertToEntity(courseDto);
        
        // Fetch the lecturer (User) and set it
        User lecturer = userRepository.findById(courseDto.getLecturerId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", courseDto.getLecturerId()));
        newCourse.setLecturer(lecturer);

        Course savedCourse = courseRepository.save(newCourse);
        return convertToDto(savedCourse);
    }

    public CourseDto updateCourse(Long id, CourseCreateUpdateDto courseDetails) {
        Course existingCourse = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", id));

        // Update fields from DTO
        existingCourse.setCourseCode(courseDetails.getCourseCode());
        existingCourse.setCourseName(courseDetails.getCourseName());
        existingCourse.setDescription(courseDetails.getDescription());
        existingCourse.setCredits(courseDetails.getCredits());
        existingCourse.setMaxCapacity(courseDetails.getMaxCapacity());

        // Update lecturer if changed
        if (!existingCourse.getLecturer().getId().equals(courseDetails.getLecturerId())) {
            User newLecturer = userRepository.findById(courseDetails.getLecturerId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", courseDetails.getLecturerId()));
            existingCourse.setLecturer(newLecturer);
        }

        Course updatedCourse = courseRepository.save(existingCourse);
        return convertToDto(updatedCourse);
    }

    public void deleteCourse(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", id));
        courseRepository.delete(course);
    }

    // --- Helper Methods for Conversion ---

    private CourseDto convertToDto(Course course) {
        CourseDto courseDto = modelMapper.map(course, CourseDto.class);
        // Manually map fields that ModelMapper can't handle directly
        courseDto.setLecturerId(course.getLecturer().getId());
        courseDto.setLecturerName(course.getLecturer().getFullName()); // Assuming User has getFullName()
        courseDto.setCurrentEnrollmentCount(course.getCurrentEnrollmentCount());
        return courseDto;
    }

    private Course convertToEntity(CourseCreateUpdateDto courseDto) {
        // We map to a base Course entity, relationships are handled in the service method
        return modelMapper.map(courseDto, Course.class);
    }
}