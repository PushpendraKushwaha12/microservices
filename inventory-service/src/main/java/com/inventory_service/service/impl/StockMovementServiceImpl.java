package com.inventory_service.service.impl;

import com.inventory_service.dto.StockMovementDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StockMovementServiceImpl {
    StockMovementDto createStockMovement(StockMovementDto stockMovementDto);
    List<StockMovementDto> getAllStockMovements();
    StockMovementDto getStockMovementById(Long id);
    List<StockMovementDto> getMovementsByInventoryItemId(Long inventoryItemId);
}
