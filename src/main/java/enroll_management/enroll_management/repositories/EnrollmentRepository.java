package enroll_management.enroll_management.repositories;

import enroll_management.enroll_management.Entities.Enrollment;
import enroll_management.enroll_management.enums.EnrollmentStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    
    // Find all enrollments for a specific student
    List<Enrollment> findByStudentId(Long studentId);
    
    // Find all enrollments for a specific course
    List<Enrollment> findByCourseId(Long courseId);
    
    // Check if a specific student is already enrolled in a specific course
    // This is crucial for preventing duplicate enrollments!
    Optional<Enrollment> findByStudentIdAndCourseId(Long studentId, Long courseId);

    // count enrollments by status
    long countByStatus(EnrollmentStatus status);

    // Resolved today: changed from PENDING to ENROLLED today
    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.status = 'ENROLLED' AND FUNCTION('DATE', e.updatedAt) = CURRENT_DATE")
    long countResolvedToday();

    // find last enrollment by date
    Optional<Enrollment> findTopByOrderByEnrollmentDateDesc();
}