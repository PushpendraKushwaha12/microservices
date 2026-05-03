package com.payment_service.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.payment_service.dto.OrderEvent;
import com.payment_service.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderEventConsumer {

    private final PaymentService paymentService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(
            topics = "order-events",
            groupId = "payment-service-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeOrderEvent(String message) {
        try {
            OrderEvent orderEvent = objectMapper.readValue(message, OrderEvent.class);
            log.info("Received order event: {}", orderEvent);
            log.info("Event Type: {}, Order ID: {}", orderEvent.getEventType(), orderEvent.getOrderId());

            if ("ORDER_PLACED".equals(orderEvent.getEventType())) {
                log.info("Processing payment for placed order: {}", orderEvent.getOrderId());
                paymentService.processPayment(orderEvent);
                log.info("Payment processed successfully for order: {}", orderEvent.getOrderId());
            } else {
                log.warn("Unknown event type: {}", orderEvent.getEventType());
            }
        } catch (Exception e) {
            log.error("Error processing order event: {}", message, e);
        }
    }
}
