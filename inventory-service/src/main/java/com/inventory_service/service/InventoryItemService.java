package com.inventory_service.service;

import com.inventory_service.dto.InventoryItemDto;
import com.inventory_service.entity.InventoryItem;
import com.inventory_service.enums.InventoryStatus;
import com.inventory_service.exception.InventoryException;
import com.inventory_service.repository.InventoryItemRepository;
import com.inventory_service.service.impl.InventoryItemServiceImpl;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryItemService implements InventoryItemServiceImpl {

    private final InventoryItemRepository inventoryItemRepository;
    private final ModelMapper modelMapper;

    @Override
    public InventoryItemDto createInventoryItem(InventoryItemDto inventoryItemDto) {
        if (inventoryItemRepository.findByProductIdAndWarehouseId(
                inventoryItemDto.getProductId(), inventoryItemDto.getWarehouseId()).isPresent()) {
            throw new InventoryException("Inventory item already exists for this product and warehouse", 409);
        }

        InventoryItem inventoryItem = modelMapper.map(inventoryItemDto, InventoryItem.class);
        inventoryItem.setCreatedDate(LocalDateTime.now());
        inventoryItem.setLastUpdatedDate(LocalDateTime.now());
        inventoryItem.setStatus(InventoryStatus.AVAILABLE);
        inventoryItem.setActive(true);
        inventoryItem.setAvailableQuantity(
            inventoryItem.getQuantity() - inventoryItem.getReservedQuantity() - inventoryItem.getAllocatedQuantity()
        );

        if (inventoryItem.getUnitCost() != null && inventoryItem.getQuantity() > 0) {
            inventoryItem.setTotalValue(inventoryItem.getUnitCost().multiply(
                java.math.BigDecimal.valueOf(inventoryItem.getQuantity())));
        }

        InventoryItem savedItem = inventoryItemRepository.save(inventoryItem);
        return modelMapper.map(savedItem, InventoryItemDto.class);
    }

    @Override
    public List<InventoryItemDto> getAllInventoryItems() {
        return inventoryItemRepository.findAll().stream()
                .map(item -> modelMapper.map(item, InventoryItemDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public InventoryItemDto getInventoryItemById(Long id) {
        InventoryItem inventoryItem = inventoryItemRepository.findById(id)
                .orElseThrow(() -> new InventoryException("Inventory item not found with id: " + id, 404));
        return modelMapper.map(inventoryItem, InventoryItemDto.class);
    }

    @Override
    public InventoryItemDto updateInventoryItem(Long id, InventoryItemDto inventoryItemDto) {
        InventoryItem existingItem = inventoryItemRepository.findById(id)
                .orElseThrow(() -> new InventoryException("Inventory item not found with id: " + id, 404));

        InventoryItem updatedItem = modelMapper.map(inventoryItemDto, InventoryItem.class);
        updatedItem.setId(id);
        updatedItem.setCreatedDate(existingItem.getCreatedDate());
        updatedItem.setLastUpdatedDate(LocalDateTime.now());

        updatedItem.setAvailableQuantity(
            updatedItem.getQuantity() - updatedItem.getReservedQuantity() - updatedItem.getAllocatedQuantity()
        );

        if (updatedItem.getUnitCost() != null && updatedItem.getQuantity() > 0) {
            updatedItem.setTotalValue(updatedItem.getUnitCost().multiply(
                java.math.BigDecimal.valueOf(updatedItem.getQuantity())));
        }

        InventoryItem savedItem = inventoryItemRepository.save(updatedItem);
        return modelMapper.map(savedItem, InventoryItemDto.class);
    }

    @Override
    public void deleteInventoryItem(Long id) {
        if (!inventoryItemRepository.existsById(id)) {
            throw new InventoryException("Inventory item not found with id: " + id, 404);
        }
        inventoryItemRepository.deleteById(id);
    }

    @Override
    public List<InventoryItemDto> getLowStockItems() {
        return inventoryItemRepository.findLowStockItems().stream()
                .map(item -> modelMapper.map(item, InventoryItemDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<InventoryItemDto> getOutOfStockItems() {
        return inventoryItemRepository.findOutOfStockItems().stream()
                .map(item -> modelMapper.map(item, InventoryItemDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public Long getTotalQuantityByProductId(Long productId) {
        Long total = inventoryItemRepository.getTotalQuantityByProductId(productId);
        return total != null ? total : 0L;
    }

    @Override
    public Long getAvailableQuantityByProductId(Long productId) {
        Long available = inventoryItemRepository.getAvailableQuantityByProductId(productId);
        return available != null ? available : 0L;
    }
}
