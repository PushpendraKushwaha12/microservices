# Order Service - Production-Level Code Architecture

## Overview
The Order Service has been completely refactored to follow production-level best practices with clean architecture, proper separation of concerns, centralized exception handling, and comprehensive validation.

## Project Structure

```
order-service/
├── src/main/java/com/order_service/
│   ├── config/
│   │   └── MapperConfig.java          # ModelMapper configuration
│   ├── controller/
│   │   └── OrderController.java       # REST endpoints (clean & minimal)
│   ├── dto/
│   │   └── Orderdto.java              # Data Transfer Object with validation
│   ├── entity/
│   │   └── Order.java                 # JPA Entity with 12+ fields
│   ├── exception/
│   │   ├── GlobalExceptionHandler.java # Centralized exception handling
│   │   └── OrderException.java        # Custom exception class
│   ├── payload/
│   │   ├── request/
│   │   │   └── APIRequest.java        # Generic request wrapper
│   │   └── response/
│   │       └── APIResponse.java       # Unified response format
│   ├── repository/
│   │   └── OrderRepository.java       # JPA Repository
│   ├── service/
│   │   ├── OrderService.java          # Business logic implementation
│   │   └── impl/
│   │       └── OrderServiceImpl.java   # Service interface
│   └── OrderServiceApplication.java   # Spring Boot entry point
└── pom.xml
```

## Key Features

### 1. **Clean Controller**
- Minimal logic, only request routing
- Uses `@Valid` for automatic validation
- Centralized exception handling (no try-catch blocks)
- Consistent response format via `APIResponse`
- Logging for monitoring

```java
@PostMapping
public ResponseEntity<APIResponse<Orderdto>> createOrder(@Valid @RequestBody Orderdto orderDto) {
    log.info("Creating new order");
    Orderdto createdOrder = orderService.placeOrder(orderDto);
    return ResponseEntity.status(HttpStatus.CREATED)
            .body(new APIResponse<>(HttpStatus.CREATED.value(), "Order created successfully", createdOrder));
}
```

### 2. **Unified Response Format**
All API responses follow a consistent structure:

```json
{
  "status": 201,
  "message": "Order created successfully",
  "data": {
    "id": 1,
    "userId": 123,
    "productId": 456,
    ...
  },
  "errors": null,
  "timestamp": "2026-04-26T22:30:00"
}
```

### 3. **Global Exception Handling**
- No try-catch blocks in controller/service
- Centralized `GlobalExceptionHandler` handles all exceptions
- Custom `OrderException` for business logic errors
- Automatic validation error formatting

```java
@ExceptionHandler(OrderException.class)
public ResponseEntity<APIResponse<Object>> handleOrderException(OrderException ex) {
    APIResponse<Object> response = APIResponse.builder()
            .status(ex.getStatusCode())
            .message(ex.getMessage())
            .build();
    return ResponseEntity.status(ex.getStatusCode()).body(response);
}
```

### 4. **DTO-Based Validation**
All validation happens through Jakarta Bean Validation annotations:

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Orderdto {
    @NotNull(message = "User ID is required")
    private Long userId;
    
    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;
    
    @DecimalMin(value = "0.01", message = "Total price must be greater than 0")
    private double totalPrice;
    // More fields...
}
```

### 5. **Entity Fields (12+)**
- `id`: Unique identifier
- `userId`: Reference to user
- `productId`: Reference to product
- `quantity`: Order quantity
- `orderDate`: When order was placed
- `status`: Current status (PENDING, SHIPPED, etc.)
- `totalPrice`: Total cost
- `shippingAddress`: Delivery address
- `paymentMethod`: Payment type
- `orderNumber`: Unique order reference
- `discount`: Discount applied
- `tax`: Tax amount
- `notes`: Additional notes

### 6. **Builder Pattern with Lombok**
- Clean object construction
- Type-safe parameters
- Eliminates setters and getters clutter

```java
Order order = Order.builder()
        .userId(orderDto.getUserId())
        .productId(orderDto.getProductId())
        .quantity(orderDto.getQuantity())
        .orderDate(LocalDateTime.now())
        .status("PENDING")
        .orderNumber(generateOrderNumber())
        .build();
```

### 7. **ModelMapper for DTO Conversion**
Automatically maps between Entity and DTO, eliminating boilerplate:

```java
Order savedOrder = orderRepository.save(order);
return modelMapper.map(savedOrder, Orderdto.class);
```

### 8. **Clean Service Layer**
- No unnecessary null checks (rely on validation)
- Business logic centralized
- Throws custom exceptions for error handling
- Uses builder pattern for object creation

```java
@Override
public Orderdto placeOrder(Orderdto orderDto) {
    // Validation handled by @Valid in controller
    Order order = Order.builder()
            .userId(orderDto.getUserId())
            .productId(orderDto.getProductId())
            // ... more fields
            .orderDate(LocalDateTime.now())
            .status("PENDING")
            .orderNumber(generateOrderNumber())
            .build();

    Order savedOrder = orderRepository.save(order);
    return modelMapper.map(savedOrder, Orderdto.class);
}
```

## API Endpoints

### Create Order
```
POST /api/v1/orders
Content-Type: application/json

{
  "userId": 1,
  "productId": 2,
  "quantity": 5,
  "totalPrice": 500.00,
  "shippingAddress": "123 Main St, City, State",
  "paymentMethod": "CREDIT_CARD",
  "discount": 50.00,
  "tax": 45.00,
  "notes": "Fragile items"
}
```

### Retrieve All Orders
```
GET /api/v1/orders
```

### Retrieve Order by ID
```
GET /api/v1/orders/1
```

### Update Order
```
PUT /api/v1/orders/1
Content-Type: application/json
{
  "userId": 1,
  "productId": 2,
  "quantity": 10,
  "status": "SHIPPED",
  ...
}
```

### Delete Order
```
DELETE /api/v1/orders/1
```

## Dependencies

Key dependencies added:
- `org.modelmapper:modelmapper:3.1.1` - DTO to Entity mapping
- `org.projectlombok:lombok:1.18.44` - Annotations for boilerplate reduction
- `spring-boot-starter-validation` - Bean Validation support
- `spring-boot-starter-web` - REST support
- `spring-boot-starter-data-jpa` - Database access
- `mysql-connector-j` - MySQL driver

## Error Handling Examples

### Validation Error
```json
{
  "status": 400,
  "message": "Validation failed",
  "errors": {
    "quantity": "Quantity must be at least 1",
    "totalPrice": "Total price must be greater than 0"
  },
  "timestamp": "2026-04-26T22:30:00"
}
```

### Not Found Error
```json
{
  "status": 404,
  "message": "Order not found with id: 999",
  "timestamp": "2026-04-26T22:30:00"
}
```

### Server Error
```json
{
  "status": 500,
  "message": "An unexpected error occurred",
  "timestamp": "2026-04-26T22:30:00"
}
```

## Best Practices Implemented

✅ **Clean Architecture** - Separation of concerns  
✅ **No Unnecessary Try-Catch** - Global exception handling  
✅ **Validation at Entry Point** - @Valid on DTOs  
✅ **DTO Pattern** - Loose coupling between layers  
✅ **Builder Pattern** - Clean object construction  
✅ **ModelMapper** - Reduce boilerplate  
✅ **Logging** - Monitor application flow  
✅ **Custom Exceptions** - Business logic errors  
✅ **Consistent Response Format** - APIResponse wrapper  
✅ **Production-Ready** - Scalable and maintainable  

## Configuration

### MapperConfig.java
Registers ModelMapper as a Spring bean:

```java
@Configuration
public class MapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
```

## Testing the Service

The service is ready for integration tests and deployment. Build and package with:

```bash
mvn clean package -DskipTests
```

This creates an executable JAR: `target/order-service.jar`

## Summary

The Order Service is now a production-level microservice with:
- 12+ entity fields
- Complete CRUD operations
- Global exception handling
- Comprehensive validation
- Clean, maintainable code
- Best practices throughout
- Ready for deployment
