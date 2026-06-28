package com.afisha.main.category.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.afisha.main.category.dto.CategoryDto;
import com.afisha.main.category.dto.NewCategoryDto;
import com.afisha.main.category.dto.UpdateCategoryDto;
import com.afisha.main.category.service.CategoryService;

@Slf4j
@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class AdminCategoryController {

    private final CategoryService service;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CategoryDto create(@RequestBody @Valid NewCategoryDto newCategoryDto) {
        log.info("POST запрос на создание категории: {}", newCategoryDto);
        return service.create(newCategoryDto);
    }

    @PatchMapping("/{catId}")
    public CategoryDto update(@PathVariable Long catId,
                              @RequestBody @Valid UpdateCategoryDto updateCategoryDto) {
        log.info("PATCH запрос на обновление категории c id: {}", catId);
        return service.update(catId, updateCategoryDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{catId}")
    public void delete(@PathVariable Long catId) {
        log.info("DELETE запрос на удаление категории с id: {}", catId);
        service.delete(catId);
    }
}