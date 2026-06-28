package com.afisha.main.category.service;

import com.afisha.main.category.dto.CategoryDto;
import com.afisha.main.category.dto.NewCategoryDto;
import com.afisha.main.category.dto.UpdateCategoryDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {
    CategoryDto create(NewCategoryDto newCategoryDto);

    void delete(Long id);

    CategoryDto update(Long catId, UpdateCategoryDto updateCategoryDto);

    CategoryDto getCategoryById(Long catId);

    List<CategoryDto> getAllCategories(Pageable pageable);

}