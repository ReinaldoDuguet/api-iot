package com.grupouno.iot.minero.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grupouno.iot.minero.dto.UserCreateRequest;
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

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void testGetAllUsers() throws Exception {
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("user1");
        user1.setPasswordHash("hashed_password");
        
        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("user2");
        user2.setPasswordHash("hashed_password");
        
        List<User> users = Arrays.asList(user1, user2);
        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("user1"))
                .andExpect(jsonPath("$[1].username").value("user2"));
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void testGetUserById_UserFound() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("user1");
        user.setPasswordHash("hashed_password");
        
        when(userService.getUserById(1L)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/v1/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("user1"));
        verify(userService, times(1)).getUserById(1L);
    }

    @Test
    void testGetUserById_UserNotFound() throws Exception {
        when(userService.getUserById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/users/1"))
                .andExpect(status().isNotFound());
        verify(userService, times(1)).getUserById(1L);
    }

    @Test
    void testCreateUser() throws Exception {
        UserCreateRequest userRequest = new UserCreateRequest();
        userRequest.setUsername("new_user");
        userRequest.setPassword("password");
        userRequest.setEnabled(true);
        userRequest.setBlocked(false);
        userRequest.setCredentialsExpired(false);

        User createdUser = new User();
        createdUser.setId(1L);
        createdUser.setUsername("new_user");
        createdUser.setPasswordHash("hashed_password");

        when(userService.createUser(any(UserCreateRequest.class))).thenReturn(createdUser);

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("new_user"));

        verify(userService, times(1)).createUser(any(UserCreateRequest.class));
    }

    @Test
    void testUpdateUser_UserFound() throws Exception {
        User userUpdate = new User();
        userUpdate.setUsername("updated_user");
        userUpdate.setPasswordHash("new_hashed_password");

        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setUsername("updated_user");
        updatedUser.setPasswordHash("new_hashed_password");

        when(userService.updateUser(eq(1L), any(User.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/api/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("updated_user"));
        verify(userService, times(1)).updateUser(eq(1L), any(User.class));
    }

    @Test
    void testUpdateUser_UserNotFound() throws Exception {
        User userUpdate = new User();
        userUpdate.setUsername("user");
        userUpdate.setPasswordHash("pass");

        when(userService.updateUser(eq(1L), any(User.class)))
                .thenThrow(new RuntimeException("User not found with id: 1"));

        mockMvc.perform(put("/api/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userUpdate)))
                .andExpect(status().isNotFound());
        verify(userService, times(1)).updateUser(eq(1L), any(User.class));
    }

    @Test
    void testDeleteUser() throws Exception {
        doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/api/v1/users/1"))
                .andExpect(status().isNoContent());
        verify(userService, times(1)).deleteUser(1L);
    }
}