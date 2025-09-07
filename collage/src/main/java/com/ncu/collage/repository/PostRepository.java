package com.ncu.collage.repository;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ncu.collage.irepository.IPostRepository;
import com.ncu.collage.model.Post;

@Repository
public class PostRepository implements IPostRepository {
    private final JdbcTemplate jdbc;

    @Autowired
    public PostRepository(JdbcTemplate jdbc){
        this.jdbc = jdbc;
    }

    @Override
    public List<Post> findAll(){
        return jdbc.query("SELECT post_id, user_id, title, content, created_at, updated_at FROM posts", new PostRowMapper());
    }

    @Override
    public List<Post> findByUserId(String userId){
        return jdbc.query("SELECT post_id, user_id, title, content, created_at, updated_at FROM posts WHERE user_id=?", new PostRowMapper(), userId);
    }

    @Override
    public Optional<Post> findById(String id){
        var list = jdbc.query("SELECT post_id, user_id, title, content, created_at, updated_at FROM posts WHERE post_id=?", new PostRowMapper(), id);
        return list.stream().findFirst();
    }

    @Override
    public int insert(Post post){
        // Regenerate postId if null or suspiciously identical to userId
        if(post.getPostId() == null || post.getPostId().equals(post.getUserId())) {
            post.setPostId(UUID.randomUUID().toString());
        }
        // Defensive: loop (very rarely needed) until unique (max a few tries)
        int attempts = 0;
        while(attempts < 3) {
            // Check if id already exists
            var existing = findById(post.getPostId());
            if(existing.isEmpty()) break;
            post.setPostId(UUID.randomUUID().toString());
            attempts++;
        }
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        if(post.getCreatedAt()==null) post.setCreatedAt(now);
        if(post.getUpdatedAt()==null) post.setUpdatedAt(now);
        return jdbc.update("INSERT INTO posts (post_id, user_id, title, content, created_at, updated_at) VALUES (?,?,?,?,?,?)",
            post.getPostId(), post.getUserId(), post.getTitle(), post.getContent(),
            Timestamp.from(post.getCreatedAt().toInstant()), Timestamp.from(post.getUpdatedAt().toInstant()));
    }

    @Override
    public int update(Post post){
        post.setUpdatedAt(OffsetDateTime.now(ZoneOffset.UTC));
        return jdbc.update("UPDATE posts SET title=?, content=?, updated_at=? WHERE post_id=?",
            post.getTitle(), post.getContent(), Timestamp.from(post.getUpdatedAt().toInstant()), post.getPostId());
    }

    @Override
    public int delete(String id){
        return jdbc.update("DELETE FROM posts WHERE post_id=?", id);
    }
}
