# User Management & Role Assignment System

A Spring Boot application implementing a User Management System following Robert C. Martin's Clean Architecture principles.

## Overview

This system allows:
- Creating Users and Roles
- Assigning Roles to Users
- Retrieving User details along with assigned Roles

The application strictly follows Clean Architecture, separating concerns into Domain, Application, Infrastructure, and Configuration layers.

## Technology Stack

- Java 17
- Spring Boot 3.x
- H2 Database (in-memory)
- Maven/Gradle

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── example/
│   │           └── usermanagement/
│   │               ├── domain/
│   │               │   ├── User.java
│   │               │   └── Role.java
│   │               ├── application/
│   │               │   ├── service/
│   │               │   │   ├── UserService.java
│   │               │   │   └── RoleService.java
│   │               │   └── port/
│   │               │       ├── UserRepository.java
│   │               │       └── RoleRepository.java
│   │               ├── infrastructure/
│   │               │   ├── controller/
│   │               │   │   ├── UserController.java
│   │               │   │   ├── RoleController.java
│   │               │   │   └── dto/
│   │               │   │       ├── UserRequestDto.java
│   │               │   │       ├── UserResponseDto.java
│   │               │   │       ├── RoleRequestDto.java
│   │               │   │       └── RoleResponseDto.java
│   │               │   ├── persistence/
│   │               │   │   ├── entity/
│   │               │   │   │   ├── UserJpaEntity.java
│   │               │   │   │   └── RoleJpaEntity.java
│   │               │   │   ├── repository/
│   │               │   │   │   ├── UserJpaRepository.java
│   │               │   │   │   └── RoleJpaRepository.java
│   │               │   │   └── adapter/
│   │               │   │       ├── UserRepositoryAdapter.java
│   │               │   │       └── RoleRepositoryAdapter.java
│   │               │   └── exception/
│   │               │       ├── GlobalExceptionHandler.java
│   │               │       ├── ResourceNotFoundException.java
│   │               │       └── ErrorResponse.java
│   │               └── config/
│   │                   └── BeanConfig.java
│   └── resources/
│       ├── application.properties
│       └── data.sql
└── test/
    └── java/
        └── com/
            └── example/
                └── usermanagement/
                    ├── application/
                    │   └── service/
                    │       ├── UserServiceTest.java
                    │       └── RoleServiceTest.java
                    └── infrastructure/
                        └── controller/
                            ├── UserControllerTest.java
                            └── RoleControllerTest.java
```

## Setup Instructions

### Prerequisites

- JDK 17+
- Maven 3.6+ or Gradle 7+
- Git

### Clone Repository

```bash
git clone https://github.com/yourusername/user-management-system.git
cd user-management-system
```

### Build the Application

Using Maven:
```bash
mvn clean install
```

### Run the Application

Using Maven:
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### Access H2 Console

H2 Database console is available at: `http://localhost:8080/h2-console`

Connection details:
- JDBC URL: `jdbc:h2:mem:usermanagement`
- Username: `sa`
- Password: (leave empty)

## API Endpoints

| HTTP Method | URL | Request Body | Description |
|-------------|-----|--------------|-------------|
| POST | `/users` | `{ "name": "John Doe", "email": "john@example.com" }` | Create a new user |
| POST | `/roles` | `{ "roleName": "ADMIN" }` | Create a new role |
| POST | `/users/{userId}/assign-role/{roleId}` | (Empty body) | Assign role to user |
| GET | `/users/{id}` | - | Get user details with roles |

## Running Tests

Using Maven:
```bash
mvn test
```

## API Documentation

If you've implemented the optional Swagger integration, access the API documentation at:
`http://localhost:8080/swagger-ui.html`

## Validation Rules

- User email must be properly formatted
- User name must not be blank
- Role name must not be blank

## Error Handling

The application returns appropriate HTTP status codes:
- 400: Bad Request (validation errors)
- 404: Not Found (resource not found)
- 500: Internal Server Error

## Architecture Details

This application strictly follows Clean Architecture principles:

1. **Domain Layer**: Core business entities with no framework dependencies
2. **Application Layer**: Use cases and business rules with repository interfaces (ports)
3. **Infrastructure Layer**: Adapters for persistence and controllers
4. **Configuration Layer**: Bean configuration and dependency injection setup

Dependencies point inward, with the domain layer having no dependencies on outer layers.
