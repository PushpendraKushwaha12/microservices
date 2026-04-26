package com.order_service.repository;

import com.order_service.entity.Order;
import com.order_service.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long> {
    boolean existsByUserIdAndProductIdAndStatus(Long userId, Long productId, OrderStatus status);
}
