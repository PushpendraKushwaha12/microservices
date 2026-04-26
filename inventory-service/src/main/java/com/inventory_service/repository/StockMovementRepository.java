package com.inventory_service.repository;

import com.inventory_service.entity.StockMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface StockMovementRepository extends JpaRepository<StockMovement, Long> {

    List<StockMovement> findByInventoryItemId(Long inventoryItemId);

    List<StockMovement> findByProductId(Long productId);

    List<StockMovement> findByMovementType(String movementType);

    List<StockMovement> findByReferenceNumber(String referenceNumber);

    List<StockMovement> findByOrderNumber(String orderNumber);

    List<StockMovement> findByPerformedBy(String performedBy);

    @Query("SELECT sm FROM StockMovement sm WHERE sm.movementDate BETWEEN :startDate AND :endDate ORDER BY sm.movementDate DESC")
    List<StockMovement> findMovementsBetweenDates(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT sm FROM StockMovement sm WHERE sm.inventoryItemId = :inventoryItemId AND sm.movementDate BETWEEN :startDate AND :endDate ORDER BY sm.movementDate DESC")
    List<StockMovement> findMovementsByItemAndDateRange(@Param("inventoryItemId") Long inventoryItemId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT SUM(sm.quantity) FROM StockMovement sm WHERE sm.inventoryItemId = :inventoryItemId AND sm.movementType = 'INBOUND' AND sm.movementDate <= :date")
    Long getInboundQuantityByItemAndDate(@Param("inventoryItemId") Long inventoryItemId, @Param("date") LocalDateTime date);

    @Query("SELECT SUM(sm.quantity) FROM StockMovement sm WHERE sm.inventoryItemId = :inventoryItemId AND sm.movementType = 'OUTBOUND' AND sm.movementDate <= :date")
    Long getOutboundQuantityByItemAndDate(@Param("inventoryItemId") Long inventoryItemId, @Param("date") LocalDateTime date);

    @Query("SELECT sm FROM StockMovement sm WHERE sm.isReversed = false ORDER BY sm.movementDate DESC")
    List<StockMovement> findActiveMovements();

    @Query("SELECT sm FROM StockMovement sm WHERE sm.approvalStatus = 'PENDING'")
    List<StockMovement> findPendingApprovals();

    @Query("SELECT COUNT(sm) FROM StockMovement sm WHERE sm.inventoryItemId = :inventoryItemId AND sm.movementType = :movementType AND sm.movementDate >= :since")
    Long countMovementsByTypeSince(@Param("inventoryItemId") Long inventoryItemId, @Param("movementType") String movementType, @Param("since") LocalDateTime since);
}
