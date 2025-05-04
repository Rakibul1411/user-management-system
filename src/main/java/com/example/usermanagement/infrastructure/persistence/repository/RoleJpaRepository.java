// src/main/java/com/example/usermanagement/infrastructure/persistence/repository/RoleJpaRepository.java
package com.example.usermanagement.infrastructure.persistence.repository;

import com.example.usermanagement.infrastructure.persistence.entity.RoleJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RoleJpaRepository extends JpaRepository<RoleJpaEntity, UUID> {
    boolean existsByRoleName(String roleName);
}