package com.example.usermanagement.infrastructure.controller;

import com.example.usermanagement.application.service.UserService;
import com.example.usermanagement.domain.Role;
import com.example.usermanagement.domain.User;
import com.example.usermanagement.infrastructure.controller.dto.UserRequestDto;
import com.example.usermanagement.infrastructure.controller.dto.PageResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    private MockMvc mockMvc;
    private UserService userService;
    private UserController userController;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    void setUp() {
        // Create mock for UserService
        userService = Mockito.mock(UserService.class);

        // Create UserController with mocked UserService
        userController = new UserController(userService);

        // Setup MockMvc with the controller
        mockMvc = MockMvcBuilders
                .standaloneSetup(userController)
                .build();
    }

    @Test
    void createUser_ValidInput_ReturnsCreated() throws Exception {
        // Arrange
        UserRequestDto requestDto = new UserRequestDto("Test User", "test@example.com");
        UUID userId = UUID.randomUUID();
        User createdUser = new User(userId, "Test User", "test@example.com", Collections.emptySet(), null, null);

        when(userService.createUser(eq("Test User"), eq("test@example.com"))).thenReturn(createdUser);

        // Act & Assert
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(content().string(userId.toString()));
    }

    @Test
    void getUserById_ExistingId_ReturnsUser() throws Exception {
        // Arrange
        UUID userId = UUID.randomUUID();
        Set<Role> roles = new HashSet<>();
        roles.add(new Role(UUID.randomUUID(), "ADMIN", null, null));

        User user = new User(userId, "Test User", "test@example.com", roles, LocalDateTime.now(), LocalDateTime.now());

        when(userService.getUserById(userId)).thenReturn(user);

        // Act & Assert
        mockMvc.perform(get("/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId.toString()))
                .andExpect(jsonPath("$.name").value("Test User"))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.roles[0].roleName").value("ADMIN"));
    }

    @Test
    void getAllUsers_ReturnsPageOfUsers() throws Exception {
        // Arrange
        int page = 0;
        int size = 10;

        UUID userId1 = UUID.randomUUID();
        UUID userId2 = UUID.randomUUID();

        List<User> users = Arrays.asList(
                new User(userId1, "User One", "user1@example.com", Collections.emptySet(), LocalDateTime.now(), LocalDateTime.now()),
                new User(userId2, "User Two", "user2@example.com", Collections.emptySet(), LocalDateTime.now(), LocalDateTime.now())
        );

        long totalElements = 2;

        when(userService.getAllUsers(page, size)).thenReturn(users);
        when(userService.getUserCount()).thenReturn(totalElements);

        // Act & Assert
        mockMvc.perform(get("/users")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].id").value(userId1.toString()))
                .andExpect(jsonPath("$.content[1].id").value(userId2.toString()))
                .andExpect(jsonPath("$.pageNumber").value(page))
                .andExpect(jsonPath("$.pageSize").value(size))
                .andExpect(jsonPath("$.totalElements").value(totalElements))
                .andExpect(jsonPath("$.totalPages").value(1));
    }

    @Test
    void assignRoleToUser_ValidIds_ReturnsSuccess() throws Exception {
        // Arrange
        UUID userId = UUID.randomUUID();
        UUID roleId = UUID.randomUUID();

        User updatedUser = new User(userId, "Test User", "test@example.com",
                Collections.singleton(new Role(roleId, "ADMIN", null, null)),
                LocalDateTime.now(), LocalDateTime.now());

        when(userService.assignRoleToUser(userId, roleId)).thenReturn(updatedUser);

        // Act & Assert
        mockMvc.perform(post("/users/{userId}/assign-role/{roleId}", userId, roleId))
                .andExpect(status().isOk())
                .andExpect(content().string("Role assigned successfully"));
    }

    @Test
    void removeRoleFromUser_ValidIds_ReturnsSuccess() throws Exception {
        // Arrange
        UUID userId = UUID.randomUUID();
        UUID roleId = UUID.randomUUID();

        User updatedUser = new User(userId, "Test User", "test@example.com",
                Collections.emptySet(), LocalDateTime.now(), LocalDateTime.now());

        when(userService.removeRoleFromUser(userId, roleId)).thenReturn(updatedUser);

        // Act & Assert
        mockMvc.perform(delete("/users/{userId}/remove-role/{roleId}", userId, roleId))
                .andExpect(status().isOk())
                .andExpect(content().string("Role removed successfully"));
    }
}