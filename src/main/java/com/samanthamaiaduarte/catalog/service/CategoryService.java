package com.samanthamaiaduarte.catalog.service;

import com.samanthamaiaduarte.catalog.dto.CategoryRequestDTO;
import com.samanthamaiaduarte.catalog.dto.CategoryResponseDTO;
import com.samanthamaiaduarte.catalog.entity.Category;
import com.samanthamaiaduarte.catalog.exception.CategoryAlreadyExistsException;
import com.samanthamaiaduarte.catalog.exception.CategoryHasProductsException;
import com.samanthamaiaduarte.catalog.exception.CategoryNotFoundException;
import com.samanthamaiaduarte.catalog.mapper.CategoryMapper;
import com.samanthamaiaduarte.catalog.repository.CategoryRepository;
import com.samanthamaiaduarte.catalog.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryMapper categoryMapper;

    public CategoryResponseDTO createCategory(CategoryRequestDTO requestDTO) {
        if(categoryRepository.existsByName(requestDTO.name())) throw new CategoryAlreadyExistsException();

        Category category = categoryMapper.toEntity(requestDTO);
        categoryRepository.save(category);
        return categoryMapper.toDto(category);
    }

    public CategoryResponseDTO updateCategory(UUID categoryId, CategoryRequestDTO requestDTO) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new CategoryNotFoundException());

        if(!category.getName().equals(requestDTO.name()) && categoryRepository.existsByName(requestDTO.name())) {
            throw new CategoryAlreadyExistsException();
        }

        categoryMapper.updateCategoryFromDto(requestDTO, category);
        categoryRepository.save(category);
        return categoryMapper.toDto(category);
    }

    public void deleteCategory(UUID categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new CategoryNotFoundException());

        if(productRepository.existsByCategoryId(categoryId)) throw new CategoryHasProductsException();
        categoryRepository.delete(category);
    }

    public CategoryResponseDTO selectCategory(UUID categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new CategoryNotFoundException());

        return categoryMapper.toDto(category);
    }

    public Page<CategoryResponseDTO> selectCategories(Pageable pageable) {
        Page<Category> categories = categoryRepository.findAll(pageable);
        return categories.map(categoryMapper::toDto);
    }
}
