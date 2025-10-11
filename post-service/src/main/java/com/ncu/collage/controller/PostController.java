package com.ncu.collage.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.ncu.collage.dto.CreatePostRequest;
import com.ncu.collage.dto.PostDto;
import com.ncu.collage.dto.UpdatePostRequest;
import com.ncu.collage.service.PostService;

@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    @Autowired
    public PostController(PostService postService){
        this.postService = postService;
    }

    @GetMapping
    public List<PostDto> listPosts(){
        return postService.listPosts();
    }

    @GetMapping("/user/{userId}")
    public List<PostDto> listPostsByUser(@PathVariable String userId){
        return postService.listPostsByUser(userId);
    }

    @GetMapping("/user/{userId}/count")
    public ResponseEntity<?> countPostsByUser(@PathVariable String userId){
        int count = postService.countPostsByUser(userId);
        return ResponseEntity.ok(java.util.Map.of("userId", userId, "count", count));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPost(@PathVariable String id){
        return postService.getPost(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createPost(@Validated @RequestBody CreatePostRequest req){
        try {
            PostDto created = postService.createPost(req);
            return ResponseEntity.created(URI.create("/posts/"+created.getPostId())).body(created);
        } catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePost(@PathVariable String id, @Validated @RequestBody UpdatePostRequest req){
        return postService.updatePost(id, req)
            .<ResponseEntity<?>>map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable String id){
        return postService.deletePost(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
