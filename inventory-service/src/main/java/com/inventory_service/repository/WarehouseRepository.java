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

    List<Warehouse> findByRegion(String region);

    List<Warehouse> findByCountry(String country);

    List<Warehouse> findByIsDefaultTrue();

    @Query("SELECT w FROM Warehouse w WHERE LOWER(w.warehouseName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(w.warehouseCode) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Warehouse> searchWarehouses(@Param("keyword") String keyword);

    @Query("SELECT w FROM Warehouse w WHERE w.latitude BETWEEN :minLat AND :maxLat AND w.longitude BETWEEN :minLng AND :maxLng")
    List<Warehouse> findWarehousesInArea(@Param("minLat") Double minLat, @Param("maxLat") Double maxLat, @Param("minLng") Double minLng, @Param("maxLng") Double maxLng);

    @Query("SELECT COUNT(w) FROM Warehouse w WHERE w.isActive = true")
    Long countActiveWarehouses();

    @Query("SELECT SUM(w.totalCapacitySqFt) FROM Warehouse w WHERE w.isActive = true")
    Double getTotalCapacity();

    @Query("SELECT SUM(w.usedCapacitySqFt) FROM Warehouse w WHERE w.isActive = true")
    Double getUsedCapacity();

    @Query("SELECT w FROM Warehouse w WHERE w.isActive = true ORDER BY w.createdDate DESC")
    List<Warehouse> findActiveWarehousesOrderedByCreationDate();
}
