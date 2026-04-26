package com.product_service.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductException extends RuntimeException {
    private int statusCode;

    public ProductException(String message) {
        super(message);
        this.statusCode = 400;
    }

    public ProductException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public ProductException(String message, Throwable cause) {
        super(message, cause);
        this.statusCode = 500;
    }
}
