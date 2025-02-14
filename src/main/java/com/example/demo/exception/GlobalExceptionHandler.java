package com.example.demo.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.internalServerError;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return badRequest().body(errors);
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<String> handleGlobalException(Exception exception) {
        log.error("Exception: ", exception);
        return internalServerError().body(exception.getMessage());
    }

    @ExceptionHandler(ResourceNotFound.class)
    ResponseEntity<ErrorDto> handelResourceNotFound(ResourceNotFound resourceNotFound) {
        var errorDto = new ErrorDto(resourceNotFound.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatusCode.valueOf(404)).body(errorDto);

    }

    @ExceptionHandler(BadRequestException.class)
    ResponseEntity<ErrorDto> handleBadRequestException(BadRequestException exception) {
        var errorDto = new ErrorDto(exception.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatusCode.valueOf(400)).body(errorDto);
    }
}
