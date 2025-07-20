package com.samanthamaiaduarte.catalog.dto;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record ExceptionHandlerDTO(Integer status_code, HttpStatus status, LocalDateTime timestamp, String error_message) {
}
