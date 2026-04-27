package com.inventory_service.controller;

import com.inventory_service.dto.StockMovementDto;
import com.inventory_service.payload.response.APIResponse;
import com.inventory_service.service.StockMovementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/inventory/movements")
@Slf4j
@RequiredArgsConstructor
public class StockMovementController {

    private final StockMovementService stockMovementService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse<StockMovementDto>> createStockMovement(@Valid @RequestBody StockMovementDto stockMovementDto) {
        log.info("Creating new stock movement for inventory item: {}", stockMovementDto.getInventoryItemId());
        StockMovementDto createdMovement = stockMovementService.createStockMovement(stockMovementDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new APIResponse<>(HttpStatus.CREATED.value(), "Stock movement created successfully", createdMovement));
    }

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<APIResponse<List<StockMovementDto>>> getAllStockMovements() {
        log.info("Fetching all stock movements");
        List<StockMovementDto> movements = stockMovementService.getAllStockMovements();
        return ResponseEntity.ok(new APIResponse<>(HttpStatus.OK.value(), "Stock movements fetched successfully", movements));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<APIResponse<StockMovementDto>> getStockMovementById(@PathVariable Long id) {
        log.info("Fetching stock movement with id: {}", id);
        StockMovementDto movement = stockMovementService.getStockMovementById(id);
        return ResponseEntity.ok(new APIResponse<>(HttpStatus.OK.value(), "Stock movement fetched successfully", movement));
    }

    @GetMapping("/item/{inventoryItemId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<APIResponse<List<StockMovementDto>>> getMovementsByInventoryItemId(@PathVariable Long inventoryItemId) {
        log.info("Fetching stock movements for inventory item: {}", inventoryItemId);
        List<StockMovementDto> movements = stockMovementService.getMovementsByInventoryItemId(inventoryItemId);
        return ResponseEntity.ok(new APIResponse<>(HttpStatus.OK.value(), "Stock movements fetched successfully", movements));
    }

    @GetMapping("/date-range")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<APIResponse<List<StockMovementDto>>> getMovementsBetweenDates(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        log.info("Fetching stock movements between {} and {}", startDate, endDate);
        List<StockMovementDto> movements = stockMovementService.getMovementsBetweenDates(startDate, endDate);
        return ResponseEntity.ok(new APIResponse<>(HttpStatus.OK.value(), "Stock movements fetched successfully", movements));
    }

    @GetMapping("/pending-approvals")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse<List<StockMovementDto>>> getPendingApprovals() {
        log.info("Fetching pending movement approvals");
        List<StockMovementDto> movements = stockMovementService.getPendingApprovals();
        return ResponseEntity.ok(new APIResponse<>(HttpStatus.OK.value(), "Pending approvals fetched successfully", movements));
    }

    @PutMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse<StockMovementDto>> approveMovement(@PathVariable Long id, @RequestParam String approvedBy) {
        log.info("Approving stock movement with id: {} by {}", id, approvedBy);
        StockMovementDto approvedMovement = stockMovementService.approveMovement(id, approvedBy);
        return ResponseEntity.ok(new APIResponse<>(HttpStatus.OK.value(), "Stock movement approved successfully", approvedMovement));
    }

    @PutMapping("/{id}/reverse")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse<StockMovementDto>> reverseMovement(@PathVariable Long id, @RequestParam String reversedBy, @RequestParam String reason) {
        log.info("Reversing stock movement with id: {} by {}", id, reversedBy);
        StockMovementDto reversedMovement = stockMovementService.reverseMovement(id, reversedBy, reason);
        return ResponseEntity.ok(new APIResponse<>(HttpStatus.OK.value(), "Stock movement reversed successfully", reversedMovement));
    }
}
