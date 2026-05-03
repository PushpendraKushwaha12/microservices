# Payment Service - Quick Start Guide

## Services Overview

| Service | Port | Role |
|---------|------|------|
| Order Service | 8080 | Creates and manages orders, publishes order events |
| Payment Service | 8084 | Consumes order events and processes payments |
| Kafka | 9092 | Message broker for event communication |
| MySQL | 3306 | Database for both services |

## How It Works

1. **User places an order** via Order Service API
2. **Order Service publishes** `ORDER_PLACED` event to Kafka
3. **Payment Service listens** for order events on Kafka
4. **Payment Service automatically**:
   - Creates payment record
   - Processes payment transaction
   - Publishes `PAYMENT_COMPLETED` event back to Kafka
5. **Client can query** payment status via Payment Service API

## Quick Start

### Prerequisites
- Docker & Docker Compose installed
- MySQL, Kafka, and Zookeeper running (via docker-compose)
- Java 17 or higher
- Maven 3.6+

### Step 1: Start Infrastructure
```bash
cd C:\microservices\microservices
docker-compose up -d
```

Wait for containers to be healthy:
```bash
docker-compose ps
```

### Step 2: Start Services

**Option A: Using Docker**
```bash
# Build JAR files
mvn clean package -DskipTests

# Build Docker images
docker build -t order-service ./order-service
docker build -t payment-service ./payment-service

# Run services
docker run -p 8080:8080 order-service
docker run -p 8084:8084 payment-service
```

**Option B: Direct Java Execution**
```bash
# Terminal 1 - Order Service
cd order-service
java -jar target/order-service.jar

# Terminal 2 - Payment Service
cd payment-service
java -jar target/payment-service-0.0.1-SNAPSHOT.jar
```

### Step 3: Test the Integration

#### Create an Order
```bash
curl -X POST http://localhost:8080/api/v1/orders \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "productId": 1,
    "quantity": 1,
    "totalPrice": 100.50,
    "paymentMethod": "CREDIT_CARD",
    "shippingAddress": "123 Main Street, City, State 12345",
    "discount": 0.0,
    "tax": 10.5,
    "notes": "Test order"
  }'
```

**Response** (example):
```json
{
  "code": 201,
  "message": "Order created successfully",
  "data": {
    "id": 1,
    "userId": 1,
    "productId": 1,
    "quantity": 1,
    "orderDate": "2026-05-03T15:16:00",
    "status": "PENDING",
    "totalPrice": 100.50,
    "orderNumber": "ORD-ABC12345",
    "discount": 0.0,
    "tax": 10.5,
    "notes": "Test order",
    "shippingAddress": "123 Main Street, City, State 12345",
    "paymentMethod": "CREDIT_CARD"
  }
}
```

#### Get Payment for Order
Wait 2-3 seconds for Kafka processing, then:

```bash
curl http://localhost:8084/api/v1/payments/order/1
```

**Response** (example):
```json
{
  "id": 1,
  "orderId": 1,
  "userId": 1,
  "amount": 100.50,
  "paymentMethod": "CREDIT_CARD",
  "status": "COMPLETED",
  "paymentDate": "2026-05-03T15:16:01",
  "transactionId": "TXN-ABC12345",
  "description": "Payment for order: ORD-ABC12345",
  "notes": null,
  "tax": 10.50,
  "discount": 0.00,
  "createdAt": "2026-05-03T15:16:00.123456",
  "updatedAt": "2026-05-03T15:16:01.654321"
}
```

#### Get All Payments by User
```bash
curl http://localhost:8084/api/v1/payments/user/1
```

#### Get All Completed Payments
```bash
curl http://localhost:8084/api/v1/payments/status/COMPLETED
```

#### Update Payment Status
```bash
curl -X PUT "http://localhost:8084/api/v1/payments/1/status?status=REFUNDED"
```

#### Delete Payment
```bash
curl -X DELETE http://localhost:8084/api/v1/payments/1
```

## Kafka Event Examples

### Event 1: ORDER_PLACED (Published by Order Service)
```json
{
  "orderId": 1,
  "userId": 1,
  "productId": 1,
  "quantity": 1,
  "shippingAddress": "123 Main Street",
  "paymentMethod": "CREDIT_CARD",
  "totalPrice": 100.50,
  "discount": 0.0,
  "tax": 10.5,
  "notes": "Test order",
  "orderDate": "2026-05-03T15:16:00",
  "status": "PENDING",
  "orderNumber": "ORD-ABC12345",
  "eventType": "ORDER_PLACED"
}
```

### Event 2: PAYMENT_COMPLETED (Published by Payment Service)
```json
{
  "paymentId": 1,
  "orderId": 1,
  "userId": 1,
  "amount": 100.50,
  "paymentMethod": "CREDIT_CARD",
  "status": "COMPLETED",
  "paymentDate": "2026-05-03T15:16:01",
  "transactionId": "TXN-ABC12345",
  "eventType": "PAYMENT_COMPLETED"
}
```

## API Endpoints Summary

### Order Service (Port 8080)

```
POST   /api/v1/orders                    - Create order
GET    /api/v1/orders                    - Get all orders (admin only)
GET    /api/v1/orders/{id}               - Get order by ID
PUT    /api/v1/orders/{id}               - Update order
DELETE /api/v1/orders/{id}               - Delete order (admin only)
```

### Payment Service (Port 8084)

```
GET    /api/v1/payments/{id}             - Get payment by ID
GET    /api/v1/payments/order/{orderId}  - Get payment by order ID
GET    /api/v1/payments/user/{userId}    - Get payments by user
GET    /api/v1/payments/status/{status}  - Get payments by status
PUT    /api/v1/payments/{id}/status      - Update payment status
DELETE /api/v1/payments/{id}             - Delete payment
```

## Database Access

### MySQL Connection
```
Host: localhost
Port: 3306
Database: microservices_db
User: root
Password: test
```

### Sample Queries

**View All Payments**:
```sql
SELECT * FROM payments;
```

**View Payments by Order**:
```sql
SELECT * FROM payments WHERE order_id = 1;
```

**View Completed Payments**:
```sql
SELECT * FROM payments WHERE status = 'COMPLETED';
```

## Monitoring & Logs

### Check Service Health

**Order Service**:
```bash
curl http://localhost:8080/actuator/health
```

**Payment Service**:
```bash
curl http://localhost:8084/actuator/health
```

### View Logs

**If running via Docker**:
```bash
docker logs -f order-service
docker logs -f payment-service
docker logs -f kafka
```

**If running via Java**:
Logs will print to console in respective terminal windows.

### Key Log Messages

Watch for these in payment-service logs:
- `Received order event` - Event consumed
- `Processing payment for placed order` - Processing started
- `Payment created with ID` - Payment record created
- `Payment transaction completed` - Transaction successful
- `Payment event sent for order` - Event published to Kafka

## Troubleshooting

### Issue: Payment not created after order
**Solution**: 
1. Check if Kafka is running: `docker ps | grep kafka`
2. Check payment service logs for errors
3. Verify consumer group: `payment-service-group`
4. Ensure bootstrap servers configured: `localhost:9092`

### Issue: Connection refused error
**Solution**:
1. Verify all services are running: `docker-compose ps`
2. Check ports are not blocked
3. Wait 30 seconds after starting Kafka (startup time)

### Issue: Database errors
**Solution**:
1. Ensure MySQL container is running
2. Verify credentials: root/test
3. Check database exists: `microservices_db`
4. Check tables created (JPA auto-creates on first run)

### Issue: Security exceptions on orders
**Solution**:
- Order endpoints require JWT token or should be updated for testing
- For now, comment out @PreAuthorize annotations in OrderController for testing
- Or provide valid JWT token in Authorization header

## Performance Tips

1. **Increase Kafka Partitions**: Higher throughput with multiple consumers
2. **Connection Pooling**: Configure HikariCP for database connections
3. **Caching**: Add @Cacheable on payment queries
4. **Async Processing**: Consider async payment processing for external gateways
5. **Batch Operations**: Process multiple orders in batches

## Security Considerations

1. **Enable Authentication**: Implement JWT or OAuth2
2. **HTTPS**: Use HTTPS in production
3. **Database Credentials**: Store in environment variables
4. **Kafka Security**: Configure SASL/SSL for Kafka
5. **API Rate Limiting**: Implement rate limiting on endpoints
6. **Input Validation**: All inputs are validated (already implemented)

## Additional Resources

- See `PAYMENT_SERVICE_IMPLEMENTATION.md` for detailed architecture
- See `payment-service/KAFKA_INTEGRATION.md` for Kafka specifics
- Order Service ARCHITECTURE.md available in order-service folder

## Common Commands

```bash
# Build all services
mvn clean install -DskipTests -DskipDockerBuild

# Start infrastructure
docker-compose up -d

# Stop infrastructure
docker-compose down

# View Docker logs
docker-compose logs -f

# Rebuild payment service
cd payment-service && mvn clean package -DskipTests

# Run payment service locally
cd payment-service && mvn spring-boot:run
```

## Support

For issues or questions:
1. Check the logs (see Monitoring & Logs section)
2. Review PAYMENT_SERVICE_IMPLEMENTATION.md
3. Verify all containers are running
4. Ensure Kafka topic exists: `order-events`
5. Check database connectivity

Happy testing! 🚀

