package enroll_management.enroll_management.controllers;

import enroll_management.enroll_management.dto.CourseCreateUpdateDto;
import enroll_management.enroll_management.dto.CourseDto;
import enroll_management.enroll_management.services.CourseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping
    public List<CourseDto> getAllCourses() {
        return courseService.getAllCourses();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDto> getCourseById(@PathVariable(value = "id") Long courseId) {
        CourseDto course = courseService.getCourseById(courseId);
        return ResponseEntity.ok().body(course);
    }

    @PostMapping
    public ResponseEntity<CourseDto> createCourse(@Valid @RequestBody CourseCreateUpdateDto courseDto) {
        CourseDto createdCourse = courseService.createCourse(courseDto);
        return new ResponseEntity<>(createdCourse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseDto> updateCourse(
            @PathVariable(value = "id") Long courseId,
            @Valid @RequestBody CourseCreateUpdateDto courseDetails) {
        CourseDto updatedCourse = courseService.updateCourse(courseId, courseDetails);
        return ResponseEntity.ok(updatedCourse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable(value = "id") Long courseId) {
        courseService.deleteCourse(courseId);
        return ResponseEntity.noContent().build();
    }
}