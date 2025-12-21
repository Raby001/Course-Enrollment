package enroll_management.enroll_management.Entities;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "classrooms",
       uniqueConstraints = {
           @UniqueConstraint(columnNames = {"building", "room_number"}, 
                            name = "uk_building_room")
       },
       indexes = {
           @Index(columnList = "building", name = "idx_classroom_building"),
           @Index(columnList = "status", name = "idx_classroom_status"),
           @Index(columnList = "capacity", name = "idx_classroom_capacity")
       })
public class Classroom {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "classroom_id")
    private Long id;
    
    @Column(name = "room_number", nullable = false, length = 20)
    private String roomNumber;
    
    @Column(name = "building", nullable = false, length = 50)
    private String building;
    
    @Column(name = "capacity", nullable = false)
    private Integer capacity;
    
    @Column(name = "has_projector")
    private Boolean hasProjector = false;
    
    @Column(name = "has_computer")
    private Boolean hasComputer = false;
    
    @Column(name = "status", length = 20)
    private String status = "AVAILABLE";  // AVAILABLE, MAINTENANCE, CLOSED
    
    // ===== RELATIONSHIPS =====
    @OneToMany(mappedBy = "classroom", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Schedule> schedules = new HashSet<>();
    
    // ===== CONSTRUCTORS =====
    public Classroom() {}
    
    public Classroom(String building, String roomNumber, Integer capacity) {
        this.building = building;
        this.roomNumber = roomNumber;
        this.capacity = capacity;
    }
    
    public Classroom(String building, String roomNumber, Integer capacity, 
                     Boolean hasProjector, Boolean hasComputer) {
        this(building, roomNumber, capacity);
        this.hasProjector = hasProjector;
        this.hasComputer = hasComputer;
    }
    
    // ===== GETTERS & SETTERS =====
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }
    
    public String getBuilding() { return building; }
    public void setBuilding(String building) { this.building = building; }
    
    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }
    
    public Boolean getHasProjector() { return hasProjector; }
    public void setHasProjector(Boolean hasProjector) { this.hasProjector = hasProjector; }
    
    public Boolean getHasComputer() { return hasComputer; }
    public void setHasComputer(Boolean hasComputer) { this.hasComputer = hasComputer; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public Set<Schedule> getSchedules() { return schedules; }
    public void setSchedules(Set<Schedule> schedules) { this.schedules = schedules; }
    
    // ===== HELPER METHODS =====
    public String getFullLocation() {
        return building + " " + roomNumber;
    }
    
    public boolean isAvailable() {
        return "AVAILABLE".equals(status);
    }
    
    public boolean isUnderMaintenance() {
        return "MAINTENANCE".equals(status);
    }
    
    public boolean hasEquipment(String equipmentType) {
        if ("PROJECTOR".equalsIgnoreCase(equipmentType)) {
            return hasProjector;
        } else if ("COMPUTER".equalsIgnoreCase(equipmentType)) {
            return hasComputer;
        }
        return false;
    }
    
    public void addSchedule(Schedule schedule) {
        schedules.add(schedule);
        schedule.setClassroom(this);
    }
    
    public void removeSchedule(Schedule schedule) {
        schedules.remove(schedule);
        schedule.setClassroom(null);
    }
    
    @Override
    public String toString() {
        return "Classroom{id=" + id + ", location='" + getFullLocation() + 
               "', capacity=" + capacity + ", status='" + status + "'}";
    }
}
