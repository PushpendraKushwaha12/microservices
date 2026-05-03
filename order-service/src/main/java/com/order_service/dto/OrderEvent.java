package com.order_service.dto;

import com.order_service.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderEvent {
    private Long orderId;
    private Long userId;
    private Long productId;
    private int quantity;
    private String shippingAddress;
    private String paymentMethod;
    private BigDecimal totalPrice;
    private double discount;
    private double tax;
    private String notes;
    private LocalDateTime orderDate;
    private OrderStatus status;
    private String orderNumber;
    private String eventType;
}
