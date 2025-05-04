// src/main/java/com/example/usermanagement/infrastructure/controller/dto/RoleRequestDto.java
package com.example.usermanagement.infrastructure.controller.dto;

import jakarta.validation.constraints.NotBlank;

public class RoleRequestDto {
    @NotBlank(message = "Role name is required")
    private String roleName;

    public RoleRequestDto() {
    }

    public RoleRequestDto(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
