package com.inventory_service.enums;

import lombok.Getter;

@Getter
public enum InventoryStatus {
    AVAILABLE("Available");

    private final String displayName;

    InventoryStatus(String displayName) {
        this.displayName = displayName;
    }

}
