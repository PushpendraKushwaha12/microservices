package com.inventory_service.service.impl;

import com.inventory_service.dto.InventoryItemDto;

import java.util.List;

public interface InventoryItemServiceImpl {
    InventoryItemDto createInventoryItem(InventoryItemDto inventoryItemDto);
    List<InventoryItemDto> getAllInventoryItems();
    InventoryItemDto getInventoryItemById(Long id);
    InventoryItemDto updateInventoryItem(Long id, InventoryItemDto inventoryItemDto);
    void deleteInventoryItem(Long id);
    List<InventoryItemDto> getLowStockItems();
    List<InventoryItemDto> getOutOfStockItems();
    Long getTotalQuantityByProductId(Long productId);
    Long getAvailableQuantityByProductId(Long productId);
}
