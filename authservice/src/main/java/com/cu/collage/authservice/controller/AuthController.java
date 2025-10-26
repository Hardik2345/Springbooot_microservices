package com.cu.collage.authservice.controller;

import com.cu.collage.authservice.model.AuthUser;
import com.cu.collage.authservice.service.AuthService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verify(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorization){
        if (authorization == null || !authorization.startsWith("Basic ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String base64 = authorization.substring("Basic ".length());
        String decoded = new String(Base64.getDecoder().decode(base64), StandardCharsets.UTF_8);
        int idx = decoded.indexOf(':');
        if (idx < 0) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        String username = decoded.substring(0, idx);
        String password = decoded.substring(idx + 1);

        Optional<AuthUser> user = authService.verify(username, password);
        if (user.isEmpty()) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        return ResponseEntity.ok(Map.of(
                "userId", user.get().getUserId(),
                "username", user.get().getUsername(),
                "roles", user.get().getRoles()
        ));
    }

    public record SignupRequest(@NotBlank String username, @NotBlank String password, String email, String displayName) {}

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest req){
        if (!StringUtils.hasText(req.username()) || !StringUtils.hasText(req.password())) {
            return ResponseEntity.badRequest().body("username and password required");
        }
        // For simplicity, create a profile id here; in a real flow call user-service to create and get userId
        String userId = java.util.UUID.randomUUID().toString();
        try {
            AuthUser created = authService.signup(userId, req.username(), req.password());
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "userId", created.getUserId(),
                    "username", created.getUsername()
            ));
        } catch (IllegalStateException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
}
