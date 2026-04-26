package com.product_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.product_service.enums.ProductStatus;
import com.product_service.enums.ProductCategory;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Product name is required")
    @Size(min = 2, max = 255, message = "Product name must be between 2 and 255 characters")
    private String productName;

    @NotBlank(message = "SKU is required")
    @Size(min = 3, max = 50, message = "SKU must be between 3 and 50 characters")
    private String sku;

    @NotBlank(message = "Product description is required")
    @Size(max = 2000, message = "Description cannot exceed 2000 characters")
    private String productDescription;

    @NotNull(message = "Category is required")
    @Enumerated(EnumType.STRING)
    private ProductCategory category;

    @NotNull(message = "Base price is required")
    @DecimalMin(value = "0.01", message = "Base price must be greater than 0")
    private BigDecimal basePrice;

    @DecimalMin(value = "0.0", message = "Sale price cannot be negative")
    private BigDecimal salePrice;

    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    @Min(value = 0, message = "Stock quantity cannot be negative")
    private int stockQuantity;

    @Min(value = 0, message = "Reorder level cannot be negative")
    private int reorderLevel;

    @NotBlank(message = "Brand is required")
    @Size(max = 100, message = "Brand name cannot exceed 100 characters")
    private String brand;

    @Size(max = 500, message = "Tags cannot exceed 500 characters")
    private String tags;

    @DecimalMin(value = "0.0", message = "Weight cannot be negative")
    private BigDecimal weight;

    @Size(max = 50, message = "Weight unit cannot exceed 50 characters")
    private String weightUnit;

    @Size(max = 255, message = "Dimensions cannot exceed 255 characters")
    private String dimensions;

    @Size(max = 100, message = "Color cannot exceed 100 characters")
    private String color;

    @Size(max = 100, message = "Size cannot exceed 100 characters")
    private String size;

    @Size(max = 100, message = "Material cannot exceed 100 characters")
    private String material;

    @Size(max = 500, message = "Warranty information cannot exceed 500 characters")
    private String warrantyInfo;

    @Size(max = 1000, message = "Care instructions cannot exceed 1000 characters")
    private String careInstructions;

    private LocalDateTime createdDate;

    private LocalDateTime lastUpdatedDate;

    @Size(max = 100, message = "Created by cannot exceed 100 characters")
    private String createdBy;

    @Size(max = 100, message = "Updated by cannot exceed 100 characters")
    private String updatedBy;

    private boolean isActive;

    private boolean isFeatured;

    @Min(value = 0, message = "Rating cannot be negative")
    @Max(value = 5, message = "Rating cannot exceed 5")
    private double averageRating;

    @Min(value = 0, message = "Review count cannot be negative")
    private int reviewCount;

    @Size(max = 1000, message = "SEO keywords cannot exceed 1000 characters")
    private String seoKeywords;

    @Size(max = 500, message = "Meta description cannot exceed 500 characters")
    private String metaDescription;

    @Size(max = 255, message = "Supplier name cannot exceed 255 characters")
    private String supplierName;

    @Size(max = 50, message = "Supplier code cannot exceed 50 characters")
    private String supplierCode;

    @DecimalMin(value = "0.0", message = "Cost price cannot be negative")
    private BigDecimal costPrice;

    @DecimalMin(value = "0.0", message = "Tax rate cannot be negative")
    @Max(value = 100, message = "Tax rate cannot exceed 100%")
    private BigDecimal taxRate;

    @Size(max = 100, message = "Barcode cannot exceed 100 characters")
    private String barcode;

    @Size(max = 100, message = "UPC cannot exceed 100 characters")
    private String upc;

    @Size(max = 1000, message = "Additional specifications cannot exceed 1000 characters")
    private String additionalSpecs;
}
