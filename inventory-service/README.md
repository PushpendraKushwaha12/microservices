# Inventory Service - Production-Level Implementation

## Overview
The Inventory Service is a comprehensive microservice for managing warehouse inventory, stock movements, and warehouse operations. Built with production-level architecture following the same patterns as Order and Product services.

## Architecture Overview

```
inventory-service/
├── src/main/java/com/inventory_service/
│   ├── config/
│   │   └── MapperConfig.java                    # ModelMapper configuration
│   ├── controller/
│   │   ├── InventoryItemController.java         # Inventory item management
│   │   ├── StockMovementController.java         # Stock movement tracking
│   │   └── WarehouseController.java             # Warehouse management
│   ├── dto/
│   │   ├── InventoryItemDto.java                # Inventory item DTO
│   │   ├── StockMovementDto.java                # Stock movement DTO
│   │   └── WarehouseDto.java                    # Warehouse DTO
│   ├── entity/
│   │   ├── InventoryItem.java                   # Inventory item entity (40+ fields)
│   │   ├── StockMovement.java                   # Stock movement entity (25+ fields)
│   │   └── Warehouse.java                       # Warehouse entity (25+ fields)
│   ├── enums/
│   │   ├── InventoryStatus.java                 # Item status enum
│   │   └── MovementType.java                    # Movement type enum
│   ├── exception/
│   │   ├── GlobalExceptionHandler.java          # Centralized exception handling
│   │   └── InventoryException.java              # Custom exceptions
│   ├── payload/
│   │   ├── request/
│   │   │   └── APIRequest.java                  # Generic request wrapper
│   │   └── response/
│   │       └── APIResponse.java                 # Unified response format
│   ├── repository/
│   │   ├── InventoryItemRepository.java         # Inventory item queries
│   │   ├── StockMovementRepository.java         # Stock movement queries
│   │   └── WarehouseRepository.java             # Warehouse queries
│   ├── service/
│   │   ├── InventoryItemService.java            # Inventory business logic
│   │   ├── StockMovementService.java            # Movement business logic
│   │   ├── WarehouseService.java                # Warehouse business logic
│   │   └── impl/
│   │       ├── InventoryItemServiceImpl.java    # Service interfaces
│   │       ├── StockMovementServiceImpl.java
│   │       └── WarehouseServiceImpl.java
│   └── InventoryServiceApplication.java         # Spring Boot entry point
```

## Core Entities

### 1. InventoryItem (40+ Fields)
**Business Purpose**: Track individual inventory items with comprehensive details

**Key Fields**:
- **Basic Info**: id, productId, productSku, productName
- **Location**: warehouseId, warehouseName, locationCode, zone, aisle, shelf, bin
- **Quantities**: quantity, reservedQuantity, allocatedQuantity, availableQuantity
- **Financial**: unitCost, totalValue
- **Quality**: status, condition, qualityNotes, expiryDate
- **Logistics**: weight, dimensions, storageRequirements, handlingInstructions
- **Tracking**: batchNumber, serialNumber, lotNumber, barcode, rfidTag
- **Supplier**: supplierName, supplierBatch, supplierCode
- **Audit**: createdDate, lastUpdatedDate, createdBy, updatedBy

### 2. StockMovement (25+ Fields)
**Business Purpose**: Track all inventory movements (inbound, outbound, adjustments)

**Key Fields**:
- **Movement Info**: movementType, quantity, movementDate
- **Financial**: unitCost, totalValue
- **References**: referenceNumber, orderNumber, invoiceNumber
- **Parties**: supplierName, customerName, performedBy
- **Location**: fromLocation, toLocation
- **Quality**: qualityStatus, approvalStatus
- **Audit**: createdDate, createdBy, transactionId

### 3. Warehouse (25+ Fields)
**Business Purpose**: Manage warehouse locations and their capacities

**Key Fields**:
- **Basic Info**: warehouseCode, warehouseName, description
- **Address**: addressLine1, addressLine2, city, stateProvince, postalCode, country
- **Contact**: phoneNumber, email, managerName, managerPhone
- **Capacity**: totalCapacitySqFt, usedCapacitySqFt, totalRacks, totalShelves
- **Environment**: warehouseType, operatingHours, securityLevel, climateControl
- **Location**: latitude, longitude, timezone, region, zone

## Enums

### InventoryStatus
```java
public enum InventoryStatus {
    AVAILABLE("Available"),
    RESERVED("Reserved"),
    ALLOCATED("Allocated"),
    DAMAGED("Damaged"),
    EXPIRED("Expired"),
    QUARANTINE("Quarantine"),
    OUT_OF_STOCK("Out of Stock");
}
```

### MovementType
```java
public enum MovementType {
    INBOUND("Inbound"),
    OUTBOUND("Outbound"),
    ADJUSTMENT("Adjustment"),
    RETURN("Return"),
    TRANSFER("Transfer");
}
```

## API Endpoints

### Inventory Item Management
```
POST   /api/v1/inventory/items                    # Create inventory item
GET    /api/v1/inventory/items                    # Get all items
GET    /api/v1/inventory/items/{id}               # Get item by ID
PUT    /api/v1/inventory/items/{id}               # Update item
DELETE /api/v1/inventory/items/{id}               # Delete item
GET    /api/v1/inventory/items/low-stock          # Get low stock items
GET    /api/v1/inventory/items/out-of-stock       # Get out of stock items
GET    /api/v1/inventory/items/search?keyword=   # Search items
GET    /api/v1/inventory/items/product/{id}/total-quantity    # Get total quantity
GET    /api/v1/inventory/items/product/{id}/available-quantity # Get available quantity
```

### Stock Movement Management
```
POST   /api/v1/inventory/movements                 # Create stock movement
GET    /api/v1/inventory/movements                 # Get all movements
GET    /api/v1/inventory/movements/{id}            # Get movement by ID
GET    /api/v1/inventory/movements/item/{id}       # Get movements by item
GET    /api/v1/inventory/movements/date-range      # Get movements by date range
GET    /api/v1/inventory/movements/pending-approvals # Get pending approvals
PUT    /api/v1/inventory/movements/{id}/approve    # Approve movement
PUT    /api/v1/inventory/movements/{id}/reverse    # Reverse movement
```

### Warehouse Management
```
POST   /api/v1/inventory/warehouses                # Create warehouse
GET    /api/v1/inventory/warehouses                # Get all warehouses
GET    /api/v1/inventory/warehouses/{id}           # Get warehouse by ID
GET    /api/v1/inventory/warehouses/code/{code}    # Get warehouse by code
PUT    /api/v1/inventory/warehouses/{id}           # Update warehouse
DELETE /api/v1/inventory/warehouses/{id}           # Delete warehouse
GET    /api/v1/inventory/warehouses/active         # Get active warehouses
GET    /api/v1/inventory/warehouses/search?keyword= # Search warehouses
GET    /api/v1/inventory/warehouses/count/active   # Get active warehouse count
```

## Business Logic Features

### Inventory Management
- **Stock Level Monitoring**: Automatic low stock alerts
- **Location Tracking**: Precise item location (zone, aisle, shelf, bin)
- **Batch & Serial Tracking**: Full traceability
- **Quality Control**: Status tracking and quarantine management
- **Expiry Management**: Automatic expiry date monitoring
- **Cost Tracking**: Unit cost and total value calculations

### Stock Movement Tracking
- **Movement Types**: Inbound, outbound, adjustments, returns, transfers
- **Approval Workflow**: Movement approval system
- **Audit Trail**: Complete movement history
- **Reversal Support**: Ability to reverse incorrect movements
- **Reference Tracking**: Link to orders, invoices, suppliers

### Warehouse Operations
- **Capacity Management**: Track used vs total capacity
- **Multi-Warehouse Support**: Manage multiple warehouse locations
- **Geographic Distribution**: Location-based warehouse management
- **Environmental Controls**: Climate and security level tracking

## Data Validation

### Inventory Item Validation
```java
@NotNull(message = "Product ID is required")
private Long productId;

@NotBlank(message = "Product SKU is required")
@Size(max = 50, message = "Product SKU cannot exceed 50 characters")
private String productSku;

@Min(value = 0, message = "Quantity cannot be negative")
private int quantity;

@DecimalMin(value = "0.0", message = "Unit cost cannot be negative")
private BigDecimal unitCost;
```

### Stock Movement Validation
```java
@NotNull(message = "Movement type is required")
private MovementType movementType;

@NotNull(message = "Quantity is required")
private int quantity;

@NotNull(message = "Movement date is required")
private LocalDateTime movementDate;
```

### Warehouse Validation
```java
@NotBlank(message = "Warehouse code is required")
@Size(min = 2, max = 20, message = "Warehouse code must be between 2 and 20 characters")
@Column(unique = true)
private String warehouseCode;

@NotBlank(message = "Warehouse name is required")
@Size(min = 2, max = 100, message = "Warehouse name must be between 2 and 100 characters")
private String warehouseName;
```

## Repository Features

### InventoryItemRepository
- Find by product, warehouse, location
- Low stock and out of stock queries
- Search functionality
- Aggregate quantity calculations

### StockMovementRepository
- Movement history queries
- Date range filtering
- Approval workflow queries
- Transaction tracking

### WarehouseRepository
- Geographic queries
- Capacity reporting
- Active warehouse filtering
- Search functionality

## Error Handling

### Custom Exceptions
```java
public class InventoryException extends RuntimeException {
    private int statusCode;
    // Constructor and getters
}
```

### Global Exception Handler
- Validation errors with field-level details
- Business logic exceptions
- Generic error handling
- Consistent error response format

## Response Format

All APIs return standardized responses:

```json
{
  "status": 200,
  "message": "Inventory items fetched successfully",
  "data": [
    {
      "id": 1,
      "productName": "Wireless Headphones",
      "productSku": "WH-001",
      "quantity": 150,
      "availableQuantity": 140,
      "status": "AVAILABLE",
      "warehouseName": "Main Warehouse",
      "locationCode": "A-01-05-12"
    }
  ],
  "timestamp": "2026-04-26T23:00:00"
}
```

## Production Features

✅ **Comprehensive Field Coverage** - 40+ fields per entity for real-world scenarios  
✅ **Type Safety** - Enum-based status and movement types  
✅ **Audit Trail** - Complete tracking of changes and movements  
✅ **Validation** - Multi-layer validation with meaningful messages  
✅ **Search & Filtering** - Advanced querying capabilities  
✅ **Business Logic** - Real-world inventory management rules  
✅ **Scalability** - Repository patterns for efficient data access  
✅ **Error Handling** - Centralized exception management  
✅ **API Design** - RESTful endpoints with consistent responses  

## Database Schema

### Inventory Items Table
```sql
CREATE TABLE inventory_items (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_id BIGINT NOT NULL,
    product_sku VARCHAR(50) NOT NULL,
    product_name VARCHAR(255) NOT NULL,
    warehouse_id BIGINT NOT NULL,
    location_code VARCHAR(50) NOT NULL,
    batch_number VARCHAR(50) NOT NULL,
    quantity INT NOT NULL DEFAULT 0,
    available_quantity INT NOT NULL DEFAULT 0,
    unit_cost DECIMAL(10,2),
    total_value DECIMAL(15,2),
    status VARCHAR(20) NOT NULL,
    expiry_date DATETIME,
    created_date DATETIME NOT NULL,
    last_updated_date DATETIME NOT NULL,
    -- ... 30+ additional fields
);
```

### Stock Movements Table
```sql
CREATE TABLE stock_movements (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    inventory_item_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    movement_type VARCHAR(20) NOT NULL,
    quantity INT NOT NULL,
    movement_date DATETIME NOT NULL,
    reference_number VARCHAR(100),
    performed_by VARCHAR(100),
    created_date DATETIME NOT NULL,
    -- ... 20+ additional fields
);
```

### Warehouses Table
```sql
CREATE TABLE warehouses (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    warehouse_code VARCHAR(20) UNIQUE NOT NULL,
    warehouse_name VARCHAR(100) NOT NULL,
    address_line1 VARCHAR(255) NOT NULL,
    city VARCHAR(100) NOT NULL,
    country VARCHAR(100) NOT NULL,
    total_capacity_sq_ft DOUBLE,
    is_active BOOLEAN DEFAULT TRUE,
    created_date DATETIME NOT NULL,
    -- ... 20+ additional fields
);
```

## Build & Deployment

### Dependencies
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
<dependency>
    <groupId>org.modelmapper</groupId>
    <artifactId>modelmapper</artifactId>
    <version>3.1.1</version>
</dependency>
```

### Build Command
```bash
mvn clean package -DskipTests
```

### Run Command
```bash
java -jar target/inventory-service-0.0.1-SNAPSHOT.jar
```

## Integration Points

### With Product Service
- Product ID and SKU synchronization
- Product name updates
- Category and brand information

### With Order Service
- Stock quantity updates on order placement
- Stock movement creation for order fulfillment
- Inventory reservation for pending orders

### With External Systems
- ERP system integration
- Barcode/RFID system integration
- Supplier management system
- Transportation management system

## Monitoring & Analytics

### Key Metrics
- Stock levels by product/warehouse
- Movement volume and trends
- Low stock alerts
- Inventory turnover rates
- Carrying costs
- Stock accuracy rates

### Reporting Capabilities
- Inventory valuation reports
- Stock movement history
- Warehouse utilization reports
- Supplier performance reports
- ABC analysis for inventory classification

## Security Considerations

- Role-based access control for inventory operations
- Audit logging for all inventory changes
- Secure API authentication
- Data encryption for sensitive information
- Compliance with inventory management regulations

## Future Enhancements

- **Real-time Inventory Tracking**: WebSocket integration for live updates
- **AI-Powered Forecasting**: Demand prediction and automatic reordering
- **IoT Integration**: Sensor-based inventory monitoring
- **Blockchain Tracking**: Immutable audit trail for high-value items
- **Mobile App**: Field inventory management for warehouse staff

---

## Summary

The Inventory Service provides enterprise-grade inventory management capabilities with:

- **40+ fields** per inventory item for comprehensive tracking
- **25+ fields** per stock movement for complete audit trail
- **25+ fields** per warehouse for detailed facility management
- **Type-safe enums** for status and movement types
- **Advanced querying** and search capabilities
- **Business logic validation** and error handling
- **Production-ready architecture** following microservice best practices

The service is designed to handle real-world inventory management scenarios in a large-scale e-commerce or manufacturing environment, with full traceability, compliance capabilities, and scalability for enterprise deployments.
