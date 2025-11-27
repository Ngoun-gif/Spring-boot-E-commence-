package Ecommerce.Application.project.modules.auth;

import Ecommerce.Application.project.modules.auth.dto.*;
import Ecommerce.Application.project.utils.ResponseHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        return ResponseHandler.success("Registration successful", service.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        return ResponseHandler.success("Login successful", service.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody RefreshTokenRequest request) {
        return ResponseHandler.success("New token generated", service.refresh(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseHandler.success("Logout successful", null);
    }
}

