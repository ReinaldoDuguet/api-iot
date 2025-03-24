package com.grupouno.iot.minero.controller;

import com.grupouno.iot.minero.models.User;
import com.grupouno.iot.minero.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); 
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build(); 
    }

    // Prueba para GET /api/v1/users
    @Test
    void testGetAllUsers() throws Exception {
        // Arrange
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("user1");

        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("user2");

        List<User> users = Arrays.asList(user1, user2);
        when(userService.getAllUsers()).thenReturn(users);

        // Act & Assert
        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("user1"))
                .andExpect(jsonPath("$[1].username").value("user2"));
        verify(userService, times(1)).getAllUsers();
    }

    // Prueba para GET /api/v1/users/{id} (usuario encontrado)
    @Test
    void testGetUserById_UserFound() throws Exception {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setUsername("user1");

        when(userService.getUserById(1L)).thenReturn(Optional.of(user));

        // Act & Assert
        mockMvc.perform(get("/api/v1/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("user1"));
        verify(userService, times(1)).getUserById(1L);
    } 

    // Prueba para GET /api/v1/users/{id} (usuario no encontrado)
    @Test
    void testGetUserById_UserNotFound() throws Exception {
        // Arrange
        when(userService.getUserById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/v1/users/1"))
                .andExpect(status().isNotFound());
        verify(userService, times(1)).getUserById(1L);
    }
    
    
    // Prueba para POST /api/v1/users
    @Test
    void testCreateUser() throws Exception {
        // Arrange
        User newUser = new User();
        newUser.setUsername("new_user");
        newUser.setPasswordHash("password");

        when(userService.createUser(any(User.class))).thenReturn(newUser);

        // Act & Assert
        mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"new_user\", \"passwordHash\": \"password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("new_user"));
        verify(userService, times(1)).createUser(any(User.class));
    }
    /*
    // Prueba para PUT /api/v1/users/{id} (usuario encontrado)
    @Test
    void testUpdateUser_UserFound() throws Exception {
        // Arrange
        User updatedUser = new User();
        updatedUser.setUsername("updated_user");
        updatedUser.setPasswordHash("new_password");

        when(userService.updateUser(eq(1L), any(User.class))).thenReturn(updatedUser);

        // Act & Assert
        mockMvc.perform(put("/api/v1/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"updated_user\", \"passwordHash\": \"new_password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("updated_user"));
        verify(userService, times(1)).updateUser(eq(1L), any(User.class));
    }

    // Prueba para PUT /api/v1/users/{id} (usuario no encontrado)
    @Test
    void testUpdateUser_UserNotFound() throws Exception {
        // Arrange
        when(userService.updateUser(eq(1L), any(User.class)))
                .thenThrow(new RuntimeException("Usuario no encontrado con ID: 1"));

        // Act & Assert
        mockMvc.perform(put("/api/v1/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"updated_user\", \"passwordHash\": \"new_password\"}"))
                .andExpect(status().isNotFound());
        verify(userService, times(1)).updateUser(eq(1L), any(User.class));
    }

    // Prueba para DELETE /api/v1/users/{id}
    @Test
    void testDeleteUser() throws Exception {
        // Arrange
        doNothing().when(userService).deleteUser(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/v1/users/1"))
                .andExpect(status().isNoContent());
        verify(userService, times(1)).deleteUser(1L);
    }*/
}