package Ecommerce.Application.project.users.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class UserRes {
    private Long id;
    private String email;
    private String username;
    private String firstname;
    private String lastname;
    private String phone;
    private boolean active;
    private Instant createdAt;
    private Set<String> roles;
}
