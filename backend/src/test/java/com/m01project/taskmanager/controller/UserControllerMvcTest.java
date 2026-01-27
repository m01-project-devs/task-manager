package com.m01project.taskmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.m01project.taskmanager.domain.User;
import com.m01project.taskmanager.dto.request.UserCreateRequestDto;
import com.m01project.taskmanager.dto.request.UserUpdateRequestDto;
import com.m01project.taskmanager.exception.ResourceNotFoundException;
import com.m01project.taskmanager.security.JwtAuthenticationFilter;
import com.m01project.taskmanager.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

//    @MockitoBean
//    private JwtService jwtService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    void getUser_WhenUserExists() throws Exception {
        User user = new User("test@example.com", "12345678", "Joe", "Duo");

        when(userService.findByEmail(user.getEmail())).thenReturn(user);

        mockMvc.perform(get("/api/users/test@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.firstName").value("Joe"))
                .andExpect(jsonPath("$.lastName").value("Duo"));
    }

    @Test
    void getUser_WhenUserDoesntExist_ReturnUserNotFound() throws Exception {
        when(userService.findByEmail("notfound@example.com"))
                .thenThrow(new ResourceNotFoundException("User is not found."));

        mockMvc.perform(get("/api/users/notfound@example.com"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createUser_WhenValidRequest_ShouldReturnCreatedUser() throws Exception{
        UserCreateRequestDto request = new UserCreateRequestDto(
                "test@example.com", "12345678", "Joe", "Duo");

        User savedUser = new User("test@example.com", "12345678", "Joe", "Duo");

        when(userService.create(any(UserCreateRequestDto.class))).thenReturn(savedUser);

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/users/test@example.com"))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.firstName").value("Joe"))
                .andExpect(jsonPath("$.lastName").value("Duo"));
    }

    @Test
    void createUser_WhenEmailIsInvalid_ShouldReturnBadRequest400() throws Exception {
        String json = """
                {
                    "email": "wrongemail",
                    "password": "12345678",
                    "firstName": "Joe",
                    "lastName": "Duo"
                }
                """;
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateUser_WhenUserExists() throws Exception {
        String email = "update@example.com";
        UserUpdateRequestDto request = new UserUpdateRequestDto("12345678", "John", "Smith");
        User existingUser = new User("update@example.com", "12345678", "Joe", "Duo");
        existingUser.setFirstName(request.getFirstName());
        existingUser.setLastName(request.getLastName());
        existingUser.setPassword(request.getPassword());
        when(userService.update(eq(email), any(UserUpdateRequestDto.class))).thenReturn(existingUser);

        mockMvc.perform(put("/api/users/update@example.com")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("update@example.com"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Smith"));
    }

    @Test
    void deleteUser_WhenUserExists() throws Exception {
        doNothing().when(userService).delete("delete@example.com");

        mockMvc.perform(delete("/api/users/delete@example.com"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteUser_WhenUserNotExist() throws Exception {
        doNothing().when(userService).delete("notFound@example.com");

        mockMvc.perform(delete("/api/users/notFound@example.com"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getUsers_ReturnsPagedUsers() throws Exception {
        // Mock user list
        User user1 = new User("user1@test.com", "12345678", "John", "Doe");
        User user2 = new User("user2@test.com", "12345678", "Alice", "Smith");

        List<User> users = List.of(user1, user2);

        Page<User> page = new PageImpl<>(users, PageRequest.of(0, 10), users.size());

        when(userService.getUsers(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/users")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].email").value("user1@test.com"))
                .andExpect(jsonPath("$.content[0].firstName").value("John"))
                .andExpect(jsonPath("$.content[0].lastName").value("Doe"))
                .andExpect(jsonPath("$.content[1].email").value("user2@test.com"))
                .andExpect(jsonPath("$.content[1].firstName").value("Alice"))
                .andExpect(jsonPath("$.content[1].lastName").value("Smith"))
                .andExpect(jsonPath("$.totalElements").value(2));
    }
}