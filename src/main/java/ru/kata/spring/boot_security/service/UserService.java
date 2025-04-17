package ru.kata.spring.boot_security.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.model.User;

import java.util.List;

public interface UserService extends UserDetailsService {
    void save(User user);
    void update(User user);
    void delete(Long id);
    User findById(Long id);
    List<User> findAll();
    User findByEmail(String email);
}