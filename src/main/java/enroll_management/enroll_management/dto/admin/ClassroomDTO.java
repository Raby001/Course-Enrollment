package enroll_management.enroll_management.dto.admin;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ClassroomDTO {

    private Long id;

    @NotBlank
    private String building;

    @NotBlank
    private String roomNumber;

    @NotNull
    @Min(1)
    private Integer capacity;

    private Boolean hasProjector;
    private Boolean hasComputer;
    private String status;

    // getters & setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getBuilding() { return building; }
    public void setBuilding(String building) { this.building = building; }

    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }

    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }

    public Boolean getHasProjector() { return hasProjector; }
    public void setHasProjector(Boolean hasProjector) { this.hasProjector = hasProjector; }

    public Boolean getHasComputer() { return hasComputer; }
    public void setHasComputer(Boolean hasComputer) { this.hasComputer = hasComputer; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}