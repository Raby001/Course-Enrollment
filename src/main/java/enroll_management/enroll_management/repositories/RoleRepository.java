package enroll_management.enroll_management.repositories;

import enroll_management.enroll_management.Entities.Role;
import enroll_management.enroll_management.enums.RoleName;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    @EntityGraph(attributePaths = "name")
    Optional<Role> findUserByName(RoleName roleName);
}