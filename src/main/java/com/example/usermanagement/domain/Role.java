// src/main/java/com/example/usermanagement/domain/Role.java
package com.example.usermanagement.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public class Role {
    private final UUID id;
    private String roleName;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    public Role(String roleName) {
        this.id = UUID.randomUUID();
        this.roleName = roleName;
        this.createdDate = LocalDateTime.now();
        this.updatedDate = LocalDateTime.now();
    }

    public Role(UUID id, String roleName, LocalDateTime createdDate, LocalDateTime updatedDate) {
        this.id = id;
        this.roleName = roleName;
        this.createdDate = createdDate != null ? createdDate : LocalDateTime.now();
        this.updatedDate = updatedDate != null ? updatedDate : LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
        this.updatedDate = LocalDateTime.now();
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return id.equals(role.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}