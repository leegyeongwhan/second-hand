package com.secondhand.service;

import com.secondhand.domain.categoriy.Category;
import com.secondhand.domain.categoriy.CategoryRepository;
import com.secondhand.exception.CategoryNotFoundException;
import com.secondhand.web.dto.response.CategoryListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private CategoryRepository categoryRepository;

    public CategoryListResponse readAll() {
        return CategoryListResponse.toResponse(categoryRepository.findAll());
    }

    public Category findById(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(CategoryNotFoundException::new);
    }
}
