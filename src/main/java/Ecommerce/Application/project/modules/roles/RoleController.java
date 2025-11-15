package Ecommerce.Application.project.modules.roles;


import Ecommerce.Application.project.modules.roles.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    public List<RoleRes> getAll() {
        return roleService.getAll();
    }

    @GetMapping("/{id}")
    public RoleRes getById(@PathVariable Long id) {
        return roleService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RoleRes create(@RequestBody RoleReq req) {
        return roleService.create(req);
    }

    @PutMapping("/{id}")
    public RoleRes update(@PathVariable Long id, @RequestBody RoleReq req) {
        return roleService.update(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        roleService.delete(id);
    }
}
