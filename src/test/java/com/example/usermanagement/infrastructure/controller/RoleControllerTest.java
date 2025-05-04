package com.example.usermanagement.infrastructure.controller;

import com.example.usermanagement.application.service.RoleService;
import com.example.usermanagement.domain.Role;
import com.example.usermanagement.infrastructure.controller.dto.RoleRequestDto;
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
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = RoleController.class)
class RoleControllerTest {

    private MockMvc mockMvc;
    private RoleService roleService;
    private RoleController roleController;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    void setUp() {
        // Create mock for RoleService
        roleService = Mockito.mock(RoleService.class);

        // Create RoleController with mocked RoleService
        roleController = new RoleController(roleService);

        // Setup MockMvc with the controller
        mockMvc = MockMvcBuilders
                .standaloneSetup(roleController)
                .build();
    }

    @Test
    void createRole_ValidInput_ReturnsCreated() throws Exception {
        // Arrange
        RoleRequestDto requestDto = new RoleRequestDto("ADMIN");
        UUID roleId = UUID.randomUUID();
        Role createdRole = new Role(roleId, "ADMIN", null, null);

        when(roleService.createRole(eq("ADMIN"))).thenReturn(createdRole);

        // Act & Assert
        mockMvc.perform(post("/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(content().string(roleId.toString()));
    }

    @Test
    void getRoleById_ExistingId_ReturnsRole() throws Exception {
        // Arrange
        UUID roleId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        Role role = new Role(roleId, "ADMIN", now, now);

        when(roleService.getRoleById(roleId)).thenReturn(role);

        // Act & Assert
        mockMvc.perform(get("/roles/{id}", roleId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(roleId.toString()))
                .andExpect(jsonPath("$.roleName").value("ADMIN"))
                .andExpect(jsonPath("$.createdDate").isNotEmpty())
                .andExpect(jsonPath("$.updatedDate").isNotEmpty());
    }

    @Test
    void getAllRoles_ReturnsAllRoles() throws Exception {
        // Arrange
        UUID roleId1 = UUID.randomUUID();
        UUID roleId2 = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        List<Role> roles = Arrays.asList(
                new Role(roleId1, "ADMIN", now, now),
                new Role(roleId2, "USER", now, now)
        );

        when(roleService.getAllRoles()).thenReturn(roles);

        // Act & Assert
        mockMvc.perform(get("/roles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(roleId1.toString()))
                .andExpect(jsonPath("$[0].roleName").value("ADMIN"))
                .andExpect(jsonPath("$[1].id").value(roleId2.toString()))
                .andExpect(jsonPath("$[1].roleName").value("USER"));
    }
}