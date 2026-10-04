package com.example.batch_service.exception;

public class BatchCodeAlreadyExistException extends RuntimeException{

    public BatchCodeAlreadyExistException(String message) {
        super(message);
    }
}
