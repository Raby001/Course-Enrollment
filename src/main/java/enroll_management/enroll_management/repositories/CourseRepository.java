package enroll_management.enroll_management.repositories;
import enroll_management.enroll_management.Entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    // Custom query method to find a course by its unique course code
    Optional<Course> findByCourseCode(String courseCode);
}
