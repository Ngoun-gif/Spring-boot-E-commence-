package Ecommerce.Application.project.utils;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public class ResponseHandler {

    public static ResponseEntity<?> success(String message, Object data) {
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", message,
                "data", data
        ));
    }

    public static ResponseEntity<?> error(String message) {
        return ResponseEntity.badRequest().body(
                Map.of(
                        "success", false,
                        "message", message
                )
        );
    }
}
