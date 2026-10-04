package com.example.course_service.exception;

public class InactiveResourceException extends RuntimeException{
    public InactiveResourceException(String message) {
        super(message);
    }
}
