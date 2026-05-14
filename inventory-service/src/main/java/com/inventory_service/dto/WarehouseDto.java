package com.inventory_service.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WarehouseDto {

    @DecimalMin(value = "0.0", message = "Humidity max cannot be negative")
    @DecimalMax(value = "100.0", message = "Humidity max cannot exceed 100")
    private Double humidityMax;

    @NotNull(message = "Active status is required")
    private Boolean isActive;

    private String warehouseName;

    @NotNull(message = "Default status is required")
    private Boolean isDefault;

    private LocalDateTime createdDate;

    private LocalDateTime lastUpdatedDate;

    @NotBlank(message = "Created by is required")
    @Size(min = 2, max = 100, message = "Created by must be between 2 and 100 characters")
    private String createdBy;

    @Size(max = 100, message = "Custom field 1 cannot exceed 100 characters")
    private String customField1;

    @Size(max = 100, message = "Custom field 2 cannot exceed 100 characters")
    private String customField2;

    @NotBlank(message = "Warehouse code is required")
    @Size(min = 2, max = 20, message = "Warehouse code must be between 2 and 20 characters")
    private String warehouseCode;

    @Size(max = 100, message = "Custom field 3 cannot exceed 100 characters")
    private String customField3;
}