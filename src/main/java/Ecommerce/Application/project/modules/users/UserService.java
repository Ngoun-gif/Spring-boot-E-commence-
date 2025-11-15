package Ecommerce.Application.project.modules.users;

import Ecommerce.Application.project.modules.users.dto.UserRes;
import Ecommerce.Application.project.modules.users.entity.User;
import Ecommerce.Application.project.modules.roles.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repo;

    public List<UserRes> getAll() {
        return repo.findAll().stream()
                .map(this::toRes)
                .toList();
    }

    public UserRes getById(Long id) {
        User user = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return toRes(user);
    }

    public UserRes getMe(String email) {
        User user = repo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return toRes(user);
    }

    private UserRes toRes(User u) {
        UserRes res = new UserRes();
        res.setId(u.getId());
        res.setEmail(u.getEmail());
        res.setUsername(u.getUsername());
        res.setFirstname(u.getFirstname());
        res.setLastname(u.getLastname());
        res.setPhone(u.getPhone());
        res.setActive(u.isActive());
        res.setCreatedAt(u.getCreatedAt());

        res.setRoles(
                u.getRoles().stream()
                        .map(Role::getName)
                        .collect(Collectors.toSet())
        );

        return res;
    }
}
