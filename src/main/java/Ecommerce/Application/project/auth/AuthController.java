package Ecommerce.Application.project.auth;

import Ecommerce.Application.project.auth.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // ✅ register endpoint
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    // ✅ login endpoint
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        // Optional: you can add audit logs or token blacklisting here
        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }

}

