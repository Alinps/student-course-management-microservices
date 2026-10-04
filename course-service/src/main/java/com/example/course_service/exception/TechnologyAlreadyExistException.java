package com.example.course_service.exception;

public class TechnologyAlreadyExistException extends RuntimeException{
    public TechnologyAlreadyExistException(String message) {
        super(message);
    }
}
