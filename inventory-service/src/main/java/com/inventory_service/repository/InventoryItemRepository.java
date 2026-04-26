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

    List<InventoryItem> findByWarehouseId(Long warehouseId);

    List<InventoryItem> findByProductSku(String productSku);

    List<InventoryItem> findByBatchNumber(String batchNumber);

    List<InventoryItem> findByLocationCode(String locationCode);

    @Query("SELECT i FROM InventoryItem i WHERE i.quantity <= i.reorderPoint AND i.isActive = true")
    List<InventoryItem> findLowStockItems();

    @Query("SELECT i FROM InventoryItem i WHERE i.quantity = 0 AND i.isActive = true")
    List<InventoryItem> findOutOfStockItems();

    @Query("SELECT i FROM InventoryItem i WHERE i.status = 'EXPIRED' AND i.isActive = true")
    List<InventoryItem> findExpiredItems();

    @Query("SELECT i FROM InventoryItem i WHERE i.status = 'DAMAGED' AND i.isActive = true")
    List<InventoryItem> findDamagedItems();

    @Query("SELECT SUM(i.quantity) FROM InventoryItem i WHERE i.productId = :productId AND i.isActive = true")
    Long getTotalQuantityByProductId(@Param("productId") Long productId);

    @Query("SELECT SUM(i.availableQuantity) FROM InventoryItem i WHERE i.productId = :productId AND i.isActive = true")
    Long getAvailableQuantityByProductId(@Param("productId") Long productId);

    @Query("SELECT SUM(i.totalValue) FROM InventoryItem i WHERE i.warehouseId = :warehouseId AND i.isActive = true")
    java.math.BigDecimal getTotalValueByWarehouseId(@Param("warehouseId") Long warehouseId);

    @Query("SELECT i FROM InventoryItem i WHERE LOWER(i.productName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(i.productSku) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<InventoryItem> searchInventoryItems(@Param("keyword") String keyword);

    @Query("SELECT i FROM InventoryItem i WHERE i.zone = :zone AND i.aisle = :aisle AND i.shelf = :shelf AND i.bin = :bin")
    Optional<InventoryItem> findByLocation(@Param("zone") String zone, @Param("aisle") String aisle, @Param("shelf") String shelf, @Param("bin") String bin);
}
