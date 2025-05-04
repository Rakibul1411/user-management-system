// src/main/java/com/example/usermanagement/config/OpenApiConfig.java
package com.example.usermanagement.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title("User Management API")
                        .description("API for managing users and roles following Clean Architecture principles")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Rakibul")
                                .email("mdrakibul11611@gmail.com"))
                );
    }
}