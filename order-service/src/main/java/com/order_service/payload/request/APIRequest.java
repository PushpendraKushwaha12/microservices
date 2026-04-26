package com.order_service.payload.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class APIRequest<T> {
    @NotNull(message = "Request body cannot be null")
    private T data;
}
