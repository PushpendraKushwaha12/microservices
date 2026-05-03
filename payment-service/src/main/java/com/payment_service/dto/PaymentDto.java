package com.payment_service.dto;

import com.payment_service.enums.PaymentStatus;
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
public class PaymentDto {
    private Long id;
    private Long orderId;
    private Long userId;
    private BigDecimal amount;
    private String paymentMethod;
    private PaymentStatus status;
    private LocalDateTime paymentDate;
    private String transactionId;
    private String description;
    private String notes;
    private BigDecimal tax;
    private BigDecimal discount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

