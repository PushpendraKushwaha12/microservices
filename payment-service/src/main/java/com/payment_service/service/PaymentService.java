package com.payment_service.service;

import com.payment_service.dto.OrderEvent;
import com.payment_service.dto.PaymentDto;
import com.payment_service.enums.PaymentStatus;

import java.util.List;

public interface PaymentService {
    PaymentDto processPayment(OrderEvent orderEvent);
    PaymentDto getPaymentById(Long id);
    PaymentDto getPaymentByOrderId(Long orderId);
    List<PaymentDto> getPaymentsByUserId(Long userId);
    List<PaymentDto> getPaymentsByStatus(PaymentStatus status);
    PaymentDto updatePaymentStatus(Long paymentId, PaymentStatus status);
    void deletePayment(Long id);
}

