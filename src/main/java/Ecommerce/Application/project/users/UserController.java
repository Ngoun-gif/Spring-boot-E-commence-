package Ecommerce.Application.project.users;

import Ecommerce.Application.project.users.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserRes> getAll() {
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public UserRes getById(@PathVariable Long id) {
        return userService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserRes create(@RequestBody UserReq req) {
        return userService.create(req);
    }

    @PutMapping("/{id}")
    public UserRes update(@PathVariable Long id, @RequestBody UserReq req) {
        return userService.update(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }
}
