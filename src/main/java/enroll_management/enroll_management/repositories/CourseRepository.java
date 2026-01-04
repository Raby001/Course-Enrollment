package enroll_management.enroll_management.repositories;
import enroll_management.enroll_management.Entities.Course;
import enroll_management.enroll_management.enums.CourseStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    // Custom query method to find a course by its unique course code
    Optional<Course> findByCourseCode(String courseCode);

    // Count all current courses
    long count();

    // Count courses by status
    long countByStatus(CourseStatus status);

    // Custom query methods for specific counts
    default long countActiveCourses() {
        return countByStatus(CourseStatus.ACTIVE);
    }

    default long countDraftCourses() {
        return countByStatus(CourseStatus.DRAFT);
    }

    default long countPendingCourses() {
        return countByStatus(CourseStatus.PENDING);
    }
}
