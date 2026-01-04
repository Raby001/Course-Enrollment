package enroll_management.enroll_management.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalTime;
import enroll_management.enroll_management.enums.DayOfWeek;

@Data
public class ScheduleDTO {

    private Long id;

    @NotNull
    private Long courseId;

    @NotNull
    private Long classroomId;

    @NotNull
    private Integer academicYear;

    @NotNull
    private Integer semester;

    @NotNull
    private DayOfWeek dayOfWeek;

    @NotNull
    private LocalTime startTime;

    @NotNull
    private LocalTime endTime;

    private String status;
}
