package com.afisha.main.category.mapper;

import com.afisha.main.category.dto.CategoryDto;
import com.afisha.main.category.dto.NewCategoryDto;
import com.afisha.main.category.model.Category;


public class CategoryMapper {
    public static Category toNewCategoryFromDto(NewCategoryDto newCategoryDto) {
        return Category.builder()
                .name(newCategoryDto.getName())
                .build();
    }

    public static CategoryDto toCategoryDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}