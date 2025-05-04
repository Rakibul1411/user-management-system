// src/main/java/com/example/usermanagement/domain/User.java
package com.example.usermanagement.domain;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class User {
    private final UUID id;
    private String name;
    private String email;
    private final Set<Role> roles;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    public User(String name, String email) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.email = email;
        this.roles = new HashSet<>();
        this.createdDate = LocalDateTime.now();
        this.updatedDate = LocalDateTime.now();
    }

    public User(UUID id, String name, String email, Set<Role> roles, LocalDateTime createdDate, LocalDateTime updatedDate) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.roles = roles != null ? roles : new HashSet<>();
        this.createdDate = createdDate != null ? createdDate : LocalDateTime.now();
        this.updatedDate = updatedDate != null ? updatedDate : LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.updatedDate = LocalDateTime.now();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        this.updatedDate = LocalDateTime.now();
    }

    public Set<Role> getRoles() {
        return new HashSet<>(roles);
    }

    public void addRole(Role role) {
        roles.add(role);
        this.updatedDate = LocalDateTime.now();
    }

    public void removeRole(Role role) {
        roles.remove(role);
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
}