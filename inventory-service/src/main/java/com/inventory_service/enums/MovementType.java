package com.inventory_service.enums;

public enum MovementType {
    INBOUND("Inbound"),
    OUTBOUND("Outbound"),
    ADJUSTMENT("Adjustment"),
    RETURN("Return"),
    TRANSFER("Transfer");

    private final String displayName;

    MovementType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
