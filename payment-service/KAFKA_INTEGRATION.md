# Payment Service Integration with Kafka

## Overview
The Payment Service is now fully integrated as a company-level microservice that automatically processes payments when orders are confirmed through Kafka event-driven architecture.

## Architecture

### Event Flow
```
1. Order Service: User places an order
   └─> Sends "ORDER_PLACED" event to Kafka topic "order-events"

2. Payment Service: Consumes "ORDER_PLACED" event
   └─> Creates Payment record in database
   └─> Processes payment transaction
   └─> Sends "PAYMENT_COMPLETED" or "PAYMENT_FAILED" event to "payment-events" topic

3. Order Service (Optional): Can consume payment events to update order status
```

### Kafka Configuration

**Bootstrap Server**: `localhost:9092`

**Topics**:
- **order-events**: Used by Order Service to publish order events
  - Consumer Group: `payment-service-group`
  
- **payment-events**: Used by Payment Service to publish payment status events

**Consumer Properties**:
- Group ID: `payment-service-group`
- Auto Offset Reset: `earliest`
- Value Deserializer: `JsonDeserializer`
- Trusted Packages: `*`

## Payment Service Endpoints

### Get Payment by ID
```
GET /api/v1/payments/{id}
```

### Get Payment by Order ID
```
GET /api/v1/payments/order/{orderId}
```

### Get Payments by User ID
```
GET /api/v1/payments/user/{userId}
```

### Get Payments by Status
```
GET /api/v1/payments/status/{status}
```

### Update Payment Status
```
PUT /api/v1/payments/{id}/status?status={PENDING|PROCESSING|COMPLETED|FAILED|CANCELLED|REFUNDED}
```

### Delete Payment
```
DELETE /api/v1/payments/{id}
```

## Data Structures

### Payment Entity
```java
- id: Long (Primary Key)
- orderId: Long (Foreign reference to Order)
- userId: Long (Reference to User)
- amount: BigDecimal (Total payment amount)
- paymentMethod: String (e.g., "CREDIT_CARD", "DEBIT_CARD", etc.)
- status: PaymentStatus (PENDING, PROCESSING, COMPLETED, FAILED, CANCELLED, REFUNDED)
- paymentDate: LocalDateTime
- transactionId: String (Unique transaction identifier)
- description: String
- notes: String
- tax: BigDecimal
- discount: BigDecimal
- createdAt: LocalDateTime
- updatedAt: LocalDateTime
```

### Payment Status Enum
- **PENDING**: Payment is awaiting processing
- **PROCESSING**: Payment is being processed
- **COMPLETED**: Payment has been successfully completed
- **FAILED**: Payment has failed
- **CANCELLED**: Payment has been cancelled
- **REFUNDED**: Payment has been refunded

### OrderEvent (Consumed from Kafka)
```java
- orderId: Long
- userId: Long
- productId: Long
- quantity: int
- shippingAddress: String
- paymentMethod: String
- totalPrice: BigDecimal
- discount: double
- tax: double
- notes: String
- orderDate: LocalDateTime
- status: OrderStatus
- orderNumber: String
- eventType: String (e.g., "ORDER_PLACED", "ORDER_CONFIRMED")
```

### PaymentEvent (Published to Kafka)
```java
- paymentId: Long
- orderId: Long
- userId: Long
- amount: BigDecimal
- paymentMethod: String
- status: PaymentStatus
- paymentDate: LocalDateTime
- transactionId: String
- eventType: String (e.g., "PAYMENT_COMPLETED", "PAYMENT_FAILED")
```

## Service Configuration

### Application Properties
- **Server Port**: 8084
- **Database**: Shared microservices_db (MySQL)
- **Hibernate DDL**: update (auto-creates/updates tables)

## Key Features

1. **Automatic Payment Processing**: When an order is placed, the payment service automatically consumes the event and creates a payment record
2. **Event-Driven Architecture**: Uses Kafka for asynchronous communication between services
3. **Transaction ID Generation**: Each payment gets a unique transaction ID (TXN-XXXXXXXX)
4. **Status Tracking**: Payment status is tracked through different states (PENDING → PROCESSING → COMPLETED)
5. **Error Handling**: Global exception handler for graceful error responses
6. **REST API**: Full REST interface to query and manage payments

## Database Schema

```sql
CREATE TABLE payments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    amount DECIMAL(19, 2) NOT NULL,
    payment_method VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL,
    payment_date DATETIME,
    transaction_id VARCHAR(255),
    description VARCHAR(255),
    notes VARCHAR(255),
    tax DECIMAL(19, 2),
    discount DECIMAL(19, 2),
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL
);
```

## Integration Steps Completed

1. ✅ Added Spring Kafka dependency to payment-service pom.xml
2. ✅ Updated application.yaml with Kafka consumer configuration
3. ✅ Created Payment entity and database schema
4. ✅ Created PaymentDto, OrderEvent, and PaymentEvent DTOs
5. ✅ Implemented PaymentRepository with custom queries
6. ✅ Implemented PaymentService with business logic
7. ✅ Created OrderEventConsumer Kafka consumer
8. ✅ Configured Kafka consumer beans (KafkaConfig)
9. ✅ Created PaymentController REST API
10. ✅ Implemented exception handling
11. ✅ Updated OrderService to send CONFIRMED events

## Running the Services

### Prerequisites
- Docker with Docker Compose
- MySQL container running
- Kafka container running (localhost:9092)

### Start Services
```bash
# Build all services
mvn clean package

# Start with Docker Compose
docker-compose up -d

# Or run individually
java -jar payment-service/target/payment-service.jar
java -jar order-service/target/order-service.jar
```

## Example Workflow

1. **Create Order** (Order Service)
   ```
   POST /api/v1/orders
   ```
   - Creates order with status: PENDING
   - Publishes "ORDER_PLACED" event

2. **Payment Service Consumes Event**
   - Receives "ORDER_PLACED" event
   - Creates Payment record with status: PENDING
   - Simulates payment processing
   - Updates Payment status to COMPLETED
   - Publishes "PAYMENT_COMPLETED" event

3. **Query Payment** (Payment Service)
   ```
   GET /api/v1/payments/order/{orderId}
   ```
   - Returns payment details with status COMPLETED

## Monitoring & Logging

The payment service includes comprehensive logging:
- `OrderEventConsumer`: Logs received events and processing status
- `PaymentServiceImpl`: Logs payment creation, processing, and events
- `GlobalExceptionHandler`: Logs all errors with stack traces

Check logs using:
```bash
docker logs -f payment-service
```

## Future Enhancements

1. Integrate with actual payment gateway (Stripe, PayPal)
2. Add retry mechanism for failed payments
3. Implement payment reconciliation
4. Add audit trail for payment transactions
5. Implement payment webhook notifications
6. Add transaction history and reporting
7. Implement refund processing workflow

