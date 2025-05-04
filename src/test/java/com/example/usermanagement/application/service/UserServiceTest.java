// src/test/java/com/example/usermanagement/application/service/UserServiceTest.java
package com.example.usermanagement.application.service;

import com.example.usermanagement.application.port.RoleRepository;
import com.example.usermanagement.application.port.UserRepository;
import com.example.usermanagement.domain.Role;
import com.example.usermanagement.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(userRepository, roleRepository);
    }

    @Test
    void createUser_ValidInput_ReturnsUser() {
        // Arrange
        String name = "Test User";
        String email = "test@example.com";
        User expectedUser = new User(name, email);

        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(expectedUser);

        // Act
        User result = userService.createUser(name, email);

        // Assert
        assertNotNull(result);
        assertEquals(name, result.getName());
        assertEquals(email, result.getEmail());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void createUser_EmptyName_ThrowsException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.createUser("", "test@example.com"));

        assertEquals("User name cannot be blank", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void createUser_InvalidEmail_ThrowsException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.createUser("Test User", "invalid-email"));

        assertEquals("Valid email is required", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void createUser_DuplicateEmail_ThrowsException() {
        // Arrange
        String email = "test@example.com";
        when(userRepository.existsByEmail(email)).thenReturn(true);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.createUser("Test User", email));

        assertEquals("User with this email already exists", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void getUserById_ExistingId_ReturnsUser() {
        // Arrange
        UUID id = UUID.randomUUID();
        User expectedUser = new User(id, "Test User", "test@example.com", Collections.emptySet(), null, null);

        when(userRepository.findById(id)).thenReturn(Optional.of(expectedUser));

        // Act
        User result = userService.getUserById(id);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Test User", result.getName());
    }

    @Test
    void getUserById_NonExistingId_ThrowsException() {
        // Arrange
        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                UserService.ResourceNotFoundException.class,
                () -> userService.getUserById(id));
    }

    @Test
    void assignRoleToUser_ValidIds_AssignsRole() {
        // Arrange
        UUID userId = UUID.randomUUID();
        UUID roleId = UUID.randomUUID();

        User user = new User(userId, "Test User", "test@example.com", Collections.emptySet(), null, null);
        Role role = new Role(roleId, "ADMIN", null, null);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        User result = userService.assignRoleToUser(userId, roleId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getRoles().size());
        assertTrue(result.getRoles().contains(role));
        verify(userRepository).save(user);
    }

    @Test
    void removeRoleFromUser_ValidIds_RemovesRole() {
        // Arrange
        UUID userId = UUID.randomUUID();
        UUID roleId = UUID.randomUUID();

        Role role = new Role(roleId, "ADMIN", null, null);
        User user = new User(userId, "Test User", "test@example.com", Collections.singleton(role), null, null);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        User result = userService.removeRoleFromUser(userId, roleId);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.getRoles().size());
        verify(userRepository).save(user);
    }
}