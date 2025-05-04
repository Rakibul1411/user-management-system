// src/main/java/com/example/usermanagement/infrastructure/persistence/adapter/UserRepositoryAdapter.java
package com.example.usermanagement.infrastructure.persistence.adapter;

import com.example.usermanagement.application.port.UserRepository;
import com.example.usermanagement.domain.Role;
import com.example.usermanagement.domain.User;
import com.example.usermanagement.infrastructure.persistence.entity.RoleJpaEntity;
import com.example.usermanagement.infrastructure.persistence.entity.UserJpaEntity;
import com.example.usermanagement.infrastructure.persistence.repository.UserJpaRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class UserRepositoryAdapter implements UserRepository {
    private final UserJpaRepository userJpaRepository;

    public UserRepositoryAdapter(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public User save(User user) {
        UserJpaEntity userJpaEntity = toJpaEntity(user);
        UserJpaEntity savedEntity = userJpaRepository.save(userJpaEntity);
        return toDomainEntity(savedEntity);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return userJpaRepository.findById(id)
                .map(this::toDomainEntity);
    }

    @Override
    public List<User> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userJpaRepository.findAll(pageable).getContent()
                .stream()
                .map(this::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        userJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userJpaRepository.existsByEmail(email);
    }

    @Override
    public long count() {
        return userJpaRepository.count();
    }

    private UserJpaEntity toJpaEntity(User user) {
        UserJpaEntity userJpaEntity = new UserJpaEntity(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCreatedDate(),
                user.getUpdatedDate()
        );

        Set<RoleJpaEntity> roleEntities = user.getRoles().stream()
                .map(this::roleToJpaEntity)
                .collect(Collectors.toSet());

        userJpaEntity.setRoles(roleEntities);

        return userJpaEntity;
    }

    private RoleJpaEntity roleToJpaEntity(Role role) {
        return new RoleJpaEntity(
                role.getId(),
                role.getRoleName(),
                role.getCreatedDate(),
                role.getUpdatedDate()
        );
    }

    private User toDomainEntity(UserJpaEntity userJpaEntity) {
        Set<Role> roles = userJpaEntity.getRoles().stream()
                .map(this::jpaEntityToRole)
                .collect(Collectors.toSet());

        return new User(
                userJpaEntity.getId(),
                userJpaEntity.getName(),
                userJpaEntity.getEmail(),
                roles,
                userJpaEntity.getCreatedDate(),
                userJpaEntity.getUpdatedDate()
        );
    }

    private Role jpaEntityToRole(RoleJpaEntity roleJpaEntity) {
        return new Role(
                roleJpaEntity.getId(),
                roleJpaEntity.getRoleName(),
                roleJpaEntity.getCreatedDate(),
                roleJpaEntity.getUpdatedDate()
        );
    }
}