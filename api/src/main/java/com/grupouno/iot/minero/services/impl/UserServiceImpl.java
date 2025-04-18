package com.grupouno.iot.minero.services.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.grupouno.iot.minero.dto.UserCreateRequest;
import com.grupouno.iot.minero.models.User;
import com.grupouno.iot.minero.repository.UserRepository;
import com.grupouno.iot.minero.services.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User createUser(UserCreateRequest userRequest) {
        User newUser = new User();
        newUser.setUsername(userRequest.getUsername());
        newUser.setPasswordHash(passwordEncoder.encode(userRequest.getPassword()));
        newUser.setEnabled(userRequest.isEnabled());
        newUser.setBlocked(userRequest.isBlocked());
        newUser.setCredentialsExpired(userRequest.isCredentialsExpired());
        newUser.setCreatedAt(LocalDateTime.now());
        newUser.setUpdatedAt(LocalDateTime.now());
        
        return userRepository.save(newUser);
    }

    @Override
    public User updateUser(Long id, User updatedUser) {
        return userRepository.findById(id).map(user -> {
            user.setUsername(updatedUser.getUsername());
            user.setPasswordHash(updatedUser.getPasswordHash());
            user.setEnabled(updatedUser.isEnabled());
            user.setBlocked(updatedUser.isBlocked());
            user.setCredentialsExpired(updatedUser.isCredentialsExpired());
            user.setUpdatedAt(LocalDateTime.now());
            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}