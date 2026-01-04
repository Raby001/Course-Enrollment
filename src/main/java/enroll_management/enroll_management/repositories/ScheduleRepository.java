package enroll_management.enroll_management.repositories;

import enroll_management.enroll_management.Entities.Classroom;
import enroll_management.enroll_management.Entities.Schedule;

import java.util.List;
import enroll_management.enroll_management.enums.DayOfWeek;

import java.time.LocalTime;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByCourseIdIn(List<Long> courseIds);
    
    
    boolean existsByClassroomAndDayOfWeekAndStartTimeLessThanAndEndTimeGreaterThan(
            Classroom classroom,
            DayOfWeek dayOfWeek,
            LocalTime endTime,
            LocalTime startTime
    );

}
