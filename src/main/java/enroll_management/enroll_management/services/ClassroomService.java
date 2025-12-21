package enroll_management.enroll_management.services;

import enroll_management.enroll_management.Entities.Classroom;
import enroll_management.enroll_management.dto.ClassroomDTO;
import enroll_management.enroll_management.repositories.ClassroomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClassroomService {

    private final ClassroomRepository classroomRepository;

    public List<Classroom> getAll() {
        return classroomRepository.findAll();
    }

    public Classroom getById(Long id) {
        return classroomRepository.findById(id).orElseThrow(() -> new RuntimeException("Classroom not found"));
    }

    public Classroom create(ClassroomDTO dto) {
        Classroom classroom = new Classroom();
        mapToEntity(dto, classroom);
        return classroomRepository.save(classroom);
    }

    public Classroom update(Long id, ClassroomDTO dto) {
        Classroom classroom = getById(id);
        mapToEntity(dto, classroom);
        return classroomRepository.save(classroom);
    }

    public void delete(Long id) {
        classroomRepository.deleteById(id);
    }

    private void mapToEntity(ClassroomDTO dto, Classroom classroom) {
        classroom.setBuilding(dto.getBuilding());
        classroom.setRoomNumber(dto.getRoomNumber());
        classroom.setCapacity(dto.getCapacity());
        classroom.setHasProjector(dto.getHasProjector());
        classroom.setHasComputer(dto.getHasComputer());
        classroom.setStatus(dto.getStatus() != null ? dto.getStatus() : "AVAILABLE");
    }
}
