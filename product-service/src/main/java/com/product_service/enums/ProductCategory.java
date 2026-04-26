package com.product_service.enums;

import lombok.Getter;

@Getter
public enum ProductCategory {
    ELECTRONICS("Electronics"),
    CLOTHING("Clothing"),
    HOME_AND_GARDEN("Home & Garden"),
    SPORTS_AND_OUTDOORS("Sports & Outdoors"),
    BOOKS_AND_MEDIA("Books & Media"),
    HEALTH_AND_BEAUTY("Health & Beauty"),
    TOYS_AND_GAMES("Toys & Games"),
    AUTOMOTIVE("Automotive"),
    OFFICE_SUPPLIES("Office Supplies"),
    PET_SUPPLIES("Pet Supplies");

    private final String displayName;

    ProductCategory(String displayName) {
        this.displayName = displayName;
    }

}
