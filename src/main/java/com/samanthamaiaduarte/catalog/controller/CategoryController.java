package com.samanthamaiaduarte.catalog.controller;

import com.samanthamaiaduarte.catalog.dto.CategoryRequestDTO;
import com.samanthamaiaduarte.catalog.dto.CategoryResponseDTO;
import com.samanthamaiaduarte.catalog.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/categories")
@Tag(name = "Categories" , description = "Categories endpoints")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @SecurityRequirement(name = "bearerAuth")
    @Operation(description = "Create a new category")
    @PostMapping
    public ResponseEntity<CategoryResponseDTO> createCategory(@RequestBody @Valid CategoryRequestDTO requestDTO) {
        CategoryResponseDTO responseDTO = categoryService.createCategory(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @SecurityRequirement(name = "bearerAuth")
    @Operation(description = "Update a existing category")
    @PutMapping(value = "/{categoryId}", name = "Id")
    public ResponseEntity<CategoryResponseDTO> updateCategory(@PathVariable @Parameter(name = "categoryId", description = "Category identifier", required = true, example = "9e8088d0-c495-40cd-8fe5-6f76857c677f") UUID categoryId, @RequestBody @Valid CategoryRequestDTO requestDTO) {
        CategoryResponseDTO responseDTO = categoryService.updateCategory(categoryId, requestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    @SecurityRequirement(name = "bearerAuth")
    @Operation(description = "Delete a existing category")
    @DeleteMapping(value = "/{categoryId}", name = "Id")
    public ResponseEntity<Void> deleteCategory(@PathVariable @Parameter(name = "categoryId", description = "Category identifier", required = true, example = "9e8088d0-c495-40cd-8fe5-6f76857c677f") UUID categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(description = "Show a specific category")
    @GetMapping(value = "/{categoryId}", name = "Id")
    public ResponseEntity<CategoryResponseDTO> selectCategory(@PathVariable @Parameter(name = "categoryId", description = "Category identifier", required = true, example = "9e8088d0-c495-40cd-8fe5-6f76857c677f") UUID categoryId) {
        CategoryResponseDTO responseDTO = categoryService.selectCategory(categoryId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    @Operation(description = "Show all categories available")
    @GetMapping
    public ResponseEntity<Page<CategoryResponseDTO>> selectCategories(@PageableDefault(size = 20, sort = "name") Pageable pageable) {
        Page<CategoryResponseDTO> responseDTO = categoryService.selectCategories(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }
}
