# Todo App Backend

A robust Spring Boot backend for the Todo application with JWT authentication and MySQL database.

## Features

- User Authentication (Login/Register) with JWT
- Note Management (Create, Read, Update, Delete)
- Todo Management within Notes
- User-specific Data Isolation
- RESTful API Design
- MySQL Database Integration
- Spring Security Implementation

## Prerequisites

- Java 17 or higher
- MySQL 8.0 or higher
- Maven 3.6 or higher

## Tech Stack

- Spring Boot 3.2.2
- Spring Security
- Spring Data JPA
- MySQL
- JWT (JSON Web Tokens)
- Maven

## Getting Started

### Database Configuration

1. Create a MySQL database:
```sql
CREATE DATABASE tododb;
```

2. Update database configuration in `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/tododb?useSSL=false&serverTimezone=UTC
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### Building and Running

1. Clone the repository:
```bash
git clone <repository-url>
cd todo-app-backend
```

2. Build the project:
```bash
mvn clean install
```

3. Run the application:
```bash
mvn spring-boot:run
```

The server will start on port 8080.

## API Endpoints

### Authentication

- `POST /api/auth/register` - Register a new user
- `POST /api/auth/login` - Login and get JWT token

### Notes

- `GET /api/notes/user/{userId}` - Get all notes for a user
- `POST /api/notes` - Create a new note
- `DELETE /api/notes/{id}` - Delete a note

### Todos

- `POST /api/notes/{noteId}/todos` - Add a todo to a note
- `PUT /api/notes/{noteId}/todos/{todoId}` - Update a todo
- `DELETE /api/notes/{noteId}/todos/{todoId}` - Delete a todo

## Recent Changes

### Fixed User Authentication and Note Management

1. Enhanced User-Note relationship
   - Added proper JPA relationships
   - Implemented user-specific note fetching
   - Fixed JSON serialization issues

2. Improved Database Queries
   - Added JPQL query for efficient note fetching
   - Optimized lazy loading configuration

3. Security Enhancements
   - Added proper JWT configuration
   - Implemented user data isolation

4. Entity Improvements
   - Added JSON serialization annotations
   - Fixed lazy loading issues
   - Added proper cascade operations

## Security

- JWT-based authentication
- Password encryption using BCrypt
- Role-based authorization
- Secure endpoints with Spring Security

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details 
 