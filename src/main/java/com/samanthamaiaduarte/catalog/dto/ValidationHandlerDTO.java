package com.samanthamaiaduarte.catalog.dto;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

public record ValidationHandlerDTO(Integer status_code, HttpStatus status, LocalDateTime timestamp, Map<String, String> error_messages) {
}
