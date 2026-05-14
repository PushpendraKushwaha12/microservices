package com.product_service.entity;

import com.product_service.enums.ProductCategory;
import com.product_service.enums.ProductStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productName;

    @Column(unique = true, nullable = false)
    private String sku;

    @Column(length = 2000)
    private String productDescription;

    @Enumerated(EnumType.STRING)
    private ProductCategory category;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    private Boolean active;

    private String manufacturerName;

    private String countryOfOrigin;

    private LocalDateTime expiryDate;

    private Boolean returnable;

    private Integer minimumOrderQuantity;

    private String shippingClass;

    private LocalDateTime createdDate;

    private LocalDateTime lastUpdatedDate;
}