package com.example.batch_service.exception;

public class BatchNotFoundException extends  RuntimeException{

    public BatchNotFoundException(String message) {
        super(message);
    }
}
