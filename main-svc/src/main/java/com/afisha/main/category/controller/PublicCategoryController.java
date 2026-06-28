package com.afisha.main.category.controller;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import com.afisha.main.category.dto.CategoryDto;
import com.afisha.main.category.service.CategoryService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class PublicCategoryController {

    private final CategoryService service;

    @GetMapping("/{catId}")
    public CategoryDto getCategoryById(@PathVariable Long catId) {
        log.info("GET запрос на получение категории c id: {}", catId);
        return service.getCategoryById(catId);
    }

    @GetMapping
    public List<CategoryDto> getAllCategories(@RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                              @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("GET запрос на получение списка всех категорий (from={}, size={})", from, size);
        int page = from / size;
        Pageable pageable = PageRequest.of(page, size);
        return service.getAllCategories(pageable);
    }
}