package com.grupouno.iot.minero.services.impl;

import com.grupouno.iot.minero.simulator.dto.UserDTO;
import com.grupouno.iot.minero.simulator.dto.UserUpdateDTO;
import com.grupouno.iot.minero.models.Role;
import com.grupouno.iot.minero.models.User;
import com.grupouno.iot.minero.models.UserRole;
import com.grupouno.iot.minero.repository.RoleRepository;
import com.grupouno.iot.minero.repository.UserRepository;
import com.grupouno.iot.minero.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;


    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public User createUser(UserDTO dto) {
        // Hashear el password
        String passwordHash = new BCryptPasswordEncoder().encode(dto.getPassword());

        // Crear instancia del usuario sin roles aún
        User user = new User(dto.getUsername(), passwordHash, null);

        // Buscar el rol en base de datos
        Role role = roleRepository.findByName(dto.getRole())
                .orElseThrow(() -> new RuntimeException("Role no encontrado: " + dto.getRole()));

        // Crear la relación UserRole
        UserRole userRole = new UserRole(user, role);

        // Asociar el rol al usuario
        user.setUserRoles(Collections.singletonList(userRole));

        // Guardar usuario (con CascadeType.ALL guardará también UserRole)
        return userRepository.save(user);
    }

    @Override
    public User updateUser(Long id, UserUpdateDTO updateDTO) {
        return userRepository.findById(id).map(user -> {
            // Actualiza el usuario con los campos del DTO
            if (updateDTO.getPassword() != null && !updateDTO.getPassword().isEmpty()) {
                String hashed = new BCryptPasswordEncoder().encode(updateDTO.getPassword());
                user.setPasswordHash(hashed);
            }

            user.setUsername(updateDTO.getUsername());
            user.setEnabled(updateDTO.isEnabled());
            user.setBlocked(updateDTO.isBlocked());
            user.setCredentialsExpired(updateDTO.isCredentialsExpired());
            user.setUpdatedAt(LocalDateTime.now());

            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
