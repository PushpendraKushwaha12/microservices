package com.inventory_service.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WarehouseDto {

    private Long id;

    @NotBlank(message = "Warehouse code is required")
    @Size(min = 2, max = 20, message = "Warehouse code must be between 2 and 20 characters")
    private String warehouseCode;

    @NotBlank(message = "Warehouse name is required")
    @Size(min = 2, max = 100, message = "Warehouse name must be between 2 and 100 characters")
    private String warehouseName;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @NotBlank(message = "Address line 1 is required")
    @Size(max = 255, message = "Address line 1 cannot exceed 255 characters")
    private String addressLine1;

    @Size(max = 255, message = "Address line 2 cannot exceed 255 characters")
    private String addressLine2;

    @NotBlank(message = "City is required")
    @Size(max = 100, message = "City cannot exceed 100 characters")
    private String city;

    @NotBlank(message = "State/Province is required")
    @Size(max = 100, message = "State/Province cannot exceed 100 characters")
    private String stateProvince;

    @NotBlank(message = "Postal code is required")
    @Size(max = 20, message = "Postal code cannot exceed 20 characters")
    private String postalCode;

    @NotBlank(message = "Country is required")
    @Size(max = 100, message = "Country cannot exceed 100 characters")
    private String country;

    @Size(max = 20, message = "Phone number cannot exceed 20 characters")
    private String phoneNumber;

    @Size(max = 100, message = "Email cannot exceed 100 characters")
    private String email;

    @Size(max = 100, message = "Manager name cannot exceed 100 characters")
    private String managerName;

    @Size(max = 20, message = "Manager phone cannot exceed 20 characters")
    private String managerPhone;

    @DecimalMin(value = "0.0", message = "Total capacity cannot be negative")
    private Double totalCapacitySqFt;

    @DecimalMin(value = "0.0", message = "Used capacity cannot be negative")
    private Double usedCapacitySqFt;

    @Min(value = 0, message = "Total racks cannot be negative")
    private int totalRacks;

    @Min(value = 0, message = "Total shelves cannot be negative")
    private int totalShelves;

    @Size(max = 50, message = "Warehouse type cannot exceed 50 characters")
    private String warehouseType;

    @Size(max = 50, message = "Operating hours cannot exceed 50 characters")
    private String operatingHours;

    @Size(max = 100, message = "Security level cannot exceed 100 characters")
    private String securityLevel;

    @Size(max = 100, message = "Climate control cannot exceed 100 characters")
    private String climateControl;

    @DecimalMin(value = "0.0", message = "Temperature min cannot be negative")
    private Double temperatureMin;

    @DecimalMin(value = "0.0", message = "Temperature max cannot be negative")
    private Double temperatureMax;

    @DecimalMin(value = "0.0", message = "Humidity min cannot be negative")
    private Double humidityMin;

    @DecimalMin(value = "0.0", message = "Humidity max cannot be negative")
    private Double humidityMax;

    private boolean isActive;

    private boolean isDefault;

    private LocalDateTime createdDate;

    private LocalDateTime lastUpdatedDate;

    @Size(max = 100, message = "Created by cannot exceed 100 characters")
    private String createdBy;

    @Size(max = 100, message = "Updated by cannot exceed 100 characters")
    private String updatedBy;

    @Size(max = 500, message = "Notes cannot exceed 500 characters")
    private String notes;

    @Size(max = 100, message = "Timezone cannot exceed 100 characters")
    private String timezone;

    @Size(max = 100, message = "Region cannot exceed 100 characters")
    private String region;

    @Size(max = 100, message = "Zone cannot exceed 100 characters")
    private String zone;

    @DecimalMin(value = "0.0", message = "Latitude cannot be negative")
    private Double latitude;

    @DecimalMin(value = "0.0", message = "Longitude cannot be negative")
    private Double longitude;

    @Size(max = 100, message = "Custom field 1 cannot exceed 100 characters")
    private String customField1;

    @Size(max = 100, message = "Custom field 2 cannot exceed 100 characters")
    private String customField2;

    @Size(max = 100, message = "Custom field 3 cannot exceed 100 characters")
    private String customField3;
}
