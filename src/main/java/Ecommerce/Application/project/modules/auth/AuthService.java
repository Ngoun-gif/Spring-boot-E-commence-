package Ecommerce.Application.project.modules.auth;

import Ecommerce.Application.project.modules.auth.dto.*;
import Ecommerce.Application.project.modules.roles.entity.Role;
import Ecommerce.Application.project.modules.roles.RoleRepository;
import Ecommerce.Application.project.modules.users.UserRepository;
import Ecommerce.Application.project.modules.users.dto.UserRes;
import Ecommerce.Application.project.modules.users.entity.User;
import Ecommerce.Application.project.security.jwt.JwtUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            UserRepository userRepo,
            RoleRepository roleRepo,
            JwtUtils jwtUtils,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
    }

    // ---------------------------------------------------------
    // REGISTER
    // ---------------------------------------------------------
    public UserRes register(RegisterRequest req) {

        if (userRepo.existsByEmail(req.getEmail())) {
            throw new RuntimeException("Email already exists!");
        }

        Role defaultRole = roleRepo.findByName("CUSTOMER")
                .orElseThrow(() -> new RuntimeException("CUSTOMER role missing"));

        User user = new User();
        user.setEmail(req.getEmail());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setUsername(req.getUsername());
        user.setFirstname(req.getFirstname());
        user.setLastname(req.getLastname());
        user.setPhone(req.getPhone());
        user.setActive(true);
        user.setCreatedAt(Instant.now());
        user.setRoles(Set.of(defaultRole));

        userRepo.save(user);

        return toUserRes(user);
    }

    // ---------------------------------------------------------
    // LOGIN
    // ---------------------------------------------------------
    public AuthResponse login(AuthRequest req) {

        User user = userRepo.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        String access = jwtUtils.generateAccessToken(user.getEmail());
        String refresh = jwtUtils.generateRefreshToken(user.getEmail());

        return new AuthResponse(access, refresh, toUserRes(user));
    }

    public AuthResponse refresh(RefreshTokenRequest req) {

        if (!jwtUtils.validate(req.getRefreshToken())) {
            throw new RuntimeException("Invalid refresh token!");
        }

        String email = jwtUtils.extractUsername(req.getRefreshToken());

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Generate new pair
        String newAccess = jwtUtils.generateAccessToken(email);
        String newRefresh = jwtUtils.generateRefreshToken(email);

        return new AuthResponse(newAccess, newRefresh, toUserRes(user));
    }



    // ---------------------------------------------------------
    // Mapper to UserRes
    // ---------------------------------------------------------
    private UserRes toUserRes(User u) {
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
