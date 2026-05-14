package com.inventory_service.controller;

import com.inventory_service.dto.WarehouseDto;
import com.inventory_service.payload.response.APIResponse;
import com.inventory_service.service.WarehouseService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventory/warehouses")
@Slf4j
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class WarehouseController {

    private final WarehouseService warehouseService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse<WarehouseDto>> createWarehouse(@Valid @RequestBody WarehouseDto warehouseDto) {
        log.info("Creating new warehouse: {}", warehouseDto.getWarehouseName());
        WarehouseDto createdWarehouse = warehouseService.createWarehouse(warehouseDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new APIResponse<>(HttpStatus.CREATED.value(), "Warehouse created successfully", createdWarehouse));
    }

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<APIResponse<List<WarehouseDto>>> getAllWarehouses() {
        log.info("Fetching all warehouses");
        List<WarehouseDto> warehouses = warehouseService.getAllWarehouses();
        return ResponseEntity.ok(new APIResponse<>(HttpStatus.OK.value(), "Warehouses fetched successfully", warehouses));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<APIResponse<WarehouseDto>> getWarehouseById(@PathVariable Long id) {
        log.info("Fetching warehouse with id: {}", id);
        WarehouseDto warehouse = warehouseService.getWarehouseById(id);
        return ResponseEntity.ok(new APIResponse<>(HttpStatus.OK.value(), "Warehouse fetched successfully", warehouse));
    }

    @GetMapping("/code/{warehouseCode}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<APIResponse<WarehouseDto>> getWarehouseByCode(@PathVariable String warehouseCode) {
        log.info("Fetching warehouse with code: {}", warehouseCode);
        WarehouseDto warehouse = warehouseService.getWarehouseByCode(warehouseCode);
        return ResponseEntity.ok(new APIResponse<>(HttpStatus.OK.value(), "Warehouse fetched successfully", warehouse));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse<WarehouseDto>> updateWarehouse(@PathVariable Long id, @Valid @RequestBody WarehouseDto warehouseDto) {
        log.info("Updating warehouse with id: {}", id);
        WarehouseDto updatedWarehouse = warehouseService.updateWarehouse(id, warehouseDto);
        return ResponseEntity.ok(new APIResponse<>(HttpStatus.OK.value(), "Warehouse updated successfully", updatedWarehouse));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse<Void>> deleteWarehouse(@PathVariable Long id) {
        log.info("Deleting warehouse with id: {}", id);
        warehouseService.deleteWarehouse(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(new APIResponse<>(HttpStatus.NO_CONTENT.value(), "Warehouse deleted successfully"));
    }

    @GetMapping("/active")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<APIResponse<List<WarehouseDto>>> getActiveWarehouses() {
        log.info("Fetching active warehouses");
        List<WarehouseDto> warehouses = warehouseService.getActiveWarehouses();
        return ResponseEntity.ok(new APIResponse<>(HttpStatus.OK.value(), "Active warehouses fetched successfully", warehouses));
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<APIResponse<List<WarehouseDto>>> searchWarehouses(@RequestParam String keyword) {
        log.info("Searching warehouses with keyword: {}", keyword);
        List<WarehouseDto> warehouses = warehouseService.searchWarehouses(keyword);
        return ResponseEntity.ok(new APIResponse<>(HttpStatus.OK.value(), "Warehouses searched successfully", warehouses));
    }

    @GetMapping("/count/active")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<APIResponse<Long>> getActiveWarehouseCount() {
        log.info("Getting active warehouse count");
        Long count = warehouseService.getActiveWarehouseCount();
        return ResponseEntity.ok(new APIResponse<>(HttpStatus.OK.value(), "Active warehouse count fetched successfully", count));
    }
}
