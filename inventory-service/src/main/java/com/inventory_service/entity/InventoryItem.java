package com.inventory_service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.inventory_service.enums.InventoryStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_items")
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class InventoryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotBlank(message = "Product SKU is required")
    @Size(max = 50, message = "Product SKU cannot exceed 50 characters")
    private String productSku;

    @NotBlank(message = "Product name is required")
    @Size(max = 255, message = "Product name cannot exceed 255 characters")
    private String productName;

    @NotNull(message = "Warehouse ID is required")
    private Long warehouseId;

    @NotBlank(message = "Warehouse name is required")
    @Size(max = 100, message = "Warehouse name cannot exceed 100 characters")
    private String warehouseName;

    @NotBlank(message = "Location code is required")
    @Size(max = 50, message = "Location code cannot exceed 50 characters")
    private String locationCode;

    @NotBlank(message = "Batch number is required")
    @Size(max = 50, message = "Batch number cannot exceed 50 characters")
    private String batchNumber;

    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity cannot be negative")
    private int quantity;

    @Min(value = 0, message = "Reserved quantity cannot be negative")
    private int reservedQuantity;

    @Min(value = 0, message = "Allocated quantity cannot be negative")
    private int allocatedQuantity;

    @NotNull(message = "Available quantity is required")
    @Min(value = 0, message = "Available quantity cannot be negative")
    private int availableQuantity;

    @NotNull(message = "Unit cost is required")
    @DecimalMin(value = "0.0", message = "Unit cost cannot be negative")
    private BigDecimal unitCost;

    @DecimalMin(value = "0.0", message = "Total value cannot be negative")
    private BigDecimal totalValue;

    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    private InventoryStatus status;

    @Size(max = 20, message = "Serial number cannot exceed 20 characters")
    private String serialNumber;

    @Size(max = 100, message = "Lot number cannot exceed 100 characters")
    private String lotNumber;

    private LocalDateTime expiryDate;

    private LocalDateTime manufacturedDate;

    private LocalDateTime receivedDate;

    @Size(max = 100, message = "Supplier name cannot exceed 100 characters")
    private String supplierName;

    @Size(max = 50, message = "Supplier batch cannot exceed 50 characters")
    private String supplierBatch;

    @Size(max = 100, message = "Item condition cannot exceed 100 characters")
    private String itemCondition;

    @Size(max = 500, message = "Quality notes cannot exceed 500 characters")
    private String qualityNotes;

    @Min(value = 0, message = "Reorder point cannot be negative")
    private int reorderPoint;

    @Min(value = 0, message = "Maximum stock level cannot be negative")
    private int maxStockLevel;

    @DecimalMin(value = "0.0", message = "Minimum order quantity cannot be negative")
    private BigDecimal minOrderQuantity;

    @Size(max = 50, message = "Unit of measure cannot exceed 50 characters")
    private String unitOfMeasure;

    @DecimalMin(value = "0.0", message = "Weight cannot be negative")
    private BigDecimal weight;

    @Size(max = 20, message = "Weight unit cannot exceed 20 characters")
    private String weightUnit;

    @Size(max = 100, message = "Dimensions cannot exceed 100 characters")
    private String dimensions;

    @Size(max = 50, message = "Storage requirements cannot exceed 50 characters")
    private String storageRequirements;

    @Size(max = 100, message = "Handling instructions cannot exceed 100 characters")
    private String handlingInstructions;

    private LocalDateTime lastCountDate;

    @Size(max = 100, message = "Last counted by cannot exceed 100 characters")
    private String lastCountedBy;

    private LocalDateTime lastMovementDate;

    @Size(max = 100, message = "Last moved by cannot exceed 100 characters")
    private String lastMovedBy;

    private LocalDateTime createdDate;

    private LocalDateTime lastUpdatedDate;

    @Size(max = 100, message = "Created by cannot exceed 100 characters")
    private String createdBy;

    @Size(max = 100, message = "Updated by cannot exceed 100 characters")
    private String updatedBy;

    private boolean isActive;

    @Size(max = 500, message = "Notes cannot exceed 500 characters")
    private String notes;

    @Size(max = 100, message = "Barcode cannot exceed 100 characters")
    private String barcode;

    @Size(max = 100, message = "RFID tag cannot exceed 100 characters")
    private String rfidTag;

    @Size(max = 50, message = "Zone cannot exceed 50 characters")
    private String zone;

    @Size(max = 50, message = "Aisle cannot exceed 50 characters")
    private String aisle;

    @Size(max = 50, message = "Shelf cannot exceed 50 characters")
    private String shelf;

    @Size(max = 50, message = "Bin cannot exceed 50 characters")
    private String bin;

    @Min(value = 0, message = "Shelf life days cannot be negative")
    private int shelfLifeDays;

    @DecimalMin(value = "0.0", message = "Temperature requirement cannot be negative")
    private BigDecimal temperatureRequirement;

    @DecimalMin(value = "0.0", message = "Humidity requirement cannot be negative")
    private BigDecimal humidityRequirement;

    @Size(max = 100, message = "Custom field 1 cannot exceed 100 characters")
    private String customField1;

    @Size(max = 100, message = "Custom field 2 cannot exceed 100 characters")
    private String customField2;

    @Size(max = 100, message = "Custom field 3 cannot exceed 100 characters")
    private String customField3;
}
