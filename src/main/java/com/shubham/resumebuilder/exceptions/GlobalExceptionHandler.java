package com.shubham.resumebuilder.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException exception){
        Map<String , String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach(error -> {
            String filedName = ((FieldError)error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(filedName, errorMessage);
        });

        Map<String, Object>response = new HashMap<>();
        response.put("message", "Validation Failed");
        response.put("errors", errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(ResourceExistsException.class)
    public ResponseEntity<Map<String, Object>> handleResourceExistsException(ResourceExistsException ex){
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Resource Exists");
        response.put("error", ex.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenricException(Exception ex){
        Map<String, Object> response = new HashMap<>();

        response.put("message", "Something went wrong");
        response.put("error", ex.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

}
