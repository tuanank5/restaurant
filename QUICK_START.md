# Quick Start Guide - Restaurant Management System (Client-Server)

## What Has Been Converted

Your Restaurant Management System has been successfully converted from a monolithic JavaFX desktop application to a modern **Client-Server architecture** with:

✅ **Spring Boot REST Backend**
✅ **JPA ORM with Hibernate**
✅ **Lombok for reduced boilerplate**
✅ **Spring Data JPA repositories**
✅ **REST API controllers**
✅ **REST client for JavaFX frontend**

## Project Structure

```
restaurant/
├── pom.xml                                    # Updated with Spring Boot & Lombok
├── src/main/java/
│   ├── org/restaurant/
│   │   ├── RestaurantBackendApplication.java # Spring Boot main class
│   │   ├── api/controller/
│   │   │   ├── KhachHangController.java      # Customer REST API
│   │   │   └── HoaDonController.java         # Invoice REST API
│   │   └── repository/
│   │       ├── BanRepository.java            # Table JPA Repository
│   │       ├── KhachHangRepository.java      # Customer JPA Repository
│   │       ├── HoaDonRepository.java         # Invoice JPA Repository
│   │       ├── DonDatBanRepository.java      # Reservation JPA Repository
│   │       ├── NhanVienRepository.java       # Employee JPA Repository
│   │       ├── MonAnRepository.java          # Food Item JPA Repository
│   │       └── TaiKhoanRepository.java       # Account JPA Repository
│   ├── entity/                               # All 13 entities with Lombok
│   │   ├── Ban.java
│   │   ├── ChiTietHoaDon.java
│   │   ├── Coc.java
│   │   ├── DonDatBan.java
│   │   ├── DonLapDoiHuyBan.java
│   │   ├── HangKhachHang.java
│   │   ├── HoaDon.java
│   │   ├── KhachHang.java
│   │   ├── KhuyenMai.java
│   │   ├── LoaiBan.java
│   │   ├── MonAn.java
│   │   ├── NhanVien.java
│   │   └── TaiKhoan.java
│   ├── util/
│   │   └── RestClient.java                   # Frontend REST client
│   └── config/
│       └── RestaurantApplication.java        # Original config (legacy)
└── src/main/resources/
    └── application.properties                 # Database & server config
```

## Getting Started

### Step 1: Update Database Connection
Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:sqlserver://YOUR_SERVER:1433;databaseName=Restaurant
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
```

### Step 2: Build the Project
```bash
mvn clean install
```

### Step 3: Run the Backend
```bash
mvn spring-boot:run
```

The server will start at: `http://localhost:8080/api`

### Step 4: Update Frontend Controllers
Replace DAO calls with REST client calls in your JavaFX controllers:

**Before (Old DAO approach):**
```java
Ban_DAO dao = new Ban_DAO();
List<Ban> bans = dao.findAll();
```

**After (REST API approach):**
```java
List<Ban> bans = RestClient.getAll("/ban", Ban.class);
```

## Available REST Endpoints

### Customers
```
GET    /api/khach-hang              # Get all customers
GET    /api/khach-hang/{id}         # Get customer by ID
POST   /api/khach-hang              # Create new customer
PUT    /api/khach-hang/{id}         # Update customer
DELETE /api/khach-hang/{id}         # Delete customer
```

### Invoices
```
GET    /api/hoa-don                 # Get all invoices
GET    /api/hoa-don/{id}            # Get invoice by ID
POST   /api/hoa-don                 # Create new invoice
PUT    /api/hoa-don/{id}            # Update invoice
DELETE /api/hoa-don/{id}            # Delete invoice
```

### Food Items
```
GET    /api/mon-an                  # Get all food items
GET    /api/mon-an/{id}             # Get food item by ID
POST   /api/mon-an                  # Create new food item
PUT    /api/mon-an/{id}             # Update food item
DELETE /api/mon-an/{id}             # Delete food item
```

### Tables
```
GET    /api/ban                     # Get all tables
GET    /api/ban/{id}                # Get table by ID
POST   /api/ban                     # Create new table
PUT    /api/ban/{id}                # Update table
DELETE /api/ban/{id}                # Delete table
```

### More endpoints (follow same pattern):
- `/api/don-dat-ban` - Table reservations
- `/api/nhan-vien` - Employees
- `/api/tai-khoan` - User accounts
- `/api/khuy-en-mai` - Promotions
- `/api/chi-tiet-hoa-don` - Invoice details
- `/api/loai-ban` - Table types
- `/api/coc` - Deposits
- `/api/hang-khach-hang` - Customer ranks

## Using the REST Client in JavaFX

### Example: Loading customers
```java
import util.RestClient;
import entity.KhachHang;

try {
    List<KhachHang> customers = RestClient.getAll("/khach-hang", KhachHang.class);
    // Update UI with customers
    for (KhachHang customer : customers) {
        listView.getItems().add(customer.getTenKH());
    }
} catch (IOException e) {
    AlertUtil.showError("Error", "Failed to load customers: " + e.getMessage());
}
```

### Example: Creating a new customer
```java
KhachHang newCustomer = new KhachHang();
newCustomer.setMaKH("KH123");
newCustomer.setTenKH("Nguyễn Văn A");
newCustomer.setSdt("0912345678");

try {
    KhachHang created = RestClient.create("/khach-hang", newCustomer, KhachHang.class);
    AlertUtil.showInfo("Success", "Customer created: " + created.getTenKH());
} catch (IOException e) {
    AlertUtil.showError("Error", "Failed to create customer: " + e.getMessage());
}
```

### Example: Updating a customer
```java
KhachHang customer = ... // Get from UI
customer.setTenKH("Updated Name");

try {
    KhachHang updated = RestClient.update("/khach-hang", customer.getMaKH(), customer, KhachHang.class);
    AlertUtil.showInfo("Success", "Customer updated");
} catch (IOException e) {
    AlertUtil.showError("Error", "Failed to update customer: " + e.getMessage());
}
```

### Example: Deleting a customer
```java
String customerId = "KH123";

try {
    RestClient.delete("/khach-hang", customerId);
    AlertUtil.showInfo("Success", "Customer deleted");
} catch (IOException e) {
    AlertUtil.showError("Error", "Failed to delete customer: " + e.getMessage());
}
```

## Entity Modifications with Lombok

All entities now use Lombok annotations which automatically generate:
- ✅ Getters for all fields
- ✅ Setters for all fields  
- ✅ Constructors (no-arg and all-arg)
- ✅ equals() and hashCode() methods
- ✅ toString() method

**Example - KhachHang.java:**
```java
@Entity
@Getter              // Auto-generates all getters
@Setter              // Auto-generates all setters
@NoArgsConstructor   // Generates empty constructor
@AllArgsConstructor  // Generates constructor with all fields
@EqualsAndHashCode   // Generates equals() and hashCode()
@ToString            // Generates toString()
public class KhachHang {
    @Id
    private String maKH;
    private String tenKH;
    private String sdt;
    // ... no need to write boilerplate code!
}
```

## Required IDE Configuration

### For IntelliJ IDEA:
1. Go to Settings > Build > Compiler > Annotation Processors
2. Enable annotation processing
3. Restart IDE

### For Eclipse:
1. Install Lombok from https://projectlombok.org/setup/eclipse
2. Run installer

### For VS Code:
1. Install "Lombok Annotations Support for VS Code" extension

## What's Left to Do

### 1. Create Remaining Controllers (Optional but recommended)
Add REST controllers for all entities following the pattern of `KhachHangController` and `HoaDonController`.

### 2. Refactor Frontend JavaFX Controllers
Update each controller to use `RestClient` instead of DAOs:
- Replace all DAO instantiations with REST calls
- Add error handling and loading states
- Update UI refresh logic

### 3. Add Service Layer (Optional)
Create service classes in `org/restaurant/service/` for business logic:
```java
@Service
public class KhachHangService {
    @Autowired
    private KhachHangRepository repository;
    
    public List<KhachHang> getAllActive() {
        // Custom business logic
        return repository.findAll();
    }
}
```

### 4. Security & Authentication
Add Spring Security for login functionality:
```properties
spring.security.user.name=admin
spring.security.user.password=admin
```

### 5. Input Validation
Add validation annotations to entities:
```java
@NotBlank(message = "Name is required")
@Size(min = 1, max = 100)
private String tenKH;
```

## Common Errors & Solutions

### Error: "Connection refused to localhost:8080"
- Ensure backend is running: `mvn spring-boot:run`
- Check firewall settings
- Verify port 8080 is not blocked

### Error: "Cannot connect to database"
- Check database server is running
- Verify connection string in application.properties
- Test SQL Server connection directly

### Error: "Lombok annotations not generating code"
- Enable annotation processing in IDE
- Rebuild project
- Restart IDE

### Error: "Entity not found: 'Ban'"
- Ensure entity classes are in `entity` package
- Verify @Entity annotation is present
- Check package is included in component scan

## Database Setup

If you don't have the Restaurant database yet, create it:

```sql
CREATE DATABASE Restaurant;
USE Restaurant;

-- Your existing schema creation scripts
-- (Run your InsertSQL.sql and QLNH.sql files)
```

## Support Files

- 📄 **CLIENT_SERVER_CONVERSION.md** - Detailed conversion documentation
- 📄 **pom.xml** - Maven configuration with all dependencies
- 📄 **application.properties** - Server and database configuration

## Next Steps

1. **Configure your database connection**
2. **Start the backend server**
3. **Test the API using Postman or curl**
4. **Update one JavaFX controller to use RestClient**
5. **Test end-to-end communication**
6. **Gradually refactor remaining controllers**

## Example API Test (Using curl)

```bash
# Get all customers
curl http://localhost:8080/api/khach-hang

# Create a new customer
curl -X POST http://localhost:8080/api/khach-hang \
  -H "Content-Type: application/json" \
  -d '{"maKH":"KH001","tenKH":"Test Customer","sdt":"0912345678"}'

# Get specific customer
curl http://localhost:8080/api/khach-hang/KH001
```

## Architecture Benefits

🎯 **Scalability** - Deploy backend on server, multiple clients can connect
🔒 **Security** - Can add authentication/authorization layer
📱 **Multi-Client** - Desktop, web, mobile clients can use same API
🔄 **Maintainability** - Clean separation of concerns
⚡ **Performance** - Database queries optimized at backend
🔧 **Flexibility** - Easy to add new features without touching frontend

---

**Questions?** Refer to CLIENT_SERVER_CONVERSION.md for detailed information.
