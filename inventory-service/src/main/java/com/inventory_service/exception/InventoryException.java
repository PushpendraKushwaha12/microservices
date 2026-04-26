package com.inventory_service.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryException extends RuntimeException {
    private int statusCode;

    public InventoryException(String message) {
        super(message);
        this.statusCode = 400;
    }

    public InventoryException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public InventoryException(String message, Throwable cause) {
        super(message, cause);
        this.statusCode = 500;
    }
}
