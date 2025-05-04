// src/main/java/com/example/usermanagement/infrastructure/exception/ResourceNotFoundException.java
package com.example.usermanagement.infrastructure.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}