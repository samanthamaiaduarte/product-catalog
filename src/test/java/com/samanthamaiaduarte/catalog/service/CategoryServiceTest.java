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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private CategoryMapper categoryMapper;
    @InjectMocks
    private CategoryService categoryService;

    private UUID categoryId;
    private Category existingCategory;

    @BeforeEach
    void setup() {
        categoryId = UUID.randomUUID();
        existingCategory = new Category(categoryId, "Livros", "Categoria de livros");
    }

    @Test
    @DisplayName("CREATE: When a category does not exist: should save and return a CategoryResponseDTO")
    void createCategory1() {
        // arrange
        var requestDTO = new CategoryRequestDTO("Eletrônicos", "Produtos eletrônicos");
        var category = new Category(UUID.randomUUID(), "Eletrônicos", "Produtos eletrônicos");
        var responseDTO = new CategoryResponseDTO(category.getId(), category.getName(), category.getDescription());

        when(categoryRepository.existsByName("Eletrônicos")).thenReturn(false);
        when(categoryMapper.toEntity(requestDTO)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(responseDTO);

        // act
        CategoryResponseDTO result = categoryService.createCategory(requestDTO);

        // assert
        assertEquals(responseDTO, result);
        verify(categoryRepository).save(category);
    }

    @Test
    @DisplayName("CREATE: When a category already exists: should throw a CategoryAlreadyExistsException")
    void createCategory2() {
        // arrange
        var requestDTO = new CategoryRequestDTO("Eletrônicos", "Produtos eletrônicos");

        when(categoryRepository.existsByName("Eletrônicos")).thenReturn(true);

        // act & assert
        assertThrows(CategoryAlreadyExistsException.class, () -> {
            categoryService.createCategory(requestDTO);
        });

        verify(categoryRepository, never()).save(any());
    }

    @Test
    @DisplayName("UPDATE: When a category exists and name is unique: should update successfully a return a CategoryResponseDTO")
    void updateCategory1() {
        //arrange
        var requestDTO = new CategoryRequestDTO("Eletrônicos", "Produtos eletrônicos");
        var responseDTO = new CategoryResponseDTO(categoryId, "Eletrônicos", "Produtos eletrônicos");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(existingCategory));
        when(categoryRepository.existsByName("Eletrônicos")).thenReturn(false);
        doAnswer(invocation -> {
            existingCategory.setName(requestDTO.name());
            existingCategory.setDescription(requestDTO.description());
            return null;
        }).when(categoryMapper).updateCategoryFromDto(requestDTO, existingCategory);
        when(categoryMapper.toDto(existingCategory)).thenReturn(responseDTO);

        // act
        var result = categoryService.updateCategory(categoryId, requestDTO);

        // assert
        assertEquals(responseDTO, result);
        verify(categoryRepository).save(existingCategory);
    }

    @Test
    @DisplayName("UPDATE: When a category does not exist: should throw a CategoryNotFoundException")
    void updateCategory2() {
        //arrange
        var requestDTO = new CategoryRequestDTO("Eletrônicos", "Produtos eletrônicos");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        // act & assert
        assertThrows(CategoryNotFoundException.class, () -> {
            categoryService.updateCategory(categoryId, requestDTO);
        });

        verify(categoryRepository, never()).save(any());
    }

    @Test
    @DisplayName("UPDATE: When category name is not changed: should update without checking uniqueness")
    void updateCategory3() {
        //arrange
        var requestDTO = new CategoryRequestDTO("Livros", "Nova descrição");
        var responseDTO = new CategoryResponseDTO(categoryId, "Livros", "Nova descrição");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(existingCategory));
        doAnswer(invocation -> {
            existingCategory.setDescription(requestDTO.description());
            return null;
        }).when(categoryMapper).updateCategoryFromDto(requestDTO, existingCategory);
        when(categoryMapper.toDto(existingCategory)).thenReturn(responseDTO);

        // act
        var result = categoryService.updateCategory(categoryId, requestDTO);

        // assert
        assertEquals(responseDTO, result);
        verify(categoryRepository).save(existingCategory);
        verify(categoryRepository, never()).existsByName(any());
    }

    @Test
    @DisplayName("UPDATE: When new name already exists: should throw a CategoryAlreadyExistsException")
    void updateCategory4() {
        //arrange
        var requestDTO = new CategoryRequestDTO("Eletrônicos", "Outra descrição");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(existingCategory));
        when(categoryRepository.existsByName("Eletrônicos")).thenReturn(true);

        // act & assert
        assertThrows(CategoryAlreadyExistsException.class, () -> {
            categoryService.updateCategory(categoryId, requestDTO);
        });

        verify(categoryRepository, never()).save(any());
    }

    @Test
    @DisplayName("DELETE: When a category exists and has no linked products: should delete successfully")
    void deleteCategory1() {
        // arrange
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(existingCategory));
        when(productRepository.existsByCategoryId(categoryId)).thenReturn(false);

        // act
        categoryService.deleteCategory(categoryId);

        // assert
        verify(categoryRepository).delete(existingCategory);
    }

    @Test
    @DisplayName("DELETE: When a category does not exist: should throw a CategoryNotFoundException")
    void deleteCategory2() {
        // arrange
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        // act & assert
        assertThrows(CategoryNotFoundException.class, () -> {
            categoryService.deleteCategory(categoryId);
        });

        verify(categoryRepository, never()).delete(any());
    }

    @Test
    @DisplayName("DELETE: When a category exists and has linked products: should throw a CategoryHasProductsException")
    void deleteCategory3() {
        // arrange
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(existingCategory));
        when(productRepository.existsByCategoryId(categoryId)).thenReturn(true);

        // act & assert
        assertThrows(CategoryHasProductsException.class, () -> {
            categoryService.deleteCategory(categoryId);
        });

        verify(categoryRepository, never()).delete(any());
    }

    @Test
    @DisplayName("SELECT: When a category exists: should return a CategoryResponseDTO")
    void selectCategory1() {
        //arrange
        var responseDTO = new CategoryResponseDTO(categoryId, "Livros", "Categoria de livros");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(existingCategory));
        when(categoryMapper.toDto(existingCategory)).thenReturn(responseDTO);

        // act
        CategoryResponseDTO result = categoryService.selectCategory(categoryId);

        // assert
        assertNotNull(result);
        assertEquals(responseDTO, result);
    }

    @Test
    @DisplayName("SELECT: When a category does not exist: should throw a CategoryNotFoundException")
    void selectCategory2() {
        // arrange
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        // act & assert
        assertThrows(CategoryNotFoundException.class, () -> {
            categoryService.deleteCategory(categoryId);
        });
    }

    @Test
    @DisplayName("SELECT LIST: Should return Paged CategoryResponseDTO")
    void selectCategories1() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 2);

        var cat1 = new Category(UUID.randomUUID(), "Cat1", "Desc1");
        var cat2 = new Category(UUID.randomUUID(), "Cat2", "Desc2");
        List<Category> categories = List.of(cat1, cat2);
        Page<Category> categoryPage = new PageImpl<>(categories, pageable, categories.size());

        var dto1 = new CategoryResponseDTO(cat1.getId(), cat1.getName(), cat1.getDescription());
        var dto2 = new CategoryResponseDTO(cat2.getId(), cat2.getName(), cat2.getDescription());
        List<CategoryResponseDTO> dtoList = List.of(dto1, dto2);
        Page<CategoryResponseDTO> dtoPage = new PageImpl<>(dtoList, pageable, categories.size());

        when(categoryRepository.findAll(pageable)).thenReturn(categoryPage);
        when(categoryMapper.toDto(cat1)).thenReturn(dto1);
        when(categoryMapper.toDto(cat2)).thenReturn(dto2);

        // Act
        Page<CategoryResponseDTO> result = categoryService.selectCategories(pageable);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals(dto1, result.getContent().get(0));
        assertEquals(dto2, result.getContent().get(1));

        verify(categoryRepository).findAll(pageable);
        verify(categoryMapper).toDto(cat1);
        verify(categoryMapper).toDto(cat2);
    }

    @Test
    @DisplayName("SELECT LIST: When empty should return empty page")
    void selectCategories2() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<Category> emptyPage = Page.empty();

        when(categoryRepository.findAll(pageable)).thenReturn(emptyPage);

        // Act
        Page<CategoryResponseDTO> result = categoryService.selectCategories(pageable);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(categoryRepository).findAll(pageable);
        verifyNoInteractions(categoryMapper);
    }
}