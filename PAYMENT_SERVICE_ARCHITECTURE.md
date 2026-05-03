# 🏗️ Payment Service - Architecture & System Design

## System Architecture Diagram

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                         MICROSERVICES ARCHITECTURE                          │
└─────────────────────────────────────────────────────────────────────────────┘

┌──────────────────────────────────────────────────────────────────────────────┐
│                           CLIENT/API LAYER                                   │
├──────────────────────────────────┬──────────────────────────────────────────┤
│                                  │                                           │
│  POST /api/v1/orders            │  GET /api/v1/payments/*                   │
│  GET  /api/v1/orders/*          │  PUT /api/v1/payments/{id}/status         │
│  PUT  /api/v1/orders/{id}       │  DELETE /api/v1/payments/{id}             │
│                                  │                                           │
└──────────────┬───────────────────┴──────────────────────┬───────────────────┘
               │                                          │
               ▼                                          ▼
    ┌─────────────────────┐                  ┌─────────────────────┐
    │   Order Service     │                  │  Payment Service    │
    │   (Port 8080)       │                  │  (Port 8084)        │
    ├─────────────────────┤                  ├─────────────────────┤
    │ • OrderController   │                  │ • PaymentController │
    │ • OrderService      │                  │ • PaymentService    │
    │ • OrderRepository   │                  │ • PaymentRepository │
    │ • Order Entity      │                  │ • Payment Entity    │
    │                     │                  │ • OrderEventConsumer│
    └──────────┬──────────┘                  └─────────┬──────────┘
               │                                       │
               │                                       │
               │ Publishes:                            │ Consumes:
               │ OrderEvent                            │ OrderEvent
               │                                       │
               └─────────────────┬─────────────────────┘
                                 │
                 ┌───────────────▼───────────────┐
                 │   Apache Kafka (9092)         │
                 ├───────────────────────────────┤
                 │ Topic: order-events           │
                 │ Partitions: 1+                │
                 │ Consumer Group: payment-      │
                 │                service-group  │
                 └───────────────┬───────────────┘
                                 │
                    ┌────────────┴────────────┐
                    │                         │
        ┌───────────▼────────────┐  ┌────────▼──────────────┐
        │  PAYMENT_COMPLETED     │  │  PAYMENT_FAILED      │
        │  Event Published       │  │  Event Published     │
        │  (payment-events)      │  │  (payment-events)    │
        └───────────┬────────────┘  └────────┬──────────────┘
                    │                         │
                    └────────────┬────────────┘
                                 │
                         ┌───────▼────────┐
                         │ Can consume in │
                         │ Order Service  │
                         │ (for updates)  │
                         └────────────────┘
```

## Detailed Service Flow

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                        PAYMENT PROCESSING FLOW                              │
└─────────────────────────────────────────────────────────────────────────────┘

1. ORDER CREATION
┌──────────────────────────────────────────────────────────────┐
│ Client → POST /api/v1/orders                                 │
│         {                                                    │
│           "userId": 1,                                       │
│           "productId": 1,                                    │
│           "quantity": 1,                                     │
│           "totalPrice": 100.50,                              │
│           "paymentMethod": "CREDIT_CARD",                    │
│           ...                                                │
│         }                                                    │
││
└────────────────┬─────────────────────────────────────────────┘
                 │
                 ▼
2. ORDER SAVED TO DATABASE
┌─────────────────────────────────────────────────────────────────┐
│ OrderService.placeOrder()                                       │
│  ├─ Validate duplicate order                                    │
│  ├─ Create Order entity                                         │
│  ├─ Set status = PENDING                                        │
│  ├─ Generate order number (ORD-XXXXXXXX)                        │
│  ├─ Save to database                                            │
│  └─ Call sendOrderEvent(order)                                  │
└────────────────┬────────────────────────────────────────────────┘
                 │
                 ▼
3. ORDER EVENT PUBLISHED TO KAFKA
┌─────────────────────────────────────────────────────────────────┐
│ Kafka Message Topic: "order-events"                             │
│ {                                                               │
│   "orderId": 1,                                                 │
│   "userId": 1,                                                  │
│   "productId": 1,                                               │
│   "quantity": 1,                                                │
│   "totalPrice": 100.50,                                         │
│   "paymentMethod": "CREDIT_CARD",                               │
│   "orderNumber": "ORD-ABC12345",                                │
│   "eventType": "ORDER_PLACED",  ← ← ← KEY FIELD               │
│   "status": "PENDING"                                           │
│ }                                                               │
└────────────────┬────────────────────────────────────────────────┘
                 │
                 ▼
4. PAYMENT SERVICE CONSUMES EVENT
┌─────────────────────────────────────────────────────────────────┐
│ OrderEventConsumer.consumeOrderEvent(OrderEvent event)          │
│  ├─ Receive event from Kafka                                    │
│  ├─ Log received event                                          │
│  ├─ Validate: eventType == "ORDER_PLACED"                       │
│  ├─ Call PaymentService.processPayment(event)                   │
│  └─ Exception handling                                          │
└────────────────┬────────────────────────────────────────────────┘
                 │
                 ▼
5. PAYMENT RECORD CREATED
┌─────────────────────────────────────────────────────────────────┐
│ PaymentServiceImpl.processPayment(OrderEvent)                    │
│  ├─ Check if payment already exists                             │
│  ├─ Create Payment entity                                       │
│  │  ├─ orderId = event.orderId                                  │
│  │  ├─ userId = event.userId                                    │
│  │  ├─ amount = event.totalPrice                                │
│  │  ├─ status = PENDING                                         │
│  │  ├─ transactionId = TXN-XXXXXXXX (generated)                 │
│  │  └─ createdAt = NOW()                                        │
│  ├─ Save to database                                            │
│  ├─ Call processPaymentTransaction(payment)                     │
│  └─ Call sendPaymentEvent(payment)                              │
└────────────────┬────────────────────────────────────────────────┘
                 │
                 ▼
6. PAYMENT TRANSACTION PROCESSING
┌─────────────────────────────────────────────────────────────────┐
│ PaymentServiceImpl.processPaymentTransaction(Payment)            │
│  ├─ Log "Processing payment transaction"                        │
│  ├─ Simulate payment gateway delay (Thread.sleep(1000))         │
│  ├─ Set status = PROCESSING                                     │
│  ├─ ON SUCCESS:                                                 │
│  │  ├─ Set status = COMPLETED                                   │
│  │  ├─ Set paymentDate = NOW()                                  │
│  │  └─ Save to database                                         │
│  ├─ ON ERROR:                                                   │
│  │  ├─ Set status = FAILED                                      │
│  │  └─ Save to database                                         │
│  └─ Log result                                                  │
└────────────────┬────────────────────────────────────────────────┘
                 │
                 ▼
7. PAYMENT EVENT PUBLISHED
┌─────────────────────────────────────────────────────────────────┐
│ Kafka Message Topic: "payment-events"                           │
│ {                                                               │
│   "paymentId": 1,                                               │
│   "orderId": 1,                                                 │
│   "userId": 1,                                                  │
│   "amount": 100.50,                                             │
│   "paymentMethod": "CREDIT_CARD",                               │
│   "status": "COMPLETED",                                        │
│   "paymentDate": "2026-05-03T15:16:01",                         │
│   "transactionId": "TXN-ABC12345",                              │
│   "eventType": "PAYMENT_COMPLETED"  ← ← ← EVENT TYPE          │
│ }                                                               │
└────────────────┬────────────────────────────────────────────────┘
                 │
                 ▼
8. PAYMENT QUERYABLE
┌─────────────────────────────────────────────────────────────────┐
│ Client → GET /api/v1/payments/order/1                           │
│  ├─ Controller calls PaymentService.getPaymentByOrderId(1)      │
│  ├─ Repository queries database                                 │
│  ├─ Returns Payment object                                      │
│  └─ Mapped to PaymentDto                                        │
│                                                                 │
│ Response:                                                       │
│ {                                                               │
│   "id": 1,                                                      │
│   "orderId": 1,                                                 │
│   "userId": 1,                                                  │
│   "amount": 100.50,                                             │
│   "status": "COMPLETED",                                        │
│   "transactionId": "TXN-ABC12345",                              │
│   "paymentDate": "2026-05-03T15:16:01"                          │
│ }                                                               │
└──────────────────────────────────────────────────────────────────┘
```

## Database Schema

```
┌────────────────────────────────────────────────────────────┐
│                    PAYMENTS TABLE                          │
├────────┬──────────────────────────────────────────────────┤
│ Column │ Type         │ Constraints                        │
├────────┼──────────────┼────────────────────────────────────┤
│ id     │ BIGINT       │ PK, AUTO_INCREMENT                 │
│ order_id │ BIGINT     │ FK to orders(id)                   │
│ user_id  │ BIGINT     │ FK to users(id)                    │
│ amount   │ DECIMAL    │ NOT NULL, >= 0.01                  │
│ payment_method │ VARCHAR │ NOT NULL                        │
│ status   │ VARCHAR    │ NOT NULL, ENUM                     │
│ payment_date │ DATETIME │ Auto-set on complete             │
│ transaction_id │ VARCHAR │ UNIQUE, NOT NULL                │
│ description │ VARCHAR  │ Payment description               │
│ notes    │ VARCHAR    │ Additional notes                   │
│ tax      │ DECIMAL    │ >= 0                               │
│ discount │ DECIMAL    │ >= 0                               │
│ created_at │ DATETIME │ NOT NULL, Auto-set                │
│ updated_at │ DATETIME │ NOT NULL, Auto-update              │
└────────────────────────────────────────────────────────────┘

Payment Status Values:
├── PENDING     → Payment awaiting processing
├── PROCESSING  → Payment is being processed
├── COMPLETED   → Payment successful
├── FAILED      → Payment failed
├── CANCELLED   → Payment cancelled
└── REFUNDED    → Payment refunded
```

## Kafka Topic Architecture

```
┌────────────────────────────────────────────────────────────┐
│              KAFKA TOPIC: order-events                     │
├────────────────────────────────────────────────────────────┤
│ Partition 0:  [Event 1] → [Event 2] → [Event 3] → ...    │
│ Partition 1:  [Event 4] → [Event 5] → [Event 6] → ...    │
│ Partition N:  [Event X] → [Event Y] → [Event Z] → ...    │
├────────────────────────────────────────────────────────────┤
│ Consumer Group: payment-service-group                      │
│   ├─ Instance 1: Consuming from Partition 0               │
│   ├─ Instance 2: Consuming from Partition 1               │
│   └─ Instance N: Consuming from Partition N               │
└────────────────────────────────────────────────────────────┘

             ↓↓↓ Event Consumption ↓↓↓

┌────────────────────────────────────────────────────────────┐
│              KAFKA TOPIC: payment-events                   │
├────────────────────────────────────────────────────────────┤
│ Partition 0:  [Event A] → [Event B] → [Event C] → ...    │
│ Consumer: order-service (optional, for order updates)      │
└────────────────────────────────────────────────────────────┘
```

## Service Interaction Diagram

```
┌─────────────┐
│   CLIENT    │
└──────┬──────┘
       │
       │ HTTP Request
       │ POST /api/v1/orders
       │
       ▼
┌─────────────────────────┐
│   ORDER SERVICE         │
│   (Port 8080)           │
├─────────────────────────┤
│ • Validates order       │
│ • Saves to DB           │
│ • Publishes event       │
└──────┬────────┬─────────┘
       │        │
       │        │ HTTP Response
       │        │ Order Created
       │        │
       │        ▼
       │    ┌─────────┐
       │    │ CLIENT  │
       │    └─────────┘
       │
       │ Kafka Message
       │ OrderEvent
       │
       ▼
┌────────────────────────┐
│   KAFKA BROKER         │
│   (localhost:9092)     │
├────────────────────────┤
│ Topic: order-events    │
│ Offset: N              │
└───────────┬────────────┘
            │
            │ Message Fetch
            │
            ▼
┌──────────────────────────────┐
│   PAYMENT SERVICE            │
│   (Port 8084)                │
├──────────────────────────────┤
│ • Consumes event             │
│ • Creates payment record     │
│ • Processes transaction      │
│ • Publishes payment event    │
└──────────┬───────────────────┘
           │
           │ Kafka Message
           │ PaymentEvent
           │
           ▼
        ┌────────────────────┐
        │  KAFKA BROKER      │
        │  (localhost:9092)  │
        ├────────────────────┤
        │ Topic: payment-    │
        │        events      │
        └────────────────────┘
```

## Component Dependency Diagram

```
Payment Service Dependencies:

    ┌──────────────────────────┐
    │  @RestController         │
    │  PaymentController       │
    └────────────┬─────────────┘
                 │
                 │ depends on
                 │
                 ▼
    ┌──────────────────────────┐
    │  @Service                │
    │  PaymentServiceImpl       │
    ├──────────────────────────┤
    │ • PaymentRepository      │
    │ • ModelMapper            │
    │ • KafkaTemplate          │  ◄── Kafka Producer
    └────────┬─────────────┬───┘
             │             │
             │             │ calls
             │             │
             ▼             ▼
    ┌──────────────────────────┐
    │  @Repository             │
    │  PaymentRepository       │
    └────────────┬─────────────┘
                 │
                 │ manages
                 │
                 ▼
    ┌──────────────────────────┐
    │  @Entity                 │
    │  Payment                 │
    └──────────────────────────┘


Order Event Consumer Dependencies:

    ┌──────────────────────────┐
    │  @KafkaListener          │
    │  OrderEventConsumer      │◄── Kafka Consumer
    └────────────┬─────────────┘
                 │
                 │ depends on
                 │
                 ▼
    ┌──────────────────────────┐
    │  @Service                │
    │  PaymentService          │
    └────────────┬─────────────┘
                 │
                 │ calls
                 │
                 ▼
    ┌──────────────────────────┐
    │  PaymentServiceImpl       │
    └──────────────────────────┘
```

## Data Transformation Pipeline

```
Client JSON Input (Order)
          │
          ▼
    ┌─────────────┐
    │ Validation  │
    └──────┬──────┘
           │
           ▼
    ┌─────────────┐
    │ OrderDto    │
    └──────┬──────┘
           │
           ▼
    ┌─────────────┐
    │ Order       │
    │ Entity      │
    └──────┬──────┘
           │
           ▼
    ┌─────────────┐
    │ Database    │
    └──────┬──────┘
           │
           ▼
  ┌──────────────┐
  │ OrderEvent   │──────► Kafka
  │ (DTO)        │
  └──────┬───────┘
         │
         ▼
  ┌──────────────┐
  │ OrderEvent   │ (Consumed)
  │ (DTO)        │
  └──────┬───────┘
         │
         ▼
  ┌──────────────┐
  │ Payment      │
  │ Entity       │
  └──────┬───────┘
         │
         ▼
  ┌──────────────┐
  │ Database     │
  └──────┬───────┘
         │
         ▼
  ┌──────────────┐
  │ PaymentEvent │──────► Kafka
  │ (DTO)        │
  └──────┬───────┘
         │
         ▼
 ┌└──────────────┐
  │ PaymentDto   │
  │ (DTO)        │◄────── REST API Response
  └──────────────┘
```

## Scalability Architecture

```
┌─────────────────────────────────────────────────────────┐
│         Horizontal Scaling Setup (Optional)              │
└─────────────────────────────────────────────────────────┘

        ┌─────────────────────────────────┐
        │     Kafka Cluster               │
        │  (1 or more brokers)            │
        ├─────────────────────────────────┤
        │  Topic: order-events            │
        │  Partitions: 3                  │
        │  Replication Factor: 1+         │
        └────────────────┬────────────────┘
                         │
        ┌────────────────┼────────────────┐
        ▼                ▼                ▼
    ┌────────┐       ┌────────┐       ┌────────┐
    │Payment │       │Payment │       │Payment │
    │Service │       │Service │       │Service │
    │Instance│       │Instance│       │Instance│
    │   1    │       │   2    │       │   3    │
    └────┬───┘       └────┬───┘       └────┬───┘
         │                │                │
         │ Consumer Group: payment-service-group
         │                │                │
         └────────┬───────┴────────┬───────┘
                  │                │
              Partition 0      Partition 1
                  │                │
              Auto Load           Auto Load
              Balancing           Balancing
                  │                │
              One instance    One instance
              per partition   per partition
```

## Error Handling Flow

```
Exception Occurs
       │
       ▼
┌─────────────────────┐
│ Try-Catch Block     │
└────────┬────────────┘
         │
         ├── PaymentException
         │       │
         │       ▼
         │   ┌────────────────────────┐
         │   │ GlobalExceptionHandler │
         │   │ @ExceptionHandler      │
         │   └───────────┬────────────┘
         │               │
         │               ▼
         │   ┌────────────────────────┐
         │   │ HTTP 400 Bad Request   │
         │   │ + Error Details        │
         │   └────────────────────────┘
         │
         └── RuntimeException
                 │
                 ▼
            ┌────────────────────────┐
            │ GlobalExceptionHandler │
            │ (Generic Handler)      │
            └───────────┬────────────┘
                        │
                        ▼
            ┌────────────────────────┐
            │ HTTP 500 Server Error  │
            │ + Error Details        │
            └────────────────────────┘
```

---

## Summary

This architecture provides:
- ✅ **Decoupled Services**: No direct dependencies between Order and Payment
- ✅ **Asynchronous Processing**: Kafka enables non-blocking operations
- ✅ **Scalability**: Multiple instances supported via consumer groups
- ✅ **Reliability**: Kafka ensures message delivery
- ✅ **Observability**: Comprehensive logging and error tracking
- ✅ **Maintainability**: Clear separation of concerns


