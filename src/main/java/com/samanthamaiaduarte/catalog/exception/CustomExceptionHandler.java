package com.samanthamaiaduarte.catalog.exception;

import com.samanthamaiaduarte.catalog.dto.ExceptionHandlerDTO;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CustomExceptionHandler {
    @ExceptionHandler(CategoryAlreadyExistsException.class)
    public ResponseEntity<ExceptionHandlerDTO> categoryAlreadyExistsHandler(CategoryAlreadyExistsException exception) {
        ExceptionHandlerDTO response = new ExceptionHandlerDTO(HttpStatus.CONFLICT.value(), HttpStatus.CONFLICT, LocalDateTime.now(), exception.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ExceptionHandlerDTO> categoryNotFoundHandler(CategoryNotFoundException exception) {
        ExceptionHandlerDTO response = new ExceptionHandlerDTO(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND, LocalDateTime.now(), exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(CategoryHasProductsException.class)
    public ResponseEntity<ExceptionHandlerDTO> categoryHasProductsHandler(CategoryHasProductsException exception) {
        ExceptionHandlerDTO response = new ExceptionHandlerDTO(HttpStatus.CONFLICT.value(), HttpStatus.CONFLICT, LocalDateTime.now(), exception.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }
}
