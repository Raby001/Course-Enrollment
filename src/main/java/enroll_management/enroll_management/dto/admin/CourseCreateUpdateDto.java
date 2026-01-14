package enroll_management.enroll_management.dto.admin;

import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.constraints.*;

import enroll_management.enroll_management.enums.CourseStatus;

public class CourseCreateUpdateDto {

    @NotBlank(message="Course code is required")
    @Size(min=3, max=20, message = "Course code must be 3-20 characters")
    private String courseCode;

    @NotBlank(message="Course name is required")
    @Size(min=3, max=100, message = "Course name must be 3-100 characters")
    private String courseName;

    @Size(max=500, message = "Description can be up to 500 characters")
    private String description;

    @NotNull(message="Credits are required")
    @Min(value=1, message="Credits must be at least 1")
    @Max(value=10, message="Credits cannot exceed 10")
    private Integer credits;

    @NotNull(message="Max capacity is required")
    @Min(value=1, message="Max capacity must be at least 1")
    @Max(value=500, message="Max capacity cannot exceed 500")
    private Integer maxCapacity;


    private String courseImage;
    private MultipartFile imageFile;

    @NotNull(message="Lecturer ID is required")
    private Long lecturerId; // We only need the ID to link the User
    private CourseStatus status;
    // Getters and Setters
    public MultipartFile getImageFile() {
        return imageFile;
    }
    public void setImageFile(MultipartFile imageFile) {
        this.imageFile = imageFile;
    }

    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }

    public String getCourseImage() {return courseImage;}
    public void setCourseImage(String courseImage) {this.courseImage = courseImage;}

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getCredits() { return credits; }
    public void setCredits(Integer credits) { this.credits = credits; }

    public Integer getMaxCapacity() { return maxCapacity; }
    public void setMaxCapacity(Integer maxCapacity) { this.maxCapacity = maxCapacity; }
    
    public Long getLecturerId() { return lecturerId; }
    public void setLecturerId(Long lecturerId) { this.lecturerId = lecturerId; }

    public CourseStatus getStatus(){
        return status;
    }
    public void setStatus(CourseStatus status){
        this.status = status;
    }
}