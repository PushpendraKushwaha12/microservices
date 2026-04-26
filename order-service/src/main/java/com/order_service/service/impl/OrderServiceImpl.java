package com.order_service.service.impl;

import com.order_service.dto.Orderdto;

import java.util.List;

public interface OrderServiceImpl {
    Orderdto placeOrder(Orderdto order);
    List<Orderdto> getAllOrders();
    Orderdto getOrderById(Long id);
    Orderdto updateOrder(Long id, Orderdto order);
    void deleteOrder(Long id);
}
