package enroll_management.enroll_management.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import enroll_management.enroll_management.Entities.Classroom;

public interface ClassroomRepository extends JpaRepository<Classroom, Long>{
     Optional<Classroom> findByBuildingAndRoomNumber(String building, String roomNumber);
}
