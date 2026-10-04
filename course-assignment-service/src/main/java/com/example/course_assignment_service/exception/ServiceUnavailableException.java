package com.example.course_assignment_service.exception;

public class ServiceUnavailableException extends  RuntimeException{
    public ServiceUnavailableException(String message) {
        super(message);
    }
}
