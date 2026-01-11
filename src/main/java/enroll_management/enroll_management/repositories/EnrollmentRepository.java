package enroll_management.enroll_management.repositories;

import enroll_management.enroll_management.Entities.Enrollment;
import enroll_management.enroll_management.enums.EnrollmentStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    
    // Find all enrollments for a specific student
    List<Enrollment> findByStudentId(Long studentId);
    
    // Find all enrollments for a specific course
    List<Enrollment> findByCourseId(Long courseId);
    
    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);

    long countByCourseIdAndStatus(Long courseId, EnrollmentStatus status);
}