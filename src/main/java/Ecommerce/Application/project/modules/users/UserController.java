package Ecommerce.Application.project.modules.users;

import Ecommerce.Application.project.modules.users.dto.UserRes;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    // GET /users
    @GetMapping
    public List<UserRes> getAll() {
        return service.getAll();
    }

    // GET /users/{id}
    @GetMapping("/{id}")
    public UserRes getById(@PathVariable Long id) {
        return service.getById(id);
    }

    // GET /users/me
    @GetMapping("/me")
    public UserRes me(Authentication auth) {
        return service.getMe(auth.getName());
    }
}
