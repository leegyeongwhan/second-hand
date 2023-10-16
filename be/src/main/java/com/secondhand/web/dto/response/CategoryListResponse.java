package com.secondhand.web.dto.response;

import com.secondhand.domain.categorie.Category;
import lombok.Getter;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CategoryListResponse {

    private final List<CategoryResponse> categories;

    private CategoryListResponse(List<CategoryResponse> categories) {
        this.categories = Collections.unmodifiableList(categories);
    }

    public static CategoryListResponse toResponse(List<Category> categories) {
        List<CategoryResponse> list = categories.stream()
                .map(CategoryResponse::toResponse)
                .collect(Collectors.toList());
        return new CategoryListResponse(list);
    }
}
