package Ecommerce.Application.project.users.entity;

import Ecommerce.Application.project.roles.entity.Role;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users", schema = "ecommerce")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 120)
    private String email;

    @Column(nullable = false, unique = true, length = 80)
    private String username;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false, length = 80)
    private String firstname;

    @Column(nullable = false, length = 80)
    private String lastname;

    @Column(length = 50)
    private String phone;

    @Column(nullable = false)
    private boolean active = true;

    @Column(name = "created_at")
    private Instant createdAt = Instant.now();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "role_user",
            schema = "ecommerce",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();
}
