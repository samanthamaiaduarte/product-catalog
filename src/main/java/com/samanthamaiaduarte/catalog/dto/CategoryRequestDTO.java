package com.samanthamaiaduarte.catalog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(name = "Category information", description = "Payload for create or update a category")
public record CategoryRequestDTO(
        @NotBlank(message = "Category name can't be empty.")
        @Size(min = 1, max = 100, message = "Category name max size is 100 characters.")
        String name,

        @Size(max = 200, message = "Category description max size is 200 characters.")
        String description) {
}
