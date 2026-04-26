package com.inventory_service.enums;

public enum InventoryStatus {
    AVAILABLE("Available"),
    RESERVED("Reserved"),
    ALLOCATED("Allocated"),
    DAMAGED("Damaged"),
    EXPIRED("Expired"),
    QUARANTINE("Quarantine"),
    OUT_OF_STOCK("Out of Stock");

    private final String displayName;

    InventoryStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
