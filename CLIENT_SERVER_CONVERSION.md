# Restaurant Management System - Client-Server Architecture with Spring Boot and Lombok

## Project Conversion Summary

This document summarizes the conversion of the Restaurant Management System from a monolithic JavaFX desktop application to a modern client-server architecture using Spring Boot and Lombok with JPA ORM.

## Changes Made

### 1. **pom.xml Updates**
- Added Spring Boot parent POM (version 3.3.0)
- Added Spring Boot Starters:
  - `spring-boot-starter-web` - for REST API
  - `spring-boot-starter-data-jpa` - for ORM
- Added Lombok with provided scope
- Updated Jackson dependencies for JSON serialization
- Configured Maven compiler with Lombok annotation processing
- Added Spring Boot Maven plugin

### 2. **Entity Classes Refactoring**
All 13 entity classes have been updated with Lombok annotations:

#### Lombok Annotations Added:
- `@Getter` - Auto-generates getter methods
- `@Setter` - Auto-generates setter methods
- `@NoArgsConstructor` - Generates no-argument constructor
- `@AllArgsConstructor` - Generates constructor with all fields
- `@EqualsAndHashCode` - Generates equals() and hashCode() methods
- `@ToString` - Generates toString() method

#### Entities Updated:
1. **Ban.java** - Table information
2. **ChiTietHoaDon.java** - Invoice details
3. **Coc.java** - Deposit/Reservation fee
4. **DonDatBan.java** - Table reservation
5. **DonLapDoiHuyBan.java** - Replacement/Cancellation order
6. **HangKhachHang.java** - Customer rank
7. **HoaDon.java** - Invoice
8. **KhachHang.java** - Customer (already refactored)
9. **KhuyenMai.java** - Promotion
10. **LoaiBan.java** - Table type
11. **MonAn.java** - Dish/Food item
12. **NhanVien.java** - Employee
13. **TaiKhoan.java** - Account/User

### 3. **Spring Boot Backend Setup**

#### New Backend Structure:
```
org/restaurant/
├── RestaurantBackendApplication.java (Main Spring Boot Application)
├── api/
│   └── controller/
│       ├── KhachHangController.java
│       ├── HoaDonController.java
│       └── ... (other controllers)
├── repository/
│   ├── BanRepository.java
│   ├── KhachHangRepository.java
│   ├── HoaDonRepository.java
│   ├── DonDatBanRepository.java
│   ├── NhanVienRepository.java
│   ├── MonAnRepository.java
│   ├── TaiKhoanRepository.java
│   └── ... (other repositories)
└── service/ (to be created)
```

#### Database Configuration (application.properties):
```properties
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=Restaurant
spring.datasource.username=sa
spring.datasource.password=
spring.datasource.driver-class-name=com.microsoft.sqlserver.jdbc.SQLServerDriver
spring.jpa.database-platform=org.hibernate.dialect.SQLServerDialect
server.port=8080
server.servlet.context-path=/api
```

### 4. **REST API Implementation**

#### REST Endpoints Pattern:
Each entity has CRUD endpoints:
```
GET    /api/{resource}           - Get all resources
GET    /api/{resource}/{id}      - Get resource by ID
POST   /api/{resource}           - Create new resource
PUT    /api/{resource}/{id}      - Update resource
DELETE /api/{resource}/{id}      - Delete resource
```

#### Available Resources:
- `/api/khach-hang` - Customers
- `/api/hoa-don` - Invoices
- `/api/don-dat-ban` - Table reservations
- `/api/mon-an` - Dishes
- `/api/nhan-vien` - Employees
- `/api/tai-khoan` - User accounts
- `/api/ban` - Tables
- `/api/promotion` - Promotions
- ... and more

### 5. **Frontend REST Client (util/RestClient.java)**

A utility class has been created for the JavaFX frontend to communicate with the backend:

```java
// Get all customers
List<KhachHang> customers = RestClient.getAll("/khach-hang", KhachHang.class);

// Get a specific customer
KhachHang customer = RestClient.getById("/khach-hang", "KH001", KhachHang.class);

// Create a new customer
KhachHang newCustomer = new KhachHang(...);
KhachHang created = RestClient.create("/khach-hang", newCustomer, KhachHang.class);

// Update a customer
RestClient.update("/khach-hang", "KH001", updatedCustomer, KhachHang.class);

// Delete a customer
RestClient.delete("/khach-hang", "KH001");
```

## Architecture Overview

### Two-Tier Architecture:
```
┌─────────────────────────────┐
│    JavaFX Client App        │ (Desktop UI)
├─────────────────────────────┤
│    REST Client (util)       │ (HTTP Communication)
│───────────────────────────────│ REST API over HTTPS/HTTP
│ Spring Boot REST Server     │ (Backend API)
├─────────────────────────────┤
│ Spring Data JPA Repositories│ (ORM Layer)
├─────────────────────────────┤
│  Hibernate / JPA            │ (Persistence)
├─────────────────────────────┤
│  SQL Server Database        │ (Data Storage)
└─────────────────────────────┘
```

## Benefits of This Architecture

1. **Separation of Concerns** - Frontend and backend are independently deployable
2. **Scalability** - Backend can be deployed on a server, accessed by multiple clients
3. **Modern ORM** - Spring Data JPA with Hibernate provides clean ORM
4. **Reduced Boilerplate** - Lombok eliminates verbose getters/setters
5. **RESTful API** - Standard HTTP-based communication
6. **Database Agnostic** - Easy to switch databases via Hibernate dialect
7. **Security Ready** - Spring Security can be added for authentication/authorization

## Next Steps

### To Complete the Migration:

1. **Backend Services Layer**
   - Create service classes that use repositories
   - Add business logic layer

2. **Remaining REST Controllers**
   - Create controllers for all entities
   - Add custom endpoints for specific use cases

3. **Frontend Refactoring**
   - Update existing JavaFX controllers to use RestClient
   - Replace direct DAO calls with REST API calls
   - Add error handling and loading indicators

4. **Authentication & Authorization**
   - Add Spring Security
   - Implement JWT or OAuth2
   - Secure sensitive endpoints

5. **Validation & Error Handling**
   - Add request validation
   - Implement global exception handlers
   - Add meaningful error messages

6. **Testing**
   - Unit tests for services
   - Integration tests for controllers
   - End-to-end tests

## Running the Application

### Backend:
```bash
cd restaurant
mvn clean install
mvn spring-boot:run
```

The backend server will start at: `http://localhost:8080/api`

### Frontend:
Keep the existing JavaFX launcher and update controllers to use RestClient

## Database Connection

Update the database credentials in `application.properties`:
```properties
spring.datasource.url=jdbc:sqlserver://YOUR_SERVER:1433;databaseName=Restaurant
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
```

## Lombok Dependencies

Lombok is already added to pom.xml. Make sure your IDE supports Lombok:
- Eclipse: Install Lombok plugin from `https://projectlombok.org/setup/eclipse`
- IntelliJ: Enable annotation processing in Settings > Build > Compiler > Annotation Processors
- VS Code: Install "Lombok Annotations Support for VS Code" extension

## Key Files Created/Modified

### Created:
- `org/restaurant/RestaurantBackendApplication.java`
- `org/restaurant/api/controller/KhachHangController.java`
- `org/restaurant/api/controller/HoaDonController.java`
- `org/restaurant/repository/BanRepository.java`
- `org/restaurant/repository/KhachHangRepository.java`
- `org/restaurant/repository/HoaDonRepository.java`
- `org/restaurant/repository/DonDatBanRepository.java`
- `org/restaurant/repository/NhanVienRepository.java`
- `org/restaurant/repository/MonAnRepository.java`
- `org/restaurant/repository/TaiKhoanRepository.java`
- `util/RestClient.java` (for frontend REST communication)

### Modified:
- `pom.xml` - Added Spring Boot dependencies
- `src/main/resources/application.properties` - Database and server configuration
- All entity files in `entity/` - Added Lombok annotations

## Support & Troubleshooting

### Common Issues:

1. **Lombok annotations not working**
   - Enable annotation processing in IDE
   - Rebuild project

2. **Connection refused when calling API**
   - Ensure backend server is running on port 8080
   - Check firewall settings

3. **Entity mapping errors**
   - Ensure all entity classes are in the entity package
   - Verify @Entity and @Table annotations

4. **REST client errors**
   - Check network connectivity
   - Verify endpoint URLs match actual routes
   - Check request/response JSON format

## Future Enhancements

- [ ] Add pagination to GET endpoints
- [ ] Add filtering and sorting capabilities
- [ ] Implement caching layer
- [ ] Add API documentation (Swagger/OpenAPI)
- [ ] Add logging and monitoring
- [ ] Implement async operations
- [ ] Add rate limiting
- [ ] Implement API versioning
