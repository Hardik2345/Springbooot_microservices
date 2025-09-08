package com.ncu.collage.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneOffset;

import org.springframework.jdbc.core.RowMapper;

import com.ncu.collage.model.User;

public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User u = new User();
        u.setUserId(rs.getString("user_id"));
        u.setUsername(rs.getString("username"));
        u.setEmail(rs.getString("email"));
        u.setDisplayName(rs.getString("display_name"));
        var createdTs = rs.getTimestamp("created_at");
        var updatedTs = rs.getTimestamp("updated_at");
        if(createdTs != null) u.setCreatedAt(createdTs.toInstant().atOffset(ZoneOffset.UTC));
        if(updatedTs != null) u.setUpdatedAt(updatedTs.toInstant().atOffset(ZoneOffset.UTC));
        return u;
    }
}
