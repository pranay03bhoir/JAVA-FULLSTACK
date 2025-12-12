# Spring Boot E-Commerce Application

A full-featured E-Commerce application built with Spring Boot, Spring Security, JWT Authentication, and PostgreSQL. This
application provides a complete e-commerce solution with user authentication, product management, shopping cart, and
order processing.

## 🚀 Features

- **User Authentication & Authorization**
    - JWT-based authentication
    - Role-based access control (Admin/User)
    - Secure password hashing with BCrypt
    - Refresh token mechanism

- **Product Management**
    - CRUD operations for products
    - Product categories and subcategories
    - Product images support
    - Search and filter products

- **Shopping Cart**
    - Add/remove items from cart
    - Update quantities
    - Persistent cart for logged-in users

- **Order Processing**
    - Create and track orders
    - Order history for users
    - Order status updates

- **API Documentation**
    - Interactive API documentation with Swagger UI
    - JWT authentication support in Swagger

## 🛠 Tech Stack

- **Backend**
    - Java 17
    - Spring Boot 3.x
    - Spring Security
    - Spring Data JPA
    - JWT Authentication
    - Maven

- **Database**
    - PostgreSQL (Primary Database)
    - H2 Database (Development)

- **Tools**
    - Swagger/OpenAPI 3.0
    - Lombok
    - MapStruct
    - ModelMapper

## 📦 Prerequisites

- Java 17 or higher
- Maven 3.6.3 or higher
- PostgreSQL 13 or higher
- (Optional) Docker

## 🚀 Quick Start

### Local Development Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/pranay03bhoir/JAVA-FULLSTACK.git
   cd JAVA-FULLSTACK/sb-ecom
   ```

2. **Configure Database**
    - Create a PostgreSQL database named `sb-ecom`
    - Update database credentials in `src/main/resources/application.properties`

3. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

4. **Access the application**
    - API Documentation: http://localhost:8080/swagger-ui.html
    - H2 Console (if enabled): http://localhost:8080/h2-console

### Using Docker

```bash
# Build the application
mvn clean package

# Build and start containers
docker-compose up --build
```

## 🔧 Configuration

Key configuration in `application.properties`:

```properties
# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/sb-ecom
spring.datasource.username=your_username
spring.datasource.password=your_password
# JWT Configuration
spring.app.jwtSecret=your_jwt_secret
spring.app.jwtExpirationMs=86400000
spring.app.jwtCookieName=auth_token
# File Upload
project.images=images/
```

## 🔒 Security

The application uses JWT (JSON Web Tokens) for authentication. The security configuration includes:

- CSRF protection
- CORS configuration
- Role-based access control
- Password encryption with BCrypt
- JWT token validation

## 📚 API Documentation

API documentation is available using Swagger UI:

- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs

## 📂 Project Structure

```
sb-ecom/
├── src/
│   ├── main/
│   │   ├── java/com/ecommerce/sbecom/
│   │   │   ├── config/          # Configuration classes
│   │   │   ├── controllers/     # REST controllers
│   │   │   ├── exceptions/      # Exception handling
│   │   │   ├── models/          # JPA entities
│   │   │   ├── payload/         # DTOs and requests/responses
│   │   │   ├── repositories/    # JPA repositories
│   │   │   ├── security/        # Security configuration
│   │   │   ├── services/        # Business logic
│   │   │   └── utils/           # Utility classes
│   │   └── resources/
│   │       └── application.properties
│   └── test/                   # Test classes
└── pom.xml
```

## 📄 License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.

## 👨‍💻 Author

- **Pranay Bhoir**
    - Email: pranaytb777@gmail.com
    - GitHub: [pranay03bhoir](https://github.com/pranay03bhoir)

## 🙏 Acknowledgments

- Spring Boot Team
- Open Source Community
- All Contributors