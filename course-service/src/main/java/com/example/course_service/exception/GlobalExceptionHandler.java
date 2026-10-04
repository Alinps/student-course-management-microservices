package com.example.course_service.exception;

import com.example.course_service.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationError(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(
                                error.getField(),
                                error.getDefaultMessage()
                        ));
        return ResponseEntity.badRequest().body(errors);
    }

//    @ExceptionHandler(CourseAlreadyExistException.class)
//    public ResponseEntity<ErrorResponse> handleCourseAlreadyExist(
//            CourseAlreadyExistException ex,
//            HttpServletRequest request) {
//
//        ErrorResponse error = new ErrorResponse(
//                LocalDateTime.now(),
//                HttpStatus.CONFLICT.value(), //returns the numeric status code:
//                HttpStatus.CONFLICT.getReasonPhrase(), //returns the standard HTTP description eg: Not Found
//                ex.getMessage(),
//                request.getRequestURI() //Returns the requested path eg: /api/batches/1
//        );
//
//        return ResponseEntity
//                .status(HttpStatus.CONFLICT)
//                .body(error);
//    }

//    @ExceptionHandler(CourseNotFoundException.class)
//    public ResponseEntity<ErrorResponse> handleCourseNotFound(
//            CourseNotFoundException ex,
//            HttpServletRequest request) {
//
//        ErrorResponse error = new ErrorResponse(
//                LocalDateTime.now(),
//                HttpStatus.NOT_FOUND.value(), //returns the numeric status code:
//                HttpStatus.NOT_FOUND.getReasonPhrase(), //returns the standard HTTP description eg: Not Found
//                ex.getMessage(),
//                request.getRequestURI() //Returns the requested path eg: /api/batches/1
//        );
//
//        return ResponseEntity
//                .status(HttpStatus.NOT_FOUND)
//                .body(error);
//    }


    @ExceptionHandler(IncorrectTechnologyException.class)
    public ResponseEntity<ErrorResponse> handleIncorrectTechnology(
            IncorrectTechnologyException ex,
            HttpServletRequest request) {

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(), //returns the numeric status code:
                HttpStatus.BAD_REQUEST.getReasonPhrase(), //returns the standard HTTP description eg: Not Found
                ex.getMessage(),
                request.getRequestURI() //Returns the requested path eg: /api/batches/1
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    @ExceptionHandler(ResourceAlreadyExistException.class)
    public ResponseEntity<ErrorResponse> handleResourceAlreadyExist(
            ResourceAlreadyExistException ex,
            HttpServletRequest request) {

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(), //returns the numeric status code:
                HttpStatus.CONFLICT.getReasonPhrase(), //returns the standard HTTP description eg: Not Found
                ex.getMessage(),
                request.getRequestURI() //Returns the requested path eg: /api/batches/1
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(error);
    }

//    @ExceptionHandler(TechnologyNotFoundException.class)
//    public ResponseEntity<ErrorResponse> handleTechnologyNotFound(
//            TechnologyNotFoundException ex,
//            HttpServletRequest request) {
//
//        ErrorResponse error = new ErrorResponse(
//                LocalDateTime.now(),
//                HttpStatus.NOT_FOUND.value(), //returns the numeric status code:
//                HttpStatus.NOT_FOUND.getReasonPhrase(), //returns the standard HTTP description eg: Not Found
//                ex.getMessage(),
//                request.getRequestURI() //Returns the requested path eg: /api/batches/1
//        );
//        return ResponseEntity
//                .status(HttpStatus.NOT_FOUND)
//                .body(error);
//    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(
            ResourceNotFoundException ex,
            HttpServletRequest request
    ) {

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(), //returns the numeric status code:
                HttpStatus.NOT_FOUND.getReasonPhrase(), //returns the standard HTTP description eg: Not Found
                ex.getMessage(),
                request.getRequestURI() //Returns the requested path eg: /api/batches/1
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }

    @ExceptionHandler(InactiveResourceException.class)
    public ResponseEntity<ErrorResponse> handleInactiveResource(
            InactiveResourceException ex,
            HttpServletRequest request) {

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(), //returns the numeric status code:
                HttpStatus.BAD_REQUEST.getReasonPhrase(), //returns the standard HTTP description eg: Not Found
                ex.getMessage(),
                request.getRequestURI() //Returns the requested path eg: /api/batches/1
        );


        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }
}
