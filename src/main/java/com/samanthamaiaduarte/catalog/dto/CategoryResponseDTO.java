package com.samanthamaiaduarte.catalog.dto;

import java.util.UUID;

public record CategoryResponseDTO(UUID id, String name, String description) {
}
