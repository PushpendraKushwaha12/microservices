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

    private Long id;

    @NotNull(message = "Inventory item ID is required")
    private Long inventoryItemId;

    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotBlank(message = "Product SKU is required")
    @Size(max = 50, message = "Product SKU cannot exceed 50 characters")
    private String productSku;

    @NotNull(message = "Movement type is required")
    private MovementType movementType;

    @NotNull(message = "Quantity is required")
    private int quantity;

    @DecimalMin(value = "0.0", message = "Unit cost cannot be negative")
    private BigDecimal unitCost;

    @DecimalMin(value = "0.0", message = "Total value cannot be negative")
    private BigDecimal totalValue;

    @NotNull(message = "Movement date is required")
    private LocalDateTime movementDate;

    @Size(max = 100, message = "Reference number cannot exceed 100 characters")
    private String referenceNumber;

    @Size(max = 50, message = "Order number cannot exceed 50 characters")
    private String orderNumber;

    @Size(max = 50, message = "Invoice number cannot exceed 50 characters")
    private String invoiceNumber;

    @Size(max = 100, message = "Supplier name cannot exceed 100 characters")
    private String supplierName;

    @Size(max = 100, message = "Customer name cannot exceed 100 characters")
    private String customerName;

    @Size(max = 100, message = "From location cannot exceed 100 characters")
    private String fromLocation;

    @Size(max = 100, message = "To location cannot exceed 100 characters")
    private String toLocation;

    @Size(max = 100, message = "Reason cannot exceed 100 characters")
    private String reason;

    @Size(max = 500, message = "Notes cannot exceed 500 characters")
    private String notes;

    @Size(max = 100, message = "Performed by cannot exceed 100 characters")
    private String performedBy;

    @Size(max = 50, message = "Batch number cannot exceed 50 characters")
    private String batchNumber;

    @Size(max = 20, message = "Serial number cannot exceed 20 characters")
    private String serialNumber;

    private LocalDateTime expiryDate;

    @Size(max = 100, message = "Quality status cannot exceed 100 characters")
    private String qualityStatus;

    @Size(max = 100, message = "Approval status cannot exceed 100 characters")
    private String approvalStatus;

    @Size(max = 100, message = "Approved by cannot exceed 100 characters")
    private String approvedBy;

    private LocalDateTime approvedDate;

    private LocalDateTime createdDate;

    @Size(max = 100, message = "Created by cannot exceed 100 characters")
    private String createdBy;

    @Size(max = 100, message = "Source system cannot exceed 100 characters")
    private String sourceSystem;

    @Size(max = 100, message = "Transaction ID cannot exceed 100 characters")
    private String transactionId;

    private boolean isReversed;

    @Size(max = 100, message = "Reversed by cannot exceed 100 characters")
    private String reversedBy;

    private LocalDateTime reversedDate;

    @Size(max = 500, message = "Reversal reason cannot exceed 500 characters")
    private String reversalReason;

    @Size(max = 100, message = "Custom field 1 cannot exceed 100 characters")
    private String customField1;

    @Size(max = 100, message = "Custom field 2 cannot exceed 100 characters")
    private String customField2;

    @Size(max = 100, message = "Custom field 3 cannot exceed 100 characters")
    private String customField3;
}
