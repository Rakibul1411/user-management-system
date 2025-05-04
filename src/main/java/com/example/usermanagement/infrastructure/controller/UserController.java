// src/main/java/com/example/usermanagement/infrastructure/controller/UserController.java
package com.example.usermanagement.infrastructure.controller;

import com.example.usermanagement.application.service.UserService;
import com.example.usermanagement.domain.Role;
import com.example.usermanagement.domain.User;
import com.example.usermanagement.infrastructure.controller.dto.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UUID> createUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        User user = userService.createUser(userRequestDto.getName(), userRequestDto.getEmail());
        return new ResponseEntity<>(user.getId(), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable UUID id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(mapToUserResponseDto(user));
    }

    @GetMapping
    public ResponseEntity<PageResponseDto<UserResponseDto>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        List<User> users = userService.getAllUsers(page, size);
        long totalElements = userService.getUserCount();
        int totalPages = (int) Math.ceil((double) totalElements / size);

        List<UserResponseDto> userDtos = users.stream()
                .map(this::mapToUserResponseDto)
                .collect(Collectors.toList());

        PageResponseDto<UserResponseDto> response = new PageResponseDto<>(
                userDtos, page, size, totalElements, totalPages);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{userId}/assign-role/{roleId}")
    public ResponseEntity<String> assignRoleToUser(
            @PathVariable UUID userId,
            @PathVariable UUID roleId) {

        userService.assignRoleToUser(userId, roleId);
        return ResponseEntity.ok("Role assigned successfully");
    }

    @DeleteMapping("/{userId}/remove-role/{roleId}")
    public ResponseEntity<String> removeRoleFromUser(
            @PathVariable UUID userId,
            @PathVariable UUID roleId) {

        userService.removeRoleFromUser(userId, roleId);
        return ResponseEntity.ok("Role removed successfully");
    }

    private UserResponseDto mapToUserResponseDto(User user) {
        Set<RoleResponseDto> roleDtos = user.getRoles().stream()
                .map(this::mapToRoleResponseDto)
                .collect(Collectors.toSet());

        return new UserResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                roleDtos,
                user.getCreatedDate(),
                user.getUpdatedDate()
        );
    }

    private RoleResponseDto mapToRoleResponseDto(Role role) {
        return new RoleResponseDto(
                role.getId(),
                role.getRoleName(),
                role.getCreatedDate(),
                role.getUpdatedDate()
        );
    }
}