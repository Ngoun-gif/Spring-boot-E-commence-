package Ecommerce.Application.project.modules.users;

import Ecommerce.Application.project.modules.users.dto.UserReq;
import Ecommerce.Application.project.modules.users.dto.UserRes;
import Ecommerce.Application.project.modules.users.entity.User;
import Ecommerce.Application.project.modules.roles.entity.Role;
import Ecommerce.Application.project.modules.roles.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder passwordEncoder;

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

    public UserRes create(UserReq req) {
        if (repo.existsByEmail(req.getEmail())) {
            throw new RuntimeException("Email already used");
        }

        User u = new User();
        u.setEmail(req.getEmail());
        u.setUsername(req.getUsername());
        u.setFirstname(req.getFirstname());
        u.setLastname(req.getLastname());
        u.setPhone(req.getPhone());
        u.setPassword(passwordEncoder.encode(req.getPassword()));
        u.setActive(req.isActive());

        // ✔ GET ROLE FROM REQUEST
        String roleName = (req.getRole() != null && !req.getRole().isEmpty())
                ? req.getRole()
                : "CUSTOMER";

        Role role = roleRepo.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));

        u.getRoles().add(role);

        return toRes(repo.save(u));
    }

    public UserRes update(Long id, UserReq req) {
        User u = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        u.setEmail(req.getEmail());
        u.setUsername(req.getUsername());
        u.setFirstname(req.getFirstname());
        u.setLastname(req.getLastname());
        u.setPhone(req.getPhone());
        u.setActive(req.isActive());

        // ✔ UPDATE ROLE
        if (req.getRole() != null && !req.getRole().isEmpty()) {
            Role role = roleRepo.findByName(req.getRole())
                    .orElseThrow(() -> new RuntimeException("Role not found: " + req.getRole()));

            u.getRoles().clear();
            u.getRoles().add(role);
        }

        return toRes(repo.save(u));
    }

    public void delete(Long id) {
        repo.deleteById(id);
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
