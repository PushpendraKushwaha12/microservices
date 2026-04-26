package com.order_service.controller;

import com.order_service.dto.Orderdto;
import com.order_service.payload.response.APIResponse;
import com.order_service.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@Slf4j
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<APIResponse<Orderdto>> createOrder(@Valid @RequestBody Orderdto orderDto) {
        log.info("Creating new order");
        Orderdto createdOrder = orderService.placeOrder(orderDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new APIResponse<>(HttpStatus.CREATED.value(), "Order created successfully", createdOrder));
    }

    @GetMapping
    public ResponseEntity<APIResponse<List<Orderdto>>> getAllOrders() {
        log.info("Fetching all orders");
        List<Orderdto> orders = orderService.getAllOrders();
        return ResponseEntity.ok(new APIResponse<>(HttpStatus.OK.value(), "Orders fetched successfully", orders));
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<Orderdto>> getOrderById(@PathVariable Long id) {
        log.info("Fetching order with id: {}", id);
        Orderdto order = orderService.getOrderById(id);
        return ResponseEntity.ok(new APIResponse<>(HttpStatus.OK.value(), "Order fetched successfully", order));
    }

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<Orderdto>> updateOrder(@PathVariable Long id, @Valid @RequestBody Orderdto orderDto) {
        log.info("Updating order with id: {}", id);
        Orderdto updatedOrder = orderService.updateOrder(id, orderDto);
        return ResponseEntity.ok(new APIResponse<>(HttpStatus.OK.value(), "Order updated successfully", updatedOrder));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<Void>> deleteOrder(@PathVariable Long id) {
        log.info("Deleting order with id: {}", id);
        orderService.deleteOrder(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(new APIResponse<>(HttpStatus.NO_CONTENT.value(), "Order deleted successfully"));
    }
}
