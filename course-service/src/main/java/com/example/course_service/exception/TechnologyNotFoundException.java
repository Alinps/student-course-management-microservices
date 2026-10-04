package com.example.course_service.exception;

public class TechnologyNotFoundException extends RuntimeException{
    public TechnologyNotFoundException(String message) {
        super(message);
    }
}
