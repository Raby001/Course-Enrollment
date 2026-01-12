package enroll_management.enroll_management.services.student;

import enroll_management.enroll_management.Entities.Schedule;
import enroll_management.enroll_management.Entities.User;
import enroll_management.enroll_management.dto.admin.TimeTableRowDTO;
import enroll_management.enroll_management.repositories.EnrollmentRepository;
import enroll_management.enroll_management.repositories.ScheduleRepository;
import enroll_management.enroll_management.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StudentScheduleService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private UserRepository userRepository;

    // NEW METHOD: Get only active schedules (excludes cancelled)
    public List<Schedule> getActiveStudentSchedule() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User student = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        // Get all course IDs the student is enrolled in
        List<Long> courseIds = enrollmentRepository.findByStudentId(student.getId()).stream()
                .map(enrollment -> enrollment.getCourse().getId())
                .collect(Collectors.toList());

        // Get schedules for those courses
        List<Schedule> allSchedules = scheduleRepository.findByCourseIdIn(courseIds);
        
        // Filter out cancelled schedules
        return allSchedules.stream()
                .filter(s -> !"CANCELLED".equals(s.getStatus()))
                .collect(Collectors.toList());
    }

    // Build weekly timetable rows
    public List<TimeTableRowDTO> buildWeeklyTimetable(List<Schedule> schedules) {

        Map<String, TimeTableRowDTO> rows = new LinkedHashMap<>();

        for (Schedule s : schedules) {

            // Key = one row per time slot
            String key = s.getStartTime() + "-" + s.getEndTime();

            // Create row if it does not exist
            TimeTableRowDTO row = rows.computeIfAbsent(
                    key,
                    k -> new TimeTableRowDTO(s.getStartTime(), s.getEndTime())
            );

            // Put schedule into correct day column
            row.getSchedulesByDay().put(s.getDayOfWeek(), s);
        }

        return new ArrayList<>(rows.values());
    }

}