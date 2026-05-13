package com.inventory_service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.inventory_service.enums.MovementType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "stock_movements")
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class StockMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long inventoryItemId;

    private Long productId;

    private String productSku;

    @Enumerated(EnumType.STRING)
    private MovementType movementType;

    private int quantity;

    private String reason;

    private String notes;

    private LocalDateTime movementDate;

    private LocalDateTime createdDate;

    private boolean reversed;

    private String reversedBy;

    private LocalDateTime reversedDate;

    private String reversalReason;

    private BigDecimal unitCost;

    private BigDecimal totalValue;

    private String approvalStatus;

    private String approvedBy;

    private LocalDateTime approvedDate;

    @PrePersist
    public void prePersist() {
        this.movementDate = LocalDateTime.now();
    }
}
