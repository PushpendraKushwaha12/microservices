package com.inventory_service.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.inventory_service.enums.MovementType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockMovementDto {

    @NotNull(message = "Inventory item ID is required")
    private Long inventoryItemId;

    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotBlank(message = "Product SKU is required")
    @Size(max = 50, message = "SKU cannot exceed 50 characters")
    private String productSku;

    @NotNull(message = "Movement type is required")
    private MovementType movementType;

    @Positive(message = "Quantity must be greater than 0")
    private int quantity;

    @Size(max = 200, message = "Reason cannot exceed 200 characters")
    private String reason;

    @Size(max = 500, message = "Notes cannot exceed 500 characters")
    private String notes;
}
