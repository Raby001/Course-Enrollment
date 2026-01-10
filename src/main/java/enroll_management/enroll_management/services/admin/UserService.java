package enroll_management.enroll_management.services.admin;

import enroll_management.enroll_management.Entities.Role;
import enroll_management.enroll_management.Entities.User;
import enroll_management.enroll_management.dto.admin.UserCreateDto;
import enroll_management.enroll_management.dto.admin.UserListDto;
import enroll_management.enroll_management.enums.UserStatus;
import enroll_management.enroll_management.repositories.RoleRepository;
import enroll_management.enroll_management.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // =========================
    // GET ALL USERS
    // =========================
    public List<UserListDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToListDto)
                .toList();
    }

    // =========================
    // CREATE USER
    // =========================
    public void createUser(UserCreateDto dto) {

        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        Role role = roleRepository.findById(dto.getRoleId())
                .orElseThrow(() -> new RuntimeException("Role not found"));

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());

        // 🔐 HASH PASSWORD
        user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));

        user.setRole(role);
        user.setStatus(dto.getStatus());

        // REQUIRED by entity
        user.setDob(LocalDate.of(2000, 1, 1));

        userRepository.save(user);
    }

    // =========================
    // TOGGLE STATUS
    // =========================
    public void toggleUserStatus(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setStatus(
                user.getStatus() == UserStatus.ACTIVE
                        ? UserStatus.INACTIVE
                        : UserStatus.ACTIVE
        );

        userRepository.save(user);
    }

    // =========================
    // DELETE USER
    // =========================
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    // =========================
    // DTO MAPPER
    // =========================
    private UserListDto mapToListDto(User user) {
        UserListDto dto = new UserListDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole().getName().name());
        dto.setStatus(user.getStatus().name());
        dto.setUserImage(user.getUserImage());
        return dto;
    }
}
