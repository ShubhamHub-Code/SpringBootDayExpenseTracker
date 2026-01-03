package com.fullstack.advice;

import com.fullstack.exception.EmailAlreadyExistsException;
import com.fullstack.exception.InvalidCredentialsException;
import com.fullstack.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<?> handleEmailAlreadyExistsException(EmailAlreadyExistsException emailAlreadyExistsException) {
        return new ResponseEntity<>(emailAlreadyExistsException.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleCustomValidation(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new LinkedHashMap<>();

        exception.getBindingResult().getAllErrors().forEach(objectError -> {
            String field = ((FieldError) objectError).getField();
            String fieldMessage = objectError.getDefaultMessage();

            errors.put(field, fieldMessage);
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException userNotFoundException) {
        return new ResponseEntity<>(userNotFoundException.getMessage(), HttpStatus.NOT_FOUND);
    }

    //InvalidCredentialsException
    @ExceptionHandler
    public ResponseEntity<?> handleInvalidCredentialsException(InvalidCredentialsException invalidCredentialsException) {
        return new ResponseEntity<>(invalidCredentialsException.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
