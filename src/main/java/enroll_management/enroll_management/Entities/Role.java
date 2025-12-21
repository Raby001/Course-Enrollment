package enroll_management.enroll_management.Entities;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import enroll_management.enroll_management.enums.RoleName;

@Entity
@Table(name = "roles", 
       uniqueConstraints = {
           @UniqueConstraint(columnNames = "role_name", name = "uk_role_name")
       })
public class Role {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long id;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "role_name", nullable = false, length = 50)
    private RoleName name;
    
    @Column(name = "description", length = 255)
    private String description;
    
    // ===== RELATIONSHIPS =====
    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<User> users = new HashSet<>();
    
    // ===== CONSTRUCTORS =====
    public Role() {}

    public Role(RoleName name, String description) {
        this.name = name;
        this.description = description;
    }
    
    // ===== GETTERS & SETTERS =====
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public RoleName getName() { return name; }
    public void setName(RoleName name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public Set<User> getUsers() { return users; }
    public void setUsers(Set<User> users) { this.users = users; }
    
    // ===== HELPER METHODS =====
    public void addUser(User user) {
        users.add(user);
        user.setRole(this);
    }
    
    public void removeUser(User user) {
        users.remove(user);
        user.setRole(null);
    }
    
    @Override
    public String toString() {
        return "Role{id=" + id + ", name='" + name + "', description='" + description + "'}";
    }
}
