# OrderStatus Enum Implementation

## Overview
Created an `OrderStatus` Enum to replace string values for order status throughout the application. This provides type safety, prevents invalid status values, and makes the code more maintainable.

## OrderStatus Enum

```java
package com.order_service.enums;

public enum OrderStatus {
    PENDING("Pending"),
    CONFIRMED("Confirmed"),
    SHIPPED("Shipped"),
    DELIVERED("Delivered"),
    CANCELLED("Cancelled"),
    RETURNED("Returned"),
    FAILED("Failed");

    private final String displayName;

    OrderStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
```

### Available Status Values:
- **PENDING**: Initial status when order is placed
- **CONFIRMED**: Order has been confirmed
- **SHIPPED**: Order has been shipped
- **DELIVERED**: Order has been delivered
- **CANCELLED**: Order has been cancelled
- **RETURNED**: Order has been returned
- **FAILED**: Order processing failed

## Files Updated

### 1. Order Entity (Entity Layer)
**Before:**
```java
@NotBlank(message = "Status is required")
private String status;
```

**After:**
```java
@NotNull(message = "Status is required")
@Enumerated(EnumType.STRING)
private OrderStatus status;
```

**Changes:**
- Changed from `String` to `OrderStatus` enum
- Changed validation from `@NotBlank` to `@NotNull` (appropriate for enum)
- Added `@Enumerated(EnumType.STRING)` to persist enum as string in database
- Import: `import com.order_service.enums.OrderStatus;`

### 2. OrderDTO (Data Transfer Object)
**Before:**
```java
@NotBlank(message = "Status is required")
private String status;
```

**After:**
```java
@NotNull(message = "Status is required")
private OrderStatus status;
```

**Changes:**
- Changed from `String` to `OrderStatus` enum
- Changed validation from `@NotBlank` to `@NotNull`
- Import: `import com.order_service.enums.OrderStatus;`

### 3. OrderService (Business Logic Layer)
**Before:**
```java
.status("PENDING")
```

**After:**
```java
.status(OrderStatus.PENDING)
```

**Changes:**
- Changed hardcoded string to enum value
- Import: `import com.order_service.enums.OrderStatus;`

## Usage Examples

### Creating an Order with Initial Status
```java
Order order = Order.builder()
        .userId(1L)
        .productId(2L)
        .quantity(5)
        .status(OrderStatus.PENDING)  // Type-safe enum
        .orderDate(LocalDateTime.now())
        .orderNumber("ORD-ABC123")
        .build();
```

### Updating Order Status
```java
orderDTO.setStatus(OrderStatus.SHIPPED);
```

### Checking Order Status
```java
if (order.getStatus() == OrderStatus.DELIVERED) {
    // Handle delivered order
}
```

### Getting Display Name
```java
String displayName = order.getStatus().getDisplayName();  // Returns "Pending", "Shipped", etc.
```

## API Request/Response Examples

### Create Order (Request)
```json
{
  "userId": 1,
  "productId": 2,
  "quantity": 5,
  "totalPrice": 500.00,
  "shippingAddress": "123 Main St",
  "paymentMethod": "CREDIT_CARD",
  "status": "PENDING"
}
```

### Get Order (Response)
```json
{
  "status": 200,
  "message": "Order fetched successfully",
  "data": {
    "id": 1,
    "userId": 1,
    "productId": 2,
    "quantity": 5,
    "totalPrice": 500.00,
    "status": "PENDING",
    "orderNumber": "ORD-ABC123",
    "orderDate": "2026-04-26T22:42:36"
  },
  "timestamp": "2026-04-26T22:42:36"
}
```

### Update Order (Request)
```json
{
  "userId": 1,
  "productId": 2,
  "quantity": 5,
  "totalPrice": 500.00,
  "shippingAddress": "123 Main St",
  "paymentMethod": "CREDIT_CARD",
  "status": "SHIPPED"
}
```

## Benefits of Using Enum

✅ **Type Safety**: Compile-time checking of valid status values  
✅ **No String Errors**: Prevents typos like "PEDING" or "SHIPD"  
✅ **Better IDE Support**: Auto-completion of status values  
✅ **Easier Refactoring**: Rename or add statuses safely  
✅ **Display Names**: Built-in `getDisplayName()` for UI display  
✅ **Database Persistence**: Stored as strings for readability, loaded as enums for type safety  
✅ **Validation at Compile Time**: Invalid statuses caught during compilation  

## Database Schema

The status is stored in the database as a VARCHAR string:

```sql
ALTER TABLE orders MODIFY status VARCHAR(20);
```

Valid values in database:
- PENDING
- CONFIRMED
- SHIPPED
- DELIVERED
- CANCELLED
- RETURNED
- FAILED

## Migration for Existing Data

If you have existing order data with string status values:

```sql
UPDATE orders SET status = 'PENDING' WHERE status = 'pending';
UPDATE orders SET status = 'SHIPPED' WHERE status = 'shipped';
-- etc.
```

Or use a more flexible approach:

```sql
UPDATE orders SET status = UPPER(status);
```

## Error Handling

### Invalid Status Provided
When an invalid status is provided, Spring will automatically return a validation error:

```json
{
  "status": 400,
  "message": "Validation failed",
  "errors": {
    "status": "Invalid status value. Valid values are: PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED, RETURNED, FAILED"
  }
}
```

## Adding New Status Values

To add a new status:

1. Add to enum:
```java
public enum OrderStatus {
    PENDING("Pending"),
    ON_HOLD("On Hold"),  // NEW
    // ... other statuses
}
```

2. Update any business logic that handles status transitions
3. Rebuild and deploy

## Extending OrderStatus

You can add more methods to the enum if needed:

```java
public enum OrderStatus {
    PENDING("Pending", true),
    SHIPPED("Shipped", false),
    // ...

    private final String displayName;
    private final boolean isEditable;

    OrderStatus(String displayName, boolean isEditable) {
        this.displayName = displayName;
        this.isEditable = isEditable;
    }

    public boolean isEditable() {
        return isEditable;
    }
}
```

## Build Status
✅ **Compilation**: SUCCESS (13 source files)  
✅ **JAR Build**: SUCCESS  
✅ **Ready for Deployment**: YES

The OrderStatus enum is now fully integrated throughout the order-service and provides type-safe status management.
