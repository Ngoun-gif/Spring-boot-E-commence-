package Ecommerce.Application.project.users.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UserReq {
    private String email;
    private String username;
    private String password;
    private String firstname;
    private String lastname;
    private String phone;
    private boolean active = true;

    // roles by name, e.g. ["ADMIN", "CUSTOMER"]
    private Set<String> roles;
}
