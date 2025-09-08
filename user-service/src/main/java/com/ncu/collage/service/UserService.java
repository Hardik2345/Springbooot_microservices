package com.ncu.collage.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ncu.collage.dto.CreateUserRequest;
import com.ncu.collage.dto.UpdateUserRequest;
import com.ncu.collage.dto.UserDto;
import com.ncu.collage.irepository.IUserRepository;
import com.ncu.collage.model.User;

@Service
public class UserService {
    private final IUserRepository userRepository;
    private final ModelMapper mapper;

    @Autowired
    public UserService(IUserRepository userRepository, ModelMapper mapper){
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    public List<UserDto> getAllUsers(){
        return userRepository.findAll().stream()
            .map(u -> mapper.map(u, UserDto.class))
            .collect(Collectors.toList());
    }

    public Optional<UserDto> getUser(String id){
        return userRepository.findById(id).map(u -> mapper.map(u, UserDto.class));
    }

    public UserDto createUser(CreateUserRequest req){
        userRepository.findByUsername(req.getUsername()).ifPresent(u -> { throw new IllegalStateException("Username already exists"); });
        userRepository.findByEmail(req.getEmail()).ifPresent(u -> { throw new IllegalStateException("Email already exists"); });
        User user = mapper.map(req, User.class);
        userRepository.insert(user);
        return mapper.map(user, UserDto.class);
    }

    public Optional<UserDto> updateUser(String id, UpdateUserRequest req){
        Optional<User> existingOpt = userRepository.findById(id);
        if(existingOpt.isEmpty()) return Optional.empty();
        User existing = existingOpt.get();
        if(req.getUsername()!=null && !req.getUsername().equals(existing.getUsername())){
            userRepository.findByUsername(req.getUsername()).ifPresent(u -> { throw new IllegalStateException("Username already exists"); });
            existing.setUsername(req.getUsername());
        }
        if(req.getEmail()!=null && !req.getEmail().equals(existing.getEmail())){
            userRepository.findByEmail(req.getEmail()).ifPresent(u -> { throw new IllegalStateException("Email already exists"); });
            existing.setEmail(req.getEmail());
        }
        if(req.getDisplayName()!=null){
            existing.setDisplayName(req.getDisplayName());
        }
        userRepository.update(existing);
        return Optional.of(mapper.map(existing, UserDto.class));
    }

    public boolean deleteUser(String id){
        return userRepository.delete(id) > 0;
    }
}
