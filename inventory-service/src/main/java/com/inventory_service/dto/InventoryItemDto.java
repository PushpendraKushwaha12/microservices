package com.inventory_service.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.inventory_service.enums.InventoryStatus;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InventoryItemDto {

    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotBlank(message = "Product name is required")
    private String productName;

    @NotBlank(message = "Product SKU is required")
    private String productSku;

    @NotNull(message = "Warehouse ID is required")
    private Long warehouseId;

    @NotBlank(message = "Warehouse name is required")
    private String warehouseName;

    @Min(value = 0, message = "Quantity cannot be negative")
    private int quantity;

    @Min(value = 0, message = "Reserved quantity cannot be negative")
    private int reservedQuantity;

    @Min(value = 0, message = "Available quantity cannot be negative")
    private int availableQuantity;

    @DecimalMin(value = "0.0", inclusive = false, message = "Unit cost must be greater than 0")
    private BigDecimal unitCost;

    @NotNull(message = "Status is required")
    private InventoryStatus status;

    @Min(value = 0, message = "Reorder point cannot be negative")
    private int reorderPoint;

    @NotBlank(message = "Created by is required")
    private String createdBy;
}
