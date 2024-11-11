# User Authentication and Authorization API

This project is a demonstration of how to use Spring Security  in a Spring Boot application. It utilizes various features like 
- Authentication with Json Web Token (`JWT`) , 
- Authorization and Roles 
- Confirm registration by email
- Validation of incomming payload
- Best practices for handling errors using a global exception handler and custom exception. 
- Multi profiles (dev, prod)
- OpenApi documentaion (Swagger)


## Architecture
### Layered Architecture
Controller --> Service --> Repository

## Controllers

The main controller, `UserController`, handles operations for user entities. It leverages the power of Spring's validation annotations to ensure data integrity and adherence to business rules.

### Endpoints

1. **Register User**
    - Endpoint: `POST /auth/signup`
    - Validates the incoming `RegisterUserDto` using the `@Valid` annotation.
    - Returns the created user if successful, otherwise returns a detailed error response.

2. **Authenticate a User**
    - Endpoint: `POST /auth/login`
    - Returns  JWT

3. **Get User Current User**
    - Endpoint: `GET /users/me`
    - Retrieve the current authenticated user


## DTOs (Data Transfer Objects)

The project uses `RegisterUserDto` to transfer user-related data between the client and server. The DTO class incorporates various validation annotations to ensure the correctness of the data.

### RegisterUserDto Validation Annotations

- `@NotBlank`: Ensures that fields like name and email are not blank.
- `@Email`: Validates the email format.
- `@Pattern`: Ensures that the email follows a specific pattern.
- `@Size`: Specifies the maximum size for email and password.

## Models

The project includes several models like `User`, `Role`,  and a base entity `BaseEntity`. These entities are used to represent data in the database.

## Exception Handling

The project employs a global exception handler (`GlobalExceptionHandler`) to handle different types of exceptions, providing consistent and detailed error responses.

