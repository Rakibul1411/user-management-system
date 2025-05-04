// src/main/java/com/example/usermanagement/application/port/UserRepository.java
package com.example.usermanagement.application.port;

import com.example.usermanagement.domain.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(UUID id);
    List<User> findAll(int page, int size);
    void deleteById(UUID id);
    boolean existsByEmail(String email);
    long count();
}