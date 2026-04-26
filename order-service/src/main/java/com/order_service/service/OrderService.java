package com.order_service.service;

import com.order_service.dto.Orderdto;
import com.order_service.entity.Order;
import com.order_service.enums.OrderStatus;
import com.order_service.exception.OrderException;
import com.order_service.repository.OrderRepository;
import com.order_service.service.impl.OrderServiceImpl;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService implements OrderServiceImpl {

    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;

    @Override
    public Orderdto placeOrder(Orderdto orderDto) {
        Order order = Order.builder()
                .userId(orderDto.getUserId())
                .productId(orderDto.getProductId())
                .quantity(orderDto.getQuantity())
                .shippingAddress(orderDto.getShippingAddress())
                .paymentMethod(orderDto.getPaymentMethod())
                .totalPrice(orderDto.getTotalPrice())
                .discount(orderDto.getDiscount())
                .tax(orderDto.getTax())
                .notes(orderDto.getNotes())
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.PENDING)
                .orderNumber(generateOrderNumber())
                .build();

        Order savedOrder = orderRepository.save(order);
        return modelMapper.map(savedOrder, Orderdto.class);
    }

    @Override
    public List<Orderdto> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(order -> modelMapper.map(order, Orderdto.class))
                .collect(Collectors.toList());
    }

    @Override
    public Orderdto getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderException("Order not found with id: " + id, 404));
        return modelMapper.map(order, Orderdto.class);
    }

    @Override
    public Orderdto updateOrder(Long id, Orderdto orderDto) {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new OrderException("Order not found with id: " + id, 404));

        Order updatedOrder = Order.builder()
                .id(id)
                .userId(orderDto.getUserId())
                .productId(orderDto.getProductId())
                .quantity(orderDto.getQuantity())
                .shippingAddress(orderDto.getShippingAddress())
                .paymentMethod(orderDto.getPaymentMethod())
                .totalPrice(orderDto.getTotalPrice())
                .discount(orderDto.getDiscount())
                .tax(orderDto.getTax())
                .notes(orderDto.getNotes())
                .status(orderDto.getStatus())
                .orderDate(existingOrder.getOrderDate())
                .orderNumber(existingOrder.getOrderNumber())
                .build();

        Order saved = orderRepository.save(updatedOrder);
        return modelMapper.map(saved, Orderdto.class);
    }

    @Override
    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new OrderException("Order not found with id: " + id, 404);
        }
        orderRepository.deleteById(id);
    }

    private String generateOrderNumber() {

        return "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
