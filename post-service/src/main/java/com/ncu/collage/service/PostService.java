package com.ncu.collage.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import com.ncu.collage.dto.CreatePostRequest;
import com.ncu.collage.dto.PostDto;
import com.ncu.collage.dto.UpdatePostRequest;
import com.ncu.collage.irepository.IPostRepository;
import com.ncu.collage.model.Post;

@Service
public class PostService {
    private final IPostRepository postRepository;
    private final ModelMapper mapper;
    private final RestClient restClient;

    // User-service base URL; injected from configuration
    @Value("${user.service.base-url:http://localhost:9001}")
    private String userServiceBaseUrl;

    @Autowired
    public PostService(IPostRepository postRepository, ModelMapper mapper) {
        this.postRepository = postRepository;
        this.mapper = mapper;
        this.restClient = RestClient.create();
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
        // Validate user exists via user-service
        ensureUserExists(req.getUserId());

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

    public int countPostsByUser(String userId){
        return postRepository.countByUserId(userId);
    }

    private void ensureUserExists(String userId){
        try {
            var response = restClient.get().uri(userServiceBaseUrl + "/users/" + userId).retrieve().toEntity(String.class);
            if(response.getStatusCode() == HttpStatus.NOT_FOUND){
                throw new IllegalArgumentException("Invalid userId");
            }
        } catch(RestClientException ex){
            throw new IllegalArgumentException("Invalid userId");
        }
    }
}
