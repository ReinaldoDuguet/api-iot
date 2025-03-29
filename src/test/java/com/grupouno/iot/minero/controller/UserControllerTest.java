package com.grupouno.iot.minero.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grupouno.iot.minero.dto.UserDTO;
import com.grupouno.iot.minero.dto.UserUpdateDTO;
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
        User user1 = new User("user1", "hashed_password", List.of());
        user1.setId(1L);
        User user2 = new User("user2", "hashed_password", List.of());
        user2.setId(2L);
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
        User user = new User("user1", "hashed_password", List.of());
        user.setId(1L);
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
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("new_user");
        userDTO.setPassword("password");
        userDTO.setRole("USER");

        User createdUser = new User("new_user", "hashed_password", List.of());
        createdUser.setId(1L);

        when(userService.createUser(any(UserDTO.class))).thenReturn(createdUser);

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("new_user"));

        verify(userService, times(1)).createUser(any(UserDTO.class));
    }

    @Test
    void testUpdateUser_UserFound() throws Exception {
        UserUpdateDTO updateDTO = new UserUpdateDTO();
        updateDTO.setUsername("updated_user");
        updateDTO.setPassword("new_password");

        User updatedUser = new User("updated_user", "hashed_password", List.of());
        updatedUser.setId(1L);

        when(userService.updateUser(eq(1L), any(UserUpdateDTO.class))).thenReturn(updatedUser);

        String json = objectMapper.writeValueAsString(updateDTO);
        mockMvc.perform(put("/api/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("updated_user"));
        verify(userService, times(1)).updateUser(eq(1L), any(UserUpdateDTO.class));
    }

    @Test
    void testUpdateUser_UserNotFound() throws Exception {
        UserUpdateDTO updateDTO = new UserUpdateDTO();
        updateDTO.setUsername("user");
        updateDTO.setPassword("pass");

        when(userService.updateUser(eq(1L), any(UserUpdateDTO.class)))
                .thenThrow(new RuntimeException("Usuario no encontrado con ID: 1"));

        String json = objectMapper.writeValueAsString(updateDTO);
        mockMvc.perform(put("/api/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound());
        verify(userService, times(1)).updateUser(eq(1L), any(UserUpdateDTO.class));
    }

    @Test
    void testDeleteUser() throws Exception {
        doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/api/v1/users/1"))
                .andExpect(status().isNoContent());
        verify(userService, times(1)).deleteUser(1L);
    }
}
