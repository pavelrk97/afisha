package com.afisha.main.category.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.afisha.main.category.CategoryRepository;
import com.afisha.main.category.dto.CategoryDto;
import com.afisha.main.category.dto.NewCategoryDto;
import com.afisha.main.category.dto.UpdateCategoryDto;
import com.afisha.main.category.mapper.CategoryMapper;
import com.afisha.main.category.model.Category;
import com.afisha.main.event.EventRepository;
import com.afisha.main.exception.ConflictException;
import com.afisha.main.exception.DuplicatedDataException;
import com.afisha.main.exception.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryServiceImpl implements CategoryService {

    CategoryRepository categoryRepository;
    EventRepository eventRepository;

    @Override
    public CategoryDto create(NewCategoryDto newCategoryDto) {
        log.info("Попытка создать категорию с именем: {}", newCategoryDto.getName());
        if (categoryRepository.existsByName(newCategoryDto.getName())) {
            throw new DuplicatedDataException("Название категории уже зарегистрировано: " + newCategoryDto.getName());
        }
        Category category = CategoryMapper.toNewCategoryFromDto(newCategoryDto);
        Category createdCategory = categoryRepository.save(category);
        log.info("Категория успешно создана с ID: {}", createdCategory.getId());
        return CategoryMapper.toCategoryDto(createdCategory);
    }

    @Override
    public void delete(Long id) {
        if (eventRepository.existsByCategoryId(id)) {
            throw new ConflictException("Невозможно удалить категорию, так как с ней связаны события.");
        }
        categoryRepository.deleteById(id);
    }

    @Override
    public CategoryDto update(Long catId, UpdateCategoryDto updateCategoryDto) {
        log.info("Попытка обновить категорию с ID: {}", catId);
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Категория с ID " + catId + " не найдена!"));

        String newName = updateCategoryDto.getName().trim();
        category.setName(newName);

        categoryRepository.save(category);
        log.info("Категория с ID {} успешно обновлена.", catId);
        return CategoryMapper.toCategoryDto(category);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDto getCategoryById(Long catId) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Категория с ID " + catId + " не найдена!"));
        return CategoryMapper.toCategoryDto(category);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDto> getAllCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable)
                .stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }
}