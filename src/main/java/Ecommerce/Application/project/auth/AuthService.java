package Ecommerce.Application.project.auth;

import Ecommerce.Application.project.auth.dto.*;
import Ecommerce.Application.project.security.jwt.JwtUtils;
import Ecommerce.Application.project.users.UserRepository;
import Ecommerce.Application.project.users.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    // ✅ Register new user
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists!");
        }

        User user = User.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .phone(request.getPhone())
                .active(true)
                .build();

        userRepository.save(user);

        // ✅ build userDetails for token generation
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities("USER")
                .build();

        String token = jwtUtils.generateToken(userDetails);

        return AuthResponse.builder()
                .email(user.getEmail())
                .token(token)
                .message("User registered successfully!")
                .build();
    }

    // ✅ Login existing user
    public AuthResponse login(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities("USER")
                .build();

        String token = jwtUtils.generateToken(userDetails);

        return AuthResponse.builder()
                .email(user.getEmail())
                .token(token)
                .message("Login successful!")
                .build();
    }
}
