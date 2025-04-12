package com.grupouno.iot.minero.controller;

import com.grupouno.iot.minero.dto.UserDTO;
import com.grupouno.iot.minero.dto.UserResponseDTO;
import com.grupouno.iot.minero.dto.UserUpdateDTO;
import com.grupouno.iot.minero.models.User;
import com.grupouno.iot.minero.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private static final Logger logger = Logger.getLogger(UserController.class.getName());

    @Autowired
    private UserService userService;

    /**
     * Get all users
     */
    @GetMapping
    public List<UserResponseDTO> getAllUsers(HttpServletRequest request) {
        logger.info(" --- Starting endpoint process getAllUsers: " + request.getRequestURI() + " ---");
        return userService.getAllUsers().stream().map(UserResponseDTO::fromEntity).collect(Collectors.toList());
    }

    /**
     * Get user by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id, HttpServletRequest request) {
        logger.info(" --- Starting endpoint process getUserById: " + request.getRequestURI() + " ---");
        Optional<User> user = userService.getUserById(id);
        return user.map(u -> ResponseEntity.ok(UserResponseDTO.fromEntity(u)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Update user
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id,
                                                      @RequestBody UserUpdateDTO updatedUser,
                                                      HttpServletRequest request) {
        logger.info(" --- Starting endpoint process updateUser: " + request.getRequestURI() + " ---");
        try {
            User user = userService.updateUser(id, updatedUser);
            logger.info("User modified");
            return ResponseEntity.ok(UserResponseDTO.fromEntity(user));
        } catch (RuntimeException e) {
            logger.warning("User not found with ID: " + id);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Delete user
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id,HttpServletRequest request){
        logger.info(" --- Starting endpoint process deleteUser: " + request.getRequestURI() + " ---");
        try {
            userService.deleteUser(id);
            logger.info("User deleted");
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.warning("User not found with ID: " + id);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Create user
     */
    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserDTO newUser, HttpServletRequest request) {
        logger.info(" --- Starting endpoint process createUser: " + request.getRequestURI() + " ---");
        try {
            User createdUser = userService.createUser(newUser);
            logger.info("User created with ID: " + createdUser.getId());
            return ResponseEntity.ok(UserResponseDTO.fromEntity(createdUser));
        } catch (Exception e) {
            logger.warning("Error creating user: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
