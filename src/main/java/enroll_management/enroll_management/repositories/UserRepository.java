package enroll_management.enroll_management.repositories;

import enroll_management.enroll_management.Entities.User;
import enroll_management.enroll_management.enums.RoleName;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByEmail(String email); 
    @EntityGraph(attributePaths = "role")
    Optional<User> findByUsername(String username);
    List<User> findByRole_Name(RoleName roleName);

}
