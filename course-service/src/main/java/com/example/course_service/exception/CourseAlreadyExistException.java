package com.example.course_service.exception;

public class CourseAlreadyExistException extends  RuntimeException{
    public CourseAlreadyExistException(String message) {
        super(message);
    }
}
