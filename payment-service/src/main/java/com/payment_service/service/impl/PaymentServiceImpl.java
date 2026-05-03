package com.payment_service.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.payment_service.dto.OrderEvent;
import com.payment_service.dto.PaymentDto;
import com.payment_service.dto.PaymentEvent;
import com.payment_service.entity.Payment;
import com.payment_service.enums.PaymentStatus;
import com.payment_service.repository.PaymentRepository;
import com.payment_service.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final ModelMapper modelMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public PaymentDto processPayment(OrderEvent orderEvent) {
        log.info("Processing payment for order: {}", orderEvent.getOrderId());

        // Check if payment already exists for this order
        if (paymentRepository.findByOrderId(orderEvent.getOrderId()).isPresent()) {
            log.warn("Payment already exists for order: {}", orderEvent.getOrderId());
            throw new RuntimeException("Payment already exists for this order");
        }

        // Create payment from order event
        Payment payment = Payment.builder()
                .orderId(orderEvent.getOrderId())
                .userId(orderEvent.getUserId())
                .amount(orderEvent.getTotalPrice())
                .paymentMethod(orderEvent.getPaymentMethod())
                .tax(BigDecimal.valueOf(orderEvent.getTax()))
                .discount(BigDecimal.valueOf(orderEvent.getDiscount()))
                .status(PaymentStatus.PENDING)
                .description("Payment for order: " + orderEvent.getOrderNumber())
                .transactionId(generateTransactionId())
                .build();

        Payment savedPayment = paymentRepository.save(payment);
        log.info("Payment created with ID: {} for order: {}", savedPayment.getId(), orderEvent.getOrderId());

        // Simulate payment processing
        processPaymentTransaction(savedPayment);

        // Send payment event
        sendPaymentEvent(savedPayment);

        return modelMapper.map(savedPayment, PaymentDto.class);
    }

    @Override
    public PaymentDto getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found with id: " + id));
        return modelMapper.map(payment, PaymentDto.class);
    }

    @Override
    public PaymentDto getPaymentByOrderId(Long orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Payment not found for order: " + orderId));
        return modelMapper.map(payment, PaymentDto.class);
    }

    @Override
    public List<PaymentDto> getPaymentsByUserId(Long userId) {
        return paymentRepository.findByUserId(userId).stream()
                .map(payment -> modelMapper.map(payment, PaymentDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<PaymentDto> getPaymentsByStatus(PaymentStatus status) {
        return paymentRepository.findByStatus(status).stream()
                .map(payment -> modelMapper.map(payment, PaymentDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public PaymentDto updatePaymentStatus(Long paymentId, PaymentStatus status) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found with id: " + paymentId));
        payment.setStatus(status);
        Payment updatedPayment = paymentRepository.save(payment);
        log.info("Payment {} status updated to: {}", paymentId, status);
        return modelMapper.map(updatedPayment, PaymentDto.class);
    }

    @Override
    public void deletePayment(Long id) {
        if (!paymentRepository.existsById(id)) {
            throw new RuntimeException("Payment not found with id: " + id);
        }
        paymentRepository.deleteById(id);
        log.info("Payment deleted with ID: {}", id);
    }

    private void processPaymentTransaction(Payment payment) {
        log.info("Processing payment transaction for transaction ID: {}", payment.getTransactionId());
        // Simulate payment processing
        // In a real scenario, this would integrate with a payment gateway (Stripe, PayPal, etc.)
        try {
            // Simulate payment processing delay
            Thread.sleep(1000);
            payment.setStatus(PaymentStatus.COMPLETED);
            payment.setPaymentDate(LocalDateTime.now());
            paymentRepository.save(payment);
            log.info("Payment transaction completed for transaction ID: {}", payment.getTransactionId());
        } catch (InterruptedException e) {
            log.error("Payment processing interrupted", e);
            payment.setStatus(PaymentStatus.FAILED);
            paymentRepository.save(payment);
            Thread.currentThread().interrupt();
        }
    }

    private String generateTransactionId() {
        return "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private void sendPaymentEvent(Payment payment) {
        PaymentEvent paymentEvent = PaymentEvent.builder()
                .paymentId(payment.getId())
                .orderId(payment.getOrderId())
                .userId(payment.getUserId())
                .amount(payment.getAmount())
                .paymentMethod(payment.getPaymentMethod())
                .status(payment.getStatus())
                .paymentDate(payment.getPaymentDate())
                .transactionId(payment.getTransactionId())
                .eventType("PAYMENT_" + payment.getStatus())
                .build();

        try {
            String paymentEventJson = objectMapper.writeValueAsString(paymentEvent);
            kafkaTemplate.send("payment-events", paymentEventJson);
            log.info("Payment event sent for order: {}", payment.getOrderId());
        } catch (Exception e) {
            log.error("Error serializing payment event: {}", e.getMessage(), e);
        }
    }
}
