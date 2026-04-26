package com.order_service.service.impl;

import com.order_service.dto.OrderDto;

import java.util.List;

public interface OrderServiceImpl {
    OrderDto placeOrder(OrderDto order);
    List<OrderDto> getAllOrders();
    OrderDto getOrderById(Long id);
    OrderDto updateOrder(Long id, OrderDto order);
    void deleteOrder(Long id);
}
