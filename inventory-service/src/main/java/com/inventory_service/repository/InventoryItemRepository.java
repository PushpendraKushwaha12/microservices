package com.inventory_service.repository;

import com.inventory_service.entity.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long> {

    Optional<InventoryItem> findByProductIdAndWarehouseId(Long productId, Long warehouseId);

    List<InventoryItem> findByProductId(Long productId);


    @Query("SELECT i FROM InventoryItem i WHERE i.quantity <= i.reorderPoint AND i.isActive = true")
    List<InventoryItem> findLowStockItems();

    @Query("SELECT i FROM InventoryItem i WHERE i.quantity = 0 AND i.isActive = true")
    List<InventoryItem> findOutOfStockItems();

    @Query("SELECT SUM(i.quantity) FROM InventoryItem i WHERE i.productId = :productId AND i.isActive = true")
    Long getTotalQuantityByProductId(@Param("productId") Long productId);

    @Query("SELECT SUM(i.availableQuantity) FROM InventoryItem i WHERE i.productId = :productId AND i.isActive = true")
    Long getAvailableQuantityByProductId(@Param("productId") Long productId);

}