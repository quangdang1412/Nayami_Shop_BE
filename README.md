# Nayami Shop - Backend Service

A robust backend service for the Nayami Shop e-commerce application, built with Spring Boot and modern technologies.

## 🚀 Features

### User Management

- **Authentication**: JWT-based authentication system with token refresh mechanism
- **OAuth2 Integration**: Google authentication for seamless user onboarding
- **Role-Based Authorization**: Customer, Admin, and Staff role management
- **Password Reset**: Secure password reset functionality with email verification

### Product System

- **Product Management**: Full CRUD operations with multimedia support
- **Categorization**: Hierarchical category structure with parent-child relationships
- **Brand Management**: Brand registration, updates, and association with products
- **Search & Filter**: Advanced product search with multiple filtering criteria
- **Review System**: Customer reviews and ratings with moderation capabilities

### Shopping Experience

- **Shopping Cart**:
  - Add, update, remove items using Command Pattern
  - Persistent carts across user sessions
  - Real-time inventory validation

### Order Processing

- **Checkout Flow**: Streamlined multi-step checkout process
- **Payment Integration**:
  - PayOS for online payments (Strategy Pattern)
  - Cash On Delivery support
  - Payment verification and reconciliation
- **Order Management**:
  - Order status tracking and updates
  - Order history and details for customers
  - Administrative order processing workflows

### Promotion Engine

- **Coupon System**: Generation and validation of discount codes
- **Dynamic Discounts**: Percentage, fixed amount, and free shipping options
- **Promotion Rules**: Time-based, product-specific, and user-specific promotions (Decorator Pattern)
- **Stackable Discounts**: Support for combining multiple applicable discounts

### Notification System

- **Email Notifications**: Order confirmations, shipping updates, and password resets
- **Templated Emails**: Professionally designed email templates with Thymeleaf

### Content Delivery

- **Image Handling**: Cloudinary integration for image storage, resizing, and optimization
- **Caching**: Performance optimization with strategic data caching

### System Integration

- **Shipping Providers**: Integration with GHTK shipping service
- **External APIs**: Structured approach to third-party service integration

## 📋 System Requirements

- Java 17+
- MySQL 8.0+
- Maven 3.6+

## ⚙️ Installation & Configuration

### Step 1: Clone the repository

```bash
git clone <repository-url>
cd Nayami_Shop_BE
```

### Step 2: Set environment variables

Create a `.env` file in the root directory with the following content:

```properties
# Database
MYSQL_HOST=localhost
MYSQL_PORT=3306
MYSQL_DATABASE=nayami_shop
MYSQL_USERNAME=root
MYSQL_PASSWORD=your_password

# JWT
JWT_SECRET_KEY=your_jwt_secret_key_at_least_256_bits

# PayOS
PAYOS_CLIENT_ID=your_payos_client_id
PAYOS_API_KEY=your_payos_api_key
PAYOS_CHECKSUM_KEY=your_payos_checksum_key

# Mail
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=your_email@gmail.com
MAIL_PASSWORD=your_app_password

# Cloudinary
CLOUDINARY_CLOUD_NAME=your_cloud_name
CLOUDINARY_API_KEY=your_cloudinary_api_key
CLOUDINARY_API_SECRET=your_cloudinary_api_secret
```

### Step 3: Run the application

```bash
# Using Maven wrapper
./mvnw spring-boot:run

# Or using Maven if installed
mvn spring-boot:run
```

The application will run at `http://localhost:8080` by default.

## 🏗️ Project Architecture

### Directory structure

```
src/main/java/com/apinayami/demo/
├── config/               # JWT, Security, OpenAPI configurations
├── controller/           # REST API endpoints
├── dto/                  # Request/Response DTOs
├── exception/            # Exception handling
├── mapper/               # Entity-DTO conversions
├── model/                # JPA entities
├── repository/           # Data access layer
├── service/              # Business logic
│   └── Impl/             # Service implementations
├── util/
│   ├── Command/          # Command pattern for cart operations
│   ├── Decorator/        # Decorator pattern for promotions
│   ├── Enum/             # Enum definitions
│   ├── Strategy/         # Strategy pattern for payment methods
│   └── Validation/       # Custom validations
└── DemoApplication.java  # Entry point
```

## 🧩 Design Patterns

This project implements several design patterns:

1. **Command Pattern** (util/Command/):

   - Encapsulates cart operations (add, update, remove)
   - Facilitates maintenance and extensibility
   - Provides a clear separation of concerns

2. **Strategy Pattern** (util/Strategy/):

   - Enables flexible switching between payment methods
   - Allows easy addition of new payment providers
   - Isolates algorithm implementations from client code

3. **Decorator Pattern** (util/Decorator/):
   - Calculates order values with various promotion types
   - Extends functionality without modifying core code
   - Supports combination of multiple discounts

## 📚 API Documentation

API is documented using OpenAPI (Swagger) and can be accessed after starting the application:

```
http://localhost:8080/swagger-ui/index.html
```

## 🔐 Security

- JWT-based authentication
- CORS configuration to prevent cross-site attacks
- OAuth2 integration with Google
- Spring Security for role-based access control
- Password encryption with BCrypt

## 🧪 Testing

Run tests:

```bash
./mvnw test
```

## 🛠️ Technologies

- **Spring Boot 3.4**: Java framework
- **Spring Security**: Authentication and authorization
- **Spring Data JPA**: Database interactions
- **MySQL**: Data storage
- **JWT**: JSON Web Token for authentication
- **OAuth2**: Google login integration
- **MapStruct**: DTO-Entity mapping
- **Lombok**: Boilerplate code reduction
- **PayOS**: Online payment integration
- **OpenAPI/Swagger**: API documentation
- **Cloudinary**: Image storage and management
- **Thymeleaf**: Email templates

## 📄 License

[MIT License](LICENSE)

## 👥 Team Members

This project was developed by:

- Ha Dang Quang - Backend Developer - [GitHub](https://github.com/quangdang48)
- Nguyen Dang Quang - Database Engineer - [GitHub](https://github.com/quangdang1412)
- Le Thanh Phong - Frontend Developer - [GitHub](https://github.com/tphong0903)
- Tran Dinh Gia Bao - DevOps Specialist - [GitHub](https://github.com/BaoStar0990)

## 📧 Contact

For questions or suggestions, please open an issue or contact the development team.
