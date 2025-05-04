// src/main/java/com/example/usermanagement/infrastructure/persistence/repository/UserJpaRepository.java
package com.example.usermanagement.infrastructure.persistence.repository;

import com.example.usermanagement.infrastructure.persistence.entity.UserJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserJpaRepository extends JpaRepository<UserJpaEntity, UUID> {
    boolean existsByEmail(String email);
    Page<UserJpaEntity> findAll(Pageable pageable);
}