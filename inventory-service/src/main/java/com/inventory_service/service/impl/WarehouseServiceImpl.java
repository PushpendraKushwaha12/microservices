package com.inventory_service.service.impl;

import com.inventory_service.dto.WarehouseDto;

import java.util.List;

public interface WarehouseServiceImpl {
    WarehouseDto createWarehouse(WarehouseDto warehouseDto);
    List<WarehouseDto> getAllWarehouses();
    WarehouseDto getWarehouseById(Long id);
    WarehouseDto getWarehouseByCode(String warehouseCode);
    WarehouseDto updateWarehouse(Long id, WarehouseDto warehouseDto);
    void deleteWarehouse(Long id);
    List<WarehouseDto> getActiveWarehouses();
    List<WarehouseDto> searchWarehouses(String keyword);
    Long getActiveWarehouseCount();
}
