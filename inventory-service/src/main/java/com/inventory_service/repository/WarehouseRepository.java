package com.inventory_service.repository;

import com.inventory_service.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {

    Optional<Warehouse> findByWarehouseCode(String warehouseCode);

    List<Warehouse> findByIsActiveTrue();

    @Query("SELECT w FROM Warehouse w WHERE LOWER(w.warehouseName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(w.warehouseCode) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Warehouse> searchWarehouses(@Param("keyword") String keyword);

    @Query("SELECT COUNT(w) FROM Warehouse w WHERE w.isActive = true")
    Long countActiveWarehouses();

}
