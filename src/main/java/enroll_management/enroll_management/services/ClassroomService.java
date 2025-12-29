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

    private final ClassroomRepository repository;

    public List<Classroom> findAll() {
        return repository.findAll();
    }

    public Classroom findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Classroom not found"));
    }

    public void create(ClassroomDTO dto) {
        Classroom c = new Classroom();
        map(dto, c);
        repository.save(c);
    }

    public void update(Long id, ClassroomDTO dto) {
        Classroom c = findById(id);
        map(dto, c);
        repository.save(c);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    private void map(ClassroomDTO dto, Classroom c) {
        c.setBuilding(dto.getBuilding());
        c.setRoomNumber(dto.getRoomNumber());
        c.setCapacity(dto.getCapacity());
        c.setHasProjector(dto.getHasProjector());
        c.setHasComputer(dto.getHasComputer());
        c.setStatus(dto.getStatus() == null ? "AVAILABLE" : dto.getStatus());
    }
}
