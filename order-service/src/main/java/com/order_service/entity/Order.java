package com.order_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.order_service.enums.OrderStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Product ID is required")
    private Long productId;

    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;

    private LocalDateTime orderDate;

    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @DecimalMin(value = "0.01", message = "Total price must be greater than 0")
    private double totalPrice;

    @NotBlank(message = "Shipping address is required")
    private String shippingAddress;

    @NotBlank(message = "Payment method is required")
    private String paymentMethod;

    private String orderNumber;

    @DecimalMin(value = "0.0", message = "Discount cannot be negative")
    private double discount;

    @DecimalMin(value = "0.0", message = "Tax cannot be negative")
    private double tax;

    private String notes;
}
