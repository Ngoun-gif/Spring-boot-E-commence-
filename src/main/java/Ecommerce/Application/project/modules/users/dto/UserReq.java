package Ecommerce.Application.project.modules.users.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserReq {
    private String email;
    private String username;
    private String firstname;
    private String lastname;
    private String phone;
    private String password;
    private boolean active;

    private String role;   // ✔ ADMIN chooses role
}
