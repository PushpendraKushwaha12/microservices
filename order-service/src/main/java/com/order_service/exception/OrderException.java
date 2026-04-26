package com.order_service.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderException extends RuntimeException {
    private int statusCode;

    public OrderException(String message) {
        super(message);
        this.statusCode = 400;
    }

    public OrderException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public OrderException(String message, Throwable cause) {
        super(message, cause);
        this.statusCode = 500;
    }
}
