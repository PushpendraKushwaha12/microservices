package com.inventory_service.controller;

import com.inventory_service.dto.InventoryItemDto;
import com.inventory_service.payload.response.APIResponse;
import com.inventory_service.service.InventoryItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventory/items")
@Slf4j
@RequiredArgsConstructor
public class InventoryItemController {

    private final InventoryItemService inventoryItemService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse<InventoryItemDto>> createInventoryItem(@Valid @RequestBody InventoryItemDto inventoryItemDto) {
        log.info("Creating new inventory item for product: {}", inventoryItemDto.getProductName());
        InventoryItemDto createdItem = inventoryItemService.createInventoryItem(inventoryItemDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new APIResponse<>(HttpStatus.CREATED.value(), "Inventory item created successfully", createdItem));
    }

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<APIResponse<List<InventoryItemDto>>> getAllInventoryItems() {
        log.info("Fetching all inventory items");
        List<InventoryItemDto> items = inventoryItemService.getAllInventoryItems();
        return ResponseEntity.ok(new APIResponse<>(HttpStatus.OK.value(), "Inventory items fetched successfully", items));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<APIResponse<InventoryItemDto>> getInventoryItemById(@PathVariable Long id) {
        log.info("Fetching inventory item with id: {}", id);
        InventoryItemDto item = inventoryItemService.getInventoryItemById(id);
        return ResponseEntity.ok(new APIResponse<>(HttpStatus.OK.value(), "Inventory item fetched successfully", item));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse<InventoryItemDto>> updateInventoryItem(@PathVariable Long id, @Valid @RequestBody InventoryItemDto inventoryItemDto) {
        log.info("Updating inventory item with id: {}", id);
        InventoryItemDto updatedItem = inventoryItemService.updateInventoryItem(id, inventoryItemDto);
        return ResponseEntity.ok(new APIResponse<>(HttpStatus.OK.value(), "Inventory item updated successfully", updatedItem));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse<Void>> deleteInventoryItem(@PathVariable Long id) {
        log.info("Deleting inventory item with id: {}", id);
        inventoryItemService.deleteInventoryItem(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(new APIResponse<>(HttpStatus.NO_CONTENT.value(), "Inventory item deleted successfully"));
    }

    @GetMapping("/low-stock")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<APIResponse<List<InventoryItemDto>>> getLowStockItems() {
        log.info("Fetching low stock items");
        List<InventoryItemDto> items = inventoryItemService.getLowStockItems();
        return ResponseEntity.ok(new APIResponse<>(HttpStatus.OK.value(), "Low stock items fetched successfully", items));
    }

    @GetMapping("/out-of-stock")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<APIResponse<List<InventoryItemDto>>> getOutOfStockItems() {
        log.info("Fetching out of stock items");
        List<InventoryItemDto> items = inventoryItemService.getOutOfStockItems();
        return ResponseEntity.ok(new APIResponse<>(HttpStatus.OK.value(), "Out of stock items fetched successfully", items));
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<APIResponse<List<InventoryItemDto>>> searchInventoryItems(@RequestParam String keyword) {
        log.info("Searching inventory items with keyword: {}", keyword);
        List<InventoryItemDto> items = inventoryItemService.searchInventoryItems(keyword);
        return ResponseEntity.ok(new APIResponse<>(HttpStatus.OK.value(), "Inventory items searched successfully", items));
    }

    @GetMapping("/product/{productId}/total-quantity")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<APIResponse<Long>> getTotalQuantityByProductId(@PathVariable Long productId) {
        log.info("Getting total quantity for product id: {}", productId);
        Long totalQuantity = inventoryItemService.getTotalQuantityByProductId(productId);
        return ResponseEntity.ok(new APIResponse<>(HttpStatus.OK.value(), "Total quantity fetched successfully", totalQuantity));
    }

    @GetMapping("/product/{productId}/available-quantity")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<APIResponse<Long>> getAvailableQuantityByProductId(@PathVariable Long productId) {
        log.info("Getting available quantity for product id: {}", productId);
        Long availableQuantity = inventoryItemService.getAvailableQuantityByProductId(productId);
        return ResponseEntity.ok(new APIResponse<>(HttpStatus.OK.value(), "Available quantity fetched successfully", availableQuantity));
    }
}
