package com.service.auth.services;


import com.service.auth.entity.User;
import com.service.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public User registerUser(String username, String password, String role) {
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username is already taken!");
        }

        User user = new User(username, passwordEncoder.encode(password), role);
        return userRepository.save(user);
    }
}