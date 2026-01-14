package enroll_management.enroll_management.dto.admin;

import org.springframework.web.multipart.MultipartFile;

import enroll_management.enroll_management.enums.CourseStatus;

public class CourseCreateUpdateDto {
    private String courseCode;
    private String courseName;
    private String description;
    private Integer credits;
    private Integer maxCapacity;
    private String courseImage;
    private MultipartFile imageFile;
    private Long lecturerId;
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