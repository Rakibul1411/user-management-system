// src/main/java/com/example/usermanagement/config/BeanConfig.java
package com.example.usermanagement.config;

import com.example.usermanagement.application.port.RoleRepository;
import com.example.usermanagement.application.port.UserRepository;
import com.example.usermanagement.application.service.RoleService;
import com.example.usermanagement.application.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public UserService userService(UserRepository userRepository, RoleRepository roleRepository) {
        return new UserService(userRepository, roleRepository);
    }

    @Bean
    public RoleService roleService(RoleRepository roleRepository) {
        return new RoleService(roleRepository);
    }
}