package com.n3.backend.config;

import com.n3.backend.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ApiResponse handleMethodNotSupported(HttpRequestMethodNotSupportedException e){
        return new ApiResponse<>(false, HttpStatus.METHOD_NOT_ALLOWED.value(), null, "Method " + e.getMethod() + " not supported for this endpoint.");
    }
}
