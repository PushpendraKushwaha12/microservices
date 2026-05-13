package com.inventory_service.repository;

import com.inventory_service.entity.StockMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface StockMovementRepository extends JpaRepository<StockMovement, Long> {

    List<StockMovement> findByInventoryItemId(Long inventoryItemId);

    @Query("SELECT sm FROM StockMovement sm WHERE sm.movementDate BETWEEN :startDate AND :endDate ORDER BY sm.movementDate DESC")
    List<StockMovement> findMovementsBetweenDates(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT sm FROM StockMovement sm WHERE sm.approvalStatus = 'PENDING'")
    List<StockMovement> findPendingApprovals();
}
