package com.shopsphere.exceptions;

import org.jetbrains.annotations.NotNull;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.rememberme.InvalidCookieException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.naming.InsufficientResourcesException;
import java.util.HashMap;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrityViolationException(@NotNull DataIntegrityViolationException e) {
        final String message = e.getMessage();
        return ResponseEntity.badRequest().body(message);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<HashMap<String, String>> handleMethodArgumentNotValidException(@NotNull MethodArgumentNotValidException e) {
        final HashMap<String, String> errorMap = new HashMap<>();

        e.getBindingResult().getAllErrors().forEach(err -> {
            String fieldName = ((FieldError) err).getField();
            String message = err.getDefaultMessage();
            errorMap.put(fieldName, message);
        });

        return ResponseEntity.badRequest().body(errorMap);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(@NotNull ResourceNotFoundException e) {
        final String message = e.getMessage();
        return ResponseEntity.badRequest().body(message);
    }

    @ExceptionHandler(InsufficientResourcesException.class)
    public ResponseEntity<String> handleInsufficientResourcesException(@NotNull InsufficientResourcesException e) {
        final String message = e.getMessage();
        return ResponseEntity.badRequest().body(message);
    }

    @ExceptionHandler(InvalidJwtException.class)
    public ResponseEntity<String> handleInvalidJwtException(@NotNull InvalidJwtException e) {
        final String message = e.getMessage();
        return ResponseEntity.badRequest().body(message);
    }

    @ExceptionHandler(InvalidCookieException.class)
    public ResponseEntity<String> handleInvalidCookieExceptionException(@NotNull InvalidCookieException e) {
        final String message = e.getMessage();
        return ResponseEntity.badRequest().body(message);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<String> handleInvalidCredentialsException(@NotNull InvalidCredentialsException e) {
        final String message = e.getMessage();
        return ResponseEntity.badRequest().body(message);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> handleAuthenticationException(@NotNull AuthenticationException e) {
        final String message = e.getMessage();
        return ResponseEntity.badRequest().body(message);
    }
}
