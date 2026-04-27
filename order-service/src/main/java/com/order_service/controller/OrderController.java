package com.order_service.controller;

import com.order_service.dto.OrderDto;
import com.order_service.payload.response.APIResponse;
import com.order_service.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@Slf4j
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<APIResponse<OrderDto>> createOrder(@Valid @RequestBody OrderDto orderDto) {
        log.info("Creating new order");
        OrderDto createdOrder = orderService.placeOrder(orderDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new APIResponse<>(HttpStatus.CREATED.value(), "Order created successfully", createdOrder));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse<List<OrderDto>>> getAllOrders() {
        log.info("Fetching all orders");
        List<OrderDto> orders = orderService.getAllOrders();
        return ResponseEntity.ok(new APIResponse<>(HttpStatus.OK.value(), "Orders fetched successfully", orders));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<APIResponse<OrderDto>> getOrderById(@PathVariable Long id) {
        log.info("Fetching order with id: {}", id);
        OrderDto order = orderService.getOrderById(id);
        return ResponseEntity.ok(new APIResponse<>(HttpStatus.OK.value(), "Order fetched successfully", order));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<APIResponse<OrderDto>> updateOrder(@PathVariable Long id, @Valid @RequestBody OrderDto orderDto) {
        log.info("Updating order with id: {}", id);
        OrderDto updatedOrder = orderService.updateOrder(id, orderDto);
        return ResponseEntity.ok(new APIResponse<>(HttpStatus.OK.value(), "Order updated successfully", updatedOrder));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse<Void>> deleteOrder(@PathVariable Long id) {
        log.info("Deleting order with id: {}", id);
        orderService.deleteOrder(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(new APIResponse<>(HttpStatus.NO_CONTENT.value(), "Order deleted successfully"));
    }
}
