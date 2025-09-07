package com.ncu.collage.repository;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ncu.collage.irepository.IUserRepository;
import com.ncu.collage.model.User;

@Repository
public class UserRepository implements IUserRepository {
    private static final Logger log = LoggerFactory.getLogger(UserRepository.class);
    private final JdbcTemplate jdbc;

    @Autowired
    public UserRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public List<User> findAll() {
        return jdbc.query("SELECT user_id, username, email, display_name, created_at, updated_at FROM users", new UserRowMapper());
    }

    @Override
    public Optional<User> findById(String id) {
        var list = jdbc.query("SELECT user_id, username, email, display_name, created_at, updated_at FROM users WHERE user_id=?", new UserRowMapper(), id);
        return list.stream().findFirst();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        var list = jdbc.query("SELECT user_id, username, email, display_name, created_at, updated_at FROM users WHERE username=?", new UserRowMapper(), username);
        return list.stream().findFirst();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        var list = jdbc.query("SELECT user_id, username, email, display_name, created_at, updated_at FROM users WHERE email=?", new UserRowMapper(), email);
        return list.stream().findFirst();
    }

    @Override
    public int insert(User user) {
        if(user.getUserId() == null) {
            user.setUserId(UUID.randomUUID().toString());
        }
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        if(user.getCreatedAt() == null) user.setCreatedAt(now);
        if(user.getUpdatedAt() == null) user.setUpdatedAt(now);
        return jdbc.update("INSERT INTO users (user_id, username, email, display_name, created_at, updated_at) VALUES (?,?,?,?,?,?)",
            user.getUserId(), user.getUsername(), user.getEmail(), user.getDisplayName(),
            Timestamp.from(user.getCreatedAt().toInstant()), Timestamp.from(user.getUpdatedAt().toInstant()));
    }

    @Override
    public int update(User user) {
        user.setUpdatedAt(OffsetDateTime.now(ZoneOffset.UTC));
        return jdbc.update("UPDATE users SET username=?, email=?, display_name=?, updated_at=? WHERE user_id=?",
            user.getUsername(), user.getEmail(), user.getDisplayName(), Timestamp.from(user.getUpdatedAt().toInstant()), user.getUserId());
    }

    @Override
    public int delete(String id) {
        return jdbc.update("DELETE FROM users WHERE user_id=?", id);
    }
}
