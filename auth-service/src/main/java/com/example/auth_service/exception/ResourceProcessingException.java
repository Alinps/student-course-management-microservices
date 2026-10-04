package com.example.auth_service.exception;

public class ResourceProcessingException extends RuntimeException {
    public ResourceProcessingException(String message,Throwable cause) {
        super(message,cause);
    }
}
