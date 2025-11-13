package Ecommerce.Application.project.users;

import Ecommerce.Application.project.roles.RoleRepository;
import Ecommerce.Application.project.roles.entity.Role;
import Ecommerce.Application.project.users.dto.*;
import Ecommerce.Application.project.users.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public List<UserRes> getAll() {
        return userRepository.findAll().stream()
                .map(UserMapper::toRes)
                .toList();
    }

    public UserRes getById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return UserMapper.toRes(user);
    }

    public UserRes create(UserReq req) {
        if (userRepository.findByEmail(req.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        Set<Role> roles = new HashSet<>();
        for (String roleName : req.getRoles()) {
            Role role = roleRepository.findByName(roleName)
                    .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
            roles.add(role);
        }

        User user = User.builder()
                .email(req.getEmail())
                .username(req.getUsername())
                .password(passwordEncoder.encode(req.getPassword()))
                .firstname(req.getFirstname())
                .lastname(req.getLastname())
                .phone(req.getPhone())
                .active(req.isActive())
                .roles(roles)
                .build();

        userRepository.save(user);
        return UserMapper.toRes(user);
    }

    public UserRes update(Long id, UserReq req) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setFirstname(req.getFirstname());
        user.setLastname(req.getLastname());
        user.setPhone(req.getPhone());
        user.setActive(req.isActive());

        if (req.getPassword() != null && !req.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(req.getPassword()));
        }

        if (req.getRoles() != null && !req.getRoles().isEmpty()) {
            Set<Role> roles = req.getRoles().stream()
                    .map(rn -> roleRepository.findByName(rn)
                            .orElseThrow(() -> new RuntimeException("Role not found: " + rn)))
                    .collect(Collectors.toSet());
            user.setRoles(roles);
        }

        userRepository.save(user);
        return UserMapper.toRes(user);
    }

    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id);
    }
}
