package com.user_service.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserException extends RuntimeException {
    private int statusCode;

    public UserException(String message) {
        super(message);
        this.statusCode = 400;
    }

    public UserException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public UserException(String message, Throwable cause) {
        super(message, cause);
        this.statusCode = 500;
    }
}
