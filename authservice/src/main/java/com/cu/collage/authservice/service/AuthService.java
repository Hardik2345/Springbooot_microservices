package com.cu.collage.authservice.service;

import com.cu.collage.authservice.model.AuthUser;
import com.cu.collage.authservice.repository.AuthUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {
    private final AuthUserRepository repo;
    private final PasswordEncoder encoder;

    public AuthService(AuthUserRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    public Optional<AuthUser> verify(String username, String rawPassword){
        return repo.findByUsername(username)
                .filter(AuthUser::isEnabled)
                .filter(u -> encoder.matches(rawPassword, u.getPasswordHash()));
    }

    public AuthUser signup(String userId, String username, String rawPassword){
        if (repo.existsByUsername(username)) {
            throw new IllegalStateException("username already exists");
        }
        AuthUser user = new AuthUser();
        user.setId(UUID.randomUUID().toString());
        user.setUserId(userId);
        user.setUsername(username);
        user.setPasswordHash(encoder.encode(rawPassword));
        user.setEnabled(true);
        user.setRoles("ROLE_USER");
        repo.insert(user);
        return user;
    }
}
