package com.samanthamaiaduarte.catalog.repository;

import com.samanthamaiaduarte.catalog.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CategoryRepository  extends JpaRepository<Category, UUID> {
    boolean existsByName(String name);
}
