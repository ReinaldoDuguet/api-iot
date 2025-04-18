package com.grupouno.iot.minero.controller;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.grupouno.iot.minero.dto.UserCreateRequest;
import com.grupouno.iot.minero.models.User;
import com.grupouno.iot.minero.services.UserService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
	
	private static final Logger logger = Logger.getLogger(UserController.class.getName());

    @Autowired
    private UserService userService;
    
    /**
     * 
     * @param request
     * @return
     */
    @GetMapping
    public List<User> getAllUsers(HttpServletRequest request) {
    	logger.info(" --- Starting endpoint process getAllUsers: " + request.getRequestURI().toString() + " ---");
    	List<User> users = userService.getAllUsers();
    	logger.info("Total users found: " + users.size());
        return users;
    }
    
    /**
     * 
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id, HttpServletRequest request) {
    	logger.info(" --- Starting endpoint process getUserById: " + request.getRequestURI().toString() + " ---");
    	Optional<User> user = userService.getUserById(id);
    	return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    /**
     * 
     * @param id
     * @param updatedUser
     * @param request
     * @return
     */
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, 
    		@RequestBody User updatedUser,
    		HttpServletRequest request) {
    	logger.info(" --- Starting endpoint process updateUser: " + request.getRequestURI().toString() + " ---");
        try {
            User user = userService.updateUser(id, updatedUser);
            logger.info("User modified");
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
        	logger.warning("User not found with ID: " + id);
            return ResponseEntity.notFound().build(); 
        }
    }
    
    /**
     * 
     * @param id
     * @param request
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id,HttpServletRequest request){
    	logger.info(" --- Starting endpoint process updateUser: " + request.getRequestURI().toString() + " ---");
    	try {
			userService.deleteUser(id);
			logger.info("User delete");
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			logger.warning("User not found with ID: " + id);
			return ResponseEntity.notFound().build();
		}   
    }
    
    /**
     * 
     * @param newUser
     * @param request
     * @return
     */
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserCreateRequest userRequest, HttpServletRequest request) {
        logger.info(" --- Starting endpoint process createUser: " + request.getRequestURI() + " ---");
        try {
            User createdUser = userService.createUser(userRequest);
            logger.info("User created with ID: " + createdUser.getId());
            return ResponseEntity.ok(createdUser);
        } catch (Exception e) {
            logger.warning("Error creating user: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    
}