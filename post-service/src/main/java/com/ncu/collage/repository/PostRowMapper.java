package com.ncu.collage.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneOffset;

import org.springframework.jdbc.core.RowMapper;

import com.ncu.collage.model.Post;

public class PostRowMapper implements RowMapper<Post> {
    @Override
    public Post mapRow(ResultSet rs, int rowNum) throws SQLException {
        Post p = new Post();
        p.setPostId(rs.getString("post_id"));
        p.setUserId(rs.getString("user_id"));
        p.setTitle(rs.getString("title"));
        p.setContent(rs.getString("content"));
        var created = rs.getTimestamp("created_at");
        var updated = rs.getTimestamp("updated_at");
        if(created != null) p.setCreatedAt(created.toInstant().atOffset(ZoneOffset.UTC));
        if(updated != null) p.setUpdatedAt(updated.toInstant().atOffset(ZoneOffset.UTC));
        return p;
    }
}
