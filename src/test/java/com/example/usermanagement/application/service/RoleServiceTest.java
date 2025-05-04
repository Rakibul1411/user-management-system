// src/test/java/com/example/usermanagement/application/service/RoleServiceTest.java
package com.example.usermanagement.application.service;

import com.example.usermanagement.application.port.RoleRepository;
import com.example.usermanagement.domain.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    private RoleService roleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        roleService = new RoleService(roleRepository);
    }

    @Test
    void createRole_ValidInput_ReturnsRole() {
        // Arrange
        String roleName = "ADMIN";
        Role expectedRole = new Role(roleName);

        when(roleRepository.existsByRoleName(roleName)).thenReturn(false);
        when(roleRepository.save(any(Role.class))).thenReturn(expectedRole);

        // Act
        Role result = roleService.createRole(roleName);

        // Assert
        assertNotNull(result);
        assertEquals(roleName, result.getRoleName());
        verify(roleRepository).save(any(Role.class));
    }

    @Test
    void createRole_EmptyName_ThrowsException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> roleService.createRole(""));

        assertEquals("Role name cannot be blank", exception.getMessage());
        verify(roleRepository, never()).save(any(Role.class));
    }

    @Test
    void createRole_DuplicateName_ThrowsException() {
        // Arrange
        String roleName = "ADMIN";
        when(roleRepository.existsByRoleName(roleName)).thenReturn(true);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> roleService.createRole(roleName));

        assertEquals("Role with this name already exists", exception.getMessage());
        verify(roleRepository, never()).save(any(Role.class));
    }

    @Test
    void getRoleById_ExistingId_ReturnsRole() {
        // Arrange
        UUID id = UUID.randomUUID();
        Role expectedRole = new Role(id, "ADMIN", null, null);

        when(roleRepository.findById(id)).thenReturn(Optional.of(expectedRole));

        // Act
        Role result = roleService.getRoleById(id);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("ADMIN", result.getRoleName());
    }

    @Test
    void getRoleById_NonExistingId_ThrowsException() {
        // Arrange
        UUID id = UUID.randomUUID();
        when(roleRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                RoleService.ResourceNotFoundException.class,
                () -> roleService.getRoleById(id));
    }

    @Test
    void getAllRoles_ReturnsAllRoles() {
        // Arrange
        Role role1 = new Role(UUID.randomUUID(), "ADMIN", null, null);
        Role role2 = new Role(UUID.randomUUID(), "USER", null, null);
        List<Role> expectedRoles = Arrays.asList(role1, role2);

        when(roleRepository.findAll()).thenReturn(expectedRoles);

        // Act
        List<Role> result = roleService.getAllRoles();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("ADMIN", result.get(0).getRoleName());
        assertEquals("USER", result.get(1).getRoleName());
    }
}