package enroll_management.enroll_management.services.common;

import enroll_management.enroll_management.Entities.Classroom;
import enroll_management.enroll_management.Entities.Course;
import enroll_management.enroll_management.Entities.Schedule;
import enroll_management.enroll_management.dto.admin.ScheduleDTO;
import enroll_management.enroll_management.repositories.ClassroomRepository;
import enroll_management.enroll_management.repositories.CourseRepository;
import enroll_management.enroll_management.repositories.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final CourseRepository courseRepository;
    private final ClassroomRepository classroomRepository;

    /* ===================== READ ===================== */

    public List<Schedule> findAll() {
        return scheduleRepository.findAll();
    }

    public Schedule findById(Long id) {
        return scheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));
    }

    public ScheduleDTO getDTOById(Long id) {
        Schedule s = findById(id);

        ScheduleDTO dto = new ScheduleDTO();
        dto.setId(s.getId());
        dto.setCourseId(s.getCourse().getId());
        dto.setClassroomId(s.getClassroom().getId());
        dto.setAcademicYear(s.getAcademicYear());
        dto.setSemester(Integer.parseInt(s.getSemester()));
        dto.setDayOfWeek(s.getDayOfWeek());
        dto.setStartTime(s.getStartTime());
        dto.setEndTime(s.getEndTime());
        dto.setStatus(s.getStatus());

        return dto;
    }

    /* ===================== CREATE ===================== */

    public Schedule create(ScheduleDTO dto) {

        validateConflict(dto);

        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found"));

        Classroom classroom = classroomRepository.findById(dto.getClassroomId())
                .orElseThrow(() -> new RuntimeException("Classroom not found"));

        Schedule schedule = new Schedule();
        schedule.setCourse(course);
        schedule.setClassroom(classroom);
        schedule.setAcademicYear(dto.getAcademicYear());
        schedule.setSemester(String.valueOf(dto.getSemester()));
        schedule.setDayOfWeek(dto.getDayOfWeek());
        schedule.setStartTime(dto.getStartTime());
        schedule.setEndTime(dto.getEndTime());
        schedule.setStatus(dto.getStatus() != null ? dto.getStatus() : "SCHEDULED");

        return scheduleRepository.save(schedule);

        
    }

    /* ===================== UPDATE ===================== */

    public void update(Long id, ScheduleDTO dto) {
        
        validateConflict(dto);

        Schedule existing = findById(id);

        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found"));

        Classroom classroom = classroomRepository.findById(dto.getClassroomId())
                .orElseThrow(() -> new RuntimeException("Classroom not found"));

        existing.setCourse(course);
        existing.setClassroom(classroom);
        existing.setAcademicYear(dto.getAcademicYear());
        existing.setSemester(String.valueOf(dto.getSemester()));
        existing.setDayOfWeek(dto.getDayOfWeek());
        existing.setStartTime(dto.getStartTime());
        existing.setEndTime(dto.getEndTime());
        existing.setStatus(dto.getStatus());

        scheduleRepository.save(existing);
    }

    /* ===================== DELETE ===================== */

    public void delete(Long id) {
        scheduleRepository.deleteById(id);
    }

    /* ===================== VALIDATION ===================== */
    private void validateConflict(ScheduleDTO dto) {        

        Classroom classroom = classroomRepository.findById(dto.getClassroomId())
                .orElseThrow(() -> new RuntimeException("Classroom not found"));

        boolean conflict = scheduleRepository.existsByClassroomAndDayOfWeekAndStartTimeLessThanAndEndTimeGreaterThan(
            classroom,
            dto.getDayOfWeek(),
            dto.getEndTime(),
            dto.getStartTime()
        );
        if (conflict) {
            throw new IllegalStateException("Schedule conflict: Classroom already in use");
        }
    }

}
