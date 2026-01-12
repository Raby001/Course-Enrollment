package enroll_management.enroll_management.dto.admin;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.EnumMap;
import java.util.Map;
import enroll_management.enroll_management.Entities.Schedule;

public class TimeTableRowDTO {

    private LocalTime startTime;
    private LocalTime endTime;

    private Map<DayOfWeek, Schedule> schedulesByDay;

    public TimeTableRowDTO(LocalTime startTime, LocalTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.schedulesByDay = new EnumMap<>(DayOfWeek.class);
    }

    // getters & setters
    public LocalTime getStartTime() {
        return startTime;
    }
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }
    public LocalTime getEndTime() {
        return endTime;
    }
    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }
    public Map<DayOfWeek, Schedule> getSchedulesByDay() {
        return schedulesByDay;
    }
    public void setSchedulesByDay(Map<DayOfWeek, Schedule> schedulesByDay) {
        this.schedulesByDay = schedulesByDay;
    }
    
}
