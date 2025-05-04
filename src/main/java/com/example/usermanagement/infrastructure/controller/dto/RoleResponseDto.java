// src/main/java/com/example/usermanagement/infrastructure/controller/dto/RoleResponseDto.java
package com.example.usermanagement.infrastructure.controller.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class RoleResponseDto {
    private UUID id;
    private String roleName;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    public RoleResponseDto() {
    }

    public RoleResponseDto(UUID id, String roleName, LocalDateTime createdDate, LocalDateTime updatedDate) {
        this.id = id;
        this.roleName = roleName;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }
}