package com.grupouno.iot.minero.services;

import com.grupouno.iot.minero.models.User;
import com.grupouno.iot.minero.dto.UserDTO;
import com.grupouno.iot.minero.dto.UserUpdateDTO;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAllUsers();
    Optional<User> getUserById(Long id);
    User createUser(UserDTO newUser); // Usar DTO para crear usuario
    User updateUser(Long id, UserUpdateDTO updatedUser); // ← Cambiado a DTO
    void deleteUser(Long id);
}
