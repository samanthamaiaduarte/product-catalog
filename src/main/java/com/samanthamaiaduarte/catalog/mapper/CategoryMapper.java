package com.samanthamaiaduarte.catalog.mapper;

import com.samanthamaiaduarte.catalog.dto.CategoryRequestDTO;
import com.samanthamaiaduarte.catalog.dto.CategoryResponseDTO;
import com.samanthamaiaduarte.catalog.entity.Category;
import org.mapstruct.*;

@Mapper
public interface CategoryMapper {
    @Mapping(target = "id", ignore = true)
    Category toEntity(CategoryRequestDTO requestDTO);

    @Mapping(target = "id", ignore = true)
    void updateCategoryFromDto(CategoryRequestDTO dto, @MappingTarget Category category);

    CategoryResponseDTO toDto(Category category);
}
