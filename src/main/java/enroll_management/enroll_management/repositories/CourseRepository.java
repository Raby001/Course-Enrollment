package enroll_management.enroll_management.repositories;
import enroll_management.enroll_management.Entities.Course;
import enroll_management.enroll_management.enums.CourseStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    // Custom query method to find a course by its unique course code
    Optional<Course> findByCourseCode(String courseCode);
    List<Course> findByStatus(CourseStatus status);

    // ADMIN: search all courses
    Page<Course> findByCourseNameContainingIgnoreCase(
            String keyword,
            Pageable pageable
    );

    // STUDENT: ACTIVE courses only
    Page<Course> findByStatusAndCourseNameContainingIgnoreCase(
            CourseStatus status,
            String keyword,
            Pageable pageable
    );

}
