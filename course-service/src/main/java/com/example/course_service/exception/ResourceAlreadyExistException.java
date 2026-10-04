package com.example.course_service.exception;

public class ResourceAlreadyExistException extends  RuntimeException{

    public ResourceAlreadyExistException(String message) {
        super(message);
    }
}
