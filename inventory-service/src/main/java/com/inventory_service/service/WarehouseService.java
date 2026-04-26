package com.inventory_service.service;

import com.inventory_service.dto.WarehouseDto;
import com.inventory_service.entity.Warehouse;
import com.inventory_service.exception.InventoryException;
import com.inventory_service.repository.WarehouseRepository;
import com.inventory_service.service.impl.WarehouseServiceImpl;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WarehouseService implements WarehouseServiceImpl {

    private final WarehouseRepository warehouseRepository;
    private final ModelMapper modelMapper;

    @Override
    public WarehouseDto createWarehouse(WarehouseDto warehouseDto) {
        // Check if warehouse code already exists
        if (warehouseRepository.findByWarehouseCode(warehouseDto.getWarehouseCode()).isPresent()) {
            throw new InventoryException("Warehouse with code '" + warehouseDto.getWarehouseCode() + "' already exists", 409);
        }

        Warehouse warehouse = modelMapper.map(warehouseDto, Warehouse.class);
        warehouse.setCreatedDate(LocalDateTime.now());
        warehouse.setLastUpdatedDate(LocalDateTime.now());
        warehouse.setActive(true);

        Warehouse savedWarehouse = warehouseRepository.save(warehouse);
        return modelMapper.map(savedWarehouse, WarehouseDto.class);
    }

    @Override
    public List<WarehouseDto> getAllWarehouses() {
        return warehouseRepository.findAll().stream()
                .map(warehouse -> modelMapper.map(warehouse, WarehouseDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public WarehouseDto getWarehouseById(Long id) {
        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new InventoryException("Warehouse not found with id: " + id, 404));
        return modelMapper.map(warehouse, WarehouseDto.class);
    }

    @Override
    public WarehouseDto getWarehouseByCode(String warehouseCode) {
        Warehouse warehouse = warehouseRepository.findByWarehouseCode(warehouseCode)
                .orElseThrow(() -> new InventoryException("Warehouse not found with code: " + warehouseCode, 404));
        return modelMapper.map(warehouse, WarehouseDto.class);
    }

    @Override
    public WarehouseDto updateWarehouse(Long id, WarehouseDto warehouseDto) {
        Warehouse existingWarehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new InventoryException("Warehouse not found with id: " + id, 404));

        // Check if warehouse code is being changed and if it conflicts with another warehouse
        if (!existingWarehouse.getWarehouseCode().equals(warehouseDto.getWarehouseCode()) &&
            warehouseRepository.findByWarehouseCode(warehouseDto.getWarehouseCode()).isPresent()) {
            throw new InventoryException("Warehouse with code '" + warehouseDto.getWarehouseCode() + "' already exists", 409);
        }

        Warehouse updatedWarehouse = modelMapper.map(warehouseDto, Warehouse.class);
        updatedWarehouse.setId(id);
        updatedWarehouse.setCreatedDate(existingWarehouse.getCreatedDate());
        updatedWarehouse.setLastUpdatedDate(LocalDateTime.now());

        Warehouse savedWarehouse = warehouseRepository.save(updatedWarehouse);
        return modelMapper.map(savedWarehouse, WarehouseDto.class);
    }

    @Override
    public void deleteWarehouse(Long id) {
        if (!warehouseRepository.existsById(id)) {
            throw new InventoryException("Warehouse not found with id: " + id, 404);
        }
        warehouseRepository.deleteById(id);
    }

    @Override
    public List<WarehouseDto> getActiveWarehouses() {
        return warehouseRepository.findByIsActiveTrue().stream()
                .map(warehouse -> modelMapper.map(warehouse, WarehouseDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<WarehouseDto> searchWarehouses(String keyword) {
        return warehouseRepository.searchWarehouses(keyword).stream()
                .map(warehouse -> modelMapper.map(warehouse, WarehouseDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public Long getActiveWarehouseCount() {
        return warehouseRepository.countActiveWarehouses();
    }
}
