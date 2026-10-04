package com.example.batch_service.exception;

public class BatchNameAlreadyExistException extends  RuntimeException{
    public BatchNameAlreadyExistException(String message) {
        super(message);
    }
}
