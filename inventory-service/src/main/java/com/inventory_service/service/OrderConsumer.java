package com.inventory_service.service;

import com.inventory_service.dto.OrderEvent;
import com.inventory_service.entity.InventoryItem;
import com.inventory_service.exception.InventoryException;
import com.inventory_service.repository.InventoryItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderConsumer {

    private final InventoryItemRepository inventoryItemRepository;

    public void consumeOrderEvent(OrderEvent orderEvent) {
        log.info("Received order event: {}", orderEvent);

        if ("ORDER_PLACED".equals(orderEvent.getEventType())) {
            updateInventory(orderEvent);
        }
    }

    private void updateInventory(OrderEvent orderEvent) {
        List<InventoryItem> inventoryItems = inventoryItemRepository.findByProductId(orderEvent.getProductId());

        if (inventoryItems.isEmpty()) {
            throw new InventoryException("Inventory item not found for product ID: " + orderEvent.getProductId(), 404);
        }

        // Assuming we take the first one, or sum quantities, but for simplicity, take first
        InventoryItem inventoryItem = inventoryItems.get(0);

        int orderedQuantity = orderEvent.getQuantity();
        int availableQuantity = inventoryItem.getAvailableQuantity();

        if (availableQuantity < orderedQuantity) {
            throw new InventoryException("Insufficient inventory for product ID: " + orderEvent.getProductId(), 400);
        }

        inventoryItem.setAvailableQuantity(availableQuantity - orderedQuantity);
        inventoryItem.setReservedQuantity(inventoryItem.getReservedQuantity() + orderedQuantity);

        inventoryItemRepository.save(inventoryItem);
        log.info("Updated inventory for product ID: {}, new available quantity: {}", orderEvent.getProductId(), inventoryItem.getAvailableQuantity());
    }
}
