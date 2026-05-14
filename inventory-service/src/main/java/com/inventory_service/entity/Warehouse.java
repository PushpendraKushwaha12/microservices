package com.inventory_service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "warehouses")
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class Warehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double humidityMax;

    private boolean isActive;

    private boolean isDefault;

    private LocalDateTime createdDate;

    private LocalDateTime lastUpdatedDate;

    private String createdBy;

    private String customField1;

    private String customField2;

    private String warehouseCode;

    private String warehouseName;

    private String customField3;
}
