// src/main/java/com/example/usermanagement/infrastructure/controller/RoleController.java
package com.example.usermanagement.infrastructure.controller;

import com.example.usermanagement.application.service.RoleService;
import com.example.usermanagement.domain.Role;
import com.example.usermanagement.infrastructure.controller.dto.RoleRequestDto;
import com.example.usermanagement.infrastructure.controller.dto.RoleResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/roles")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping
    public ResponseEntity<UUID> createRole(@Valid @RequestBody RoleRequestDto roleRequestDto) {
        Role role = roleService.createRole(roleRequestDto.getRoleName());
        return new ResponseEntity<>(role.getId(), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleResponseDto> getRoleById(@PathVariable UUID id) {
        Role role = roleService.getRoleById(id);
        return ResponseEntity.ok(mapToRoleResponseDto(role));
    }

    @GetMapping
    public ResponseEntity<List<RoleResponseDto>> getAllRoles() {
        List<Role> roles = roleService.getAllRoles();
        List<RoleResponseDto> roleDtos = roles.stream()
                .map(this::mapToRoleResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(roleDtos);
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