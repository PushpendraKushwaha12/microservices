package com.inventory_service.entity;

import jakarta.persistence.*;
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

    private Long productId;

    private String productName;

    private String productSku;

    private Long warehouseId;

    private String warehouseName;

    private int quantity;

    private int reservedQuantity;

    private int allocatedQuantity;

    private int availableQuantity;

    private BigDecimal unitCost;

    private BigDecimal totalValue;

    @Enumerated(EnumType.STRING)
    private InventoryStatus status;

    private int reorderPoint;

    private LocalDateTime createdDate;

    private LocalDateTime lastUpdatedDate;

    private String createdBy;

    private boolean isActive;

}
