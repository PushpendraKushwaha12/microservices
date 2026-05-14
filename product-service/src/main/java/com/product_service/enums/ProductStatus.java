package com.product_service.enums;

import lombok.Getter;

@Getter
public enum ProductStatus {
    ACTIVE("Active");
    private final String displayName;

    ProductStatus(String displayName) {
        this.displayName = displayName;
    }

}
