package com.product_service.dto;

import com.product_service.enums.ProductCategory;
import com.product_service.enums.ProductStatus;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProductDto {

    @NotBlank(message = "Product name is required")
    @Size(min = 2, max = 255, message = "Product name must be between 2 and 255 characters")
    private String productName;

    @NotBlank(message = "SKU is required")
    @Size(min = 3, max = 100, message = "SKU must be between 3 and 100 characters")
    private String sku;

    @NotBlank(message = "Product description is required")
    @Size(max = 2000, message = "Description cannot exceed 2000 characters")
    private String productDescription;

    @NotNull(message = "Category is required")
    private ProductCategory category;

    private ProductStatus status;

    private Boolean active;

    @NotBlank(message = "Manufacturer name is required")
    @Size(max = 255, message = "Manufacturer name cannot exceed 255 characters")
    private String manufacturerName;

    @NotBlank(message = "Country of origin is required")
    @Size(max = 100, message = "Country name cannot exceed 100 characters")
    private String countryOfOrigin;

    @Future(message = "Expiry date must be in future")
    private LocalDateTime expiryDate;

    @NotNull(message = "Returnable field is required")
    private Boolean returnable;

    @NotNull(message = "Minimum order quantity is required")
    @Min(value = 1, message = "Minimum order quantity must be at least 1")
    private Integer minimumOrderQuantity;

    @NotBlank(message = "Shipping class is required")
    @Size(max = 100, message = "Shipping class cannot exceed 100 characters")
    private String shippingClass;
}