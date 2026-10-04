package com.example.student_batch_assignment_service.exception;

public class ServiceUnavailableException extends RuntimeException {
    public ServiceUnavailableException(String message){
        super(message);
    }
}
