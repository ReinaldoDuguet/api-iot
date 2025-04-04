package com.grupouno.iot.minero.services;

import com.grupouno.iot.minero.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAllUsers();
    Optional<User> getUserById(Long id);
    User createUser(User newUser);
    User updateUser(Long id, User updatedUser);
    void deleteUser(Long id);
}