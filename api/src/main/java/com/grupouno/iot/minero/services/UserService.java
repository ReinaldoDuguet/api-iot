package com.grupouno.iot.minero.services;

import com.grupouno.iot.minero.dto.UserCreateRequest;
import com.grupouno.iot.minero.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAllUsers();
    Optional<User> getUserById(Long id);
    User createUser(UserCreateRequest userRequest);
    User updateUser(Long id, User updatedUser);
    void deleteUser(Long id);
}