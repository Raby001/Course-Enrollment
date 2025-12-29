package enroll_management.enroll_management.repositories;

import enroll_management.enroll_management.Entities.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassroomRepository extends JpaRepository<Classroom, Long> {
    
}
