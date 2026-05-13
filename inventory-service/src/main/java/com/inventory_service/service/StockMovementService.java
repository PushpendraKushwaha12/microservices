package com.inventory_service.service;

import com.inventory_service.dto.StockMovementDto;
import com.inventory_service.entity.StockMovement;
import com.inventory_service.exception.InventoryException;
import com.inventory_service.repository.StockMovementRepository;
import com.inventory_service.service.impl.StockMovementServiceImpl;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockMovementService implements StockMovementServiceImpl {

    private final StockMovementRepository stockMovementRepository;
    private final ModelMapper modelMapper;

    @Override
    public StockMovementDto createStockMovement(StockMovementDto stockMovementDto) {
        StockMovement stockMovement = modelMapper.map(stockMovementDto, StockMovement.class);
        stockMovement.setCreatedDate(LocalDateTime.now());

        if (stockMovement.getUnitCost() != null && stockMovement.getQuantity() != 0) {
            stockMovement.setTotalValue(stockMovement.getUnitCost().multiply(
                java.math.BigDecimal.valueOf(Math.abs(stockMovement.getQuantity()))));
        }

        StockMovement savedMovement = stockMovementRepository.save(stockMovement);
        return modelMapper.map(savedMovement, StockMovementDto.class);
    }

    @Override
    public List<StockMovementDto> getAllStockMovements() {
        return stockMovementRepository.findAll().stream()
                .map(movement -> modelMapper.map(movement, StockMovementDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public StockMovementDto getStockMovementById(Long id) {
        StockMovement stockMovement = stockMovementRepository.findById(id)
                .orElseThrow(() -> new InventoryException("Stock movement not found with id: " + id, 404));
        return modelMapper.map(stockMovement, StockMovementDto.class);
    }

    @Override
    public List<StockMovementDto> getMovementsByInventoryItemId(Long inventoryItemId) {
        return stockMovementRepository.findByInventoryItemId(inventoryItemId).stream()
                .map(movement -> modelMapper.map(movement, StockMovementDto.class))
                .collect(Collectors.toList());
    }

}
