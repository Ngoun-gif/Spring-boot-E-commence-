package Ecommerce.Application.project.modules.users;

import Ecommerce.Application.project.modules.users.dto.UserReq;
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

    @GetMapping
    public List<UserRes> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public UserRes getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping("/me")
    public UserRes me(Authentication auth) {
        return service.getMe(auth.getName());
    }

    @PostMapping
    public UserRes create(@RequestBody UserReq req) {
        return service.create(req);
    }

    @PutMapping("/{id}")
    public UserRes update(@PathVariable Long id, @RequestBody UserReq req) {
        return service.update(id, req);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
