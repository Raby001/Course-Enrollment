package enroll_management.enroll_management.repositories;

import enroll_management.enroll_management.Entities.Classroom;
import enroll_management.enroll_management.Entities.Schedule;
import enroll_management.enroll_management.enums.DayOfWeek;
import enroll_management.enroll_management.enums.ScheduleStatus;

import java.util.List;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByCourseIdIn(List<Long> courseIds);

    // Find schedules excluding cancelled ones
    List<Schedule> findByStatusNot(String status);  
    
     // For CREATE - check any conflicts
    boolean existsByClassroomAndDayOfWeekAndStartTimeLessThanAndEndTimeGreaterThan(
        Classroom classroom,
        DayOfWeek dayOfWeek,
        LocalTime endTime,
        LocalTime startTime
    );

    // For UPDATE - check conflicts excluding the current schedule
    boolean existsByClassroomAndDayOfWeekAndIdNotAndStartTimeLessThanAndEndTimeGreaterThan(
        Classroom classroom,
        DayOfWeek dayOfWeek,
        Long id,
        LocalTime endTime,
        LocalTime startTime
    );

    boolean existsByClassroomAndDayOfWeekAndIdNotAndStartTimeLessThanAndEndTimeGreaterThan(Classroom classroom,
            java.time.DayOfWeek dayOfWeek, Long scheduleId, LocalTime endTime, LocalTime startTime);

    boolean existsByClassroomAndDayOfWeekAndStartTimeLessThanAndEndTimeGreaterThan(Classroom classroom,
            java.time.DayOfWeek dayOfWeek, LocalTime endTime, LocalTime startTime);
        
   
    // Count schedules by status
    long countByStatus(String status);

   
    // Count schedules created after a specific date
    long countByCreatedAtAfter(LocalDateTime date);
}
    