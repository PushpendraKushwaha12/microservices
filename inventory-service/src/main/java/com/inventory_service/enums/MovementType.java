package com.inventory_service.enums;

import lombok.Getter;

@Getter
public enum MovementType {
    IN("In"),
    OUTBOUND("Outbound"),
    ADJUSTMENT("Adjustment"),
    RETURN("Return"),
    TRANSFER("Transfer");

    private final String displayName;

    MovementType(String displayName) {
        this.displayName = displayName;
    }

}
