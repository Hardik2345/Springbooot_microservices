package com.ncu.collage.irepository;

import java.util.List;
import java.util.Optional;
import com.ncu.collage.model.User;

public interface IUserRepository {
    List<User> findAll();
    Optional<User> findById(String id);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    int insert(User user);
    int update(User user);
    int delete(String id);
}
