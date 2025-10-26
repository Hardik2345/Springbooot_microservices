package com.cu.collage.authservice.repository;

import com.cu.collage.authservice.model.AuthUser;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
public class AuthUserRepository {
    private final JdbcTemplate jdbcTemplate;

    public AuthUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final RowMapper<AuthUser> ROW_MAPPER = new RowMapper<AuthUser>() {
        @Override
        public AuthUser mapRow(ResultSet rs, int rowNum) throws SQLException {
            AuthUser u = new AuthUser();
            u.setId(rs.getString("id"));
            u.setUserId(rs.getString("user_id"));
            u.setUsername(rs.getString("username"));
            u.setPasswordHash(rs.getString("password_hash"));
            u.setEnabled(rs.getBoolean("enabled"));
            u.setRoles(rs.getString("roles"));
            return u;
        }
    };

    public Optional<AuthUser> findByUsername(String username){
        try {
            AuthUser u = jdbcTemplate.queryForObject("select id,user_id,username,password_hash,enabled,roles from auth_user where username=?", ROW_MAPPER, username);
            return Optional.ofNullable(u);
        } catch (EmptyResultDataAccessException e){
            return Optional.empty();
        }
    }

    public int insert(AuthUser user){
        return jdbcTemplate.update("insert into auth_user(id,user_id,username,password_hash,enabled,roles) values (?,?,?,?,?,?)",
                user.getId(), user.getUserId(), user.getUsername(), user.getPasswordHash(), user.isEnabled(), user.getRoles());
    }

    public boolean existsByUsername(String username){
        Integer cnt = jdbcTemplate.queryForObject("select count(1) from auth_user where username=?", Integer.class, username);
        return cnt != null && cnt > 0;
    }
}
