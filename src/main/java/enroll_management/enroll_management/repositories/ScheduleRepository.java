package enroll_management.enroll_management.repositories;

import enroll_management.enroll_management.Entities.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    
}
