// src/main/java/com/example/usermanagement/infrastructure/persistence/adapter/RoleRepositoryAdapter.java
package com.example.usermanagement.infrastructure.persistence.adapter;

import com.example.usermanagement.application.port.RoleRepository;
import com.example.usermanagement.domain.Role;
import com.example.usermanagement.infrastructure.persistence.entity.RoleJpaEntity;
import com.example.usermanagement.infrastructure.persistence.repository.RoleJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class RoleRepositoryAdapter implements RoleRepository {
    private final RoleJpaRepository roleJpaRepository;

    public RoleRepositoryAdapter(RoleJpaRepository roleJpaRepository) {
        this.roleJpaRepository = roleJpaRepository;
    }

    @Override
    public Role save(Role role) {
        RoleJpaEntity roleJpaEntity = toJpaEntity(role);
        RoleJpaEntity savedEntity = roleJpaRepository.save(roleJpaEntity);
        return toDomainEntity(savedEntity);
    }

    @Override
    public Optional<Role> findById(UUID id) {
        return roleJpaRepository.findById(id)
                .map(this::toDomainEntity);
    }

    @Override
    public List<Role> findAll() {
        return roleJpaRepository.findAll().stream()
                .map(this::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        roleJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsByRoleName(String roleName) {
        return roleJpaRepository.existsByRoleName(roleName);
    }

    private RoleJpaEntity toJpaEntity(Role role) {
        return new RoleJpaEntity(
                role.getId(),
                role.getRoleName(),
                role.getCreatedDate(),
                role.getUpdatedDate()
        );
    }

    private Role toDomainEntity(RoleJpaEntity roleJpaEntity) {
        return new Role(
                roleJpaEntity.getId(),
                roleJpaEntity.getRoleName(),
                roleJpaEntity.getCreatedDate(),
                roleJpaEntity.getUpdatedDate()
        );
    }
}