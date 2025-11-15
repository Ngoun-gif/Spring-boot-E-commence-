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

    // -----------------------------
    // POST /api/auth/register
    // -----------------------------
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        return ResponseHandler.success("Registration successful", service.register(request));
    }

    // -----------------------------
    // POST /api/auth/login
    // -----------------------------
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        return ResponseHandler.success("Login successful", service.login(request));
    }
}
