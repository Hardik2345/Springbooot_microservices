package com.ncu.collage.irepository;

import java.util.List;
import java.util.Optional;
import com.ncu.collage.model.Post;

public interface IPostRepository {
    List<Post> findAll();
    List<Post> findByUserId(String userId);
    Optional<Post> findById(String id);
    int insert(Post post);
    int update(Post post);
    int delete(String id);
}
