package com.ncu.collage.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ncu.collage.dto.CreatePostRequest;
import com.ncu.collage.dto.PostDto;
import com.ncu.collage.dto.UpdatePostRequest;
import com.ncu.collage.irepository.IPostRepository;
import com.ncu.collage.irepository.IUserRepository;
import com.ncu.collage.model.Post;
import java.util.UUID;

@Service
public class PostService {
    private final IPostRepository postRepository;
    private final IUserRepository userRepository;
    private final ModelMapper mapper;

    @Autowired
    public PostService(IPostRepository postRepository, IUserRepository userRepository, ModelMapper mapper){
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    public List<PostDto> listPosts(){
        return postRepository.findAll().stream().map(p -> mapper.map(p, PostDto.class)).collect(Collectors.toList());
    }

    public List<PostDto> listPostsByUser(String userId){
        return postRepository.findByUserId(userId).stream().map(p -> mapper.map(p, PostDto.class)).collect(Collectors.toList());
    }

    public Optional<PostDto> getPost(String id){
        return postRepository.findById(id).map(p -> mapper.map(p, PostDto.class));
    }

    public PostDto createPost(CreatePostRequest req){
        // Validate user exists
        userRepository.findById(req.getUserId()).orElseThrow(() -> new IllegalArgumentException("Invalid userId"));

        // Always generate a fresh postId (avoid accidental reuse of userId or client supplied id)
        Post post = new Post();
        post.setPostId(UUID.randomUUID().toString());
        post.setUserId(req.getUserId());
        post.setTitle(req.getTitle());
        post.setContent(req.getContent());

        postRepository.insert(post);
        return mapper.map(post, PostDto.class);
    }

    public Optional<PostDto> updatePost(String id, UpdatePostRequest req){
        Optional<Post> existingOpt = postRepository.findById(id);
        if(existingOpt.isEmpty()) return Optional.empty();
        Post existing = existingOpt.get();
        if(req.getTitle()!=null) existing.setTitle(req.getTitle());
        if(req.getContent()!=null) existing.setContent(req.getContent());
        postRepository.update(existing);
        return Optional.of(mapper.map(existing, PostDto.class));
    }

    public boolean deletePost(String id){
        return postRepository.delete(id) > 0;
    }
}
