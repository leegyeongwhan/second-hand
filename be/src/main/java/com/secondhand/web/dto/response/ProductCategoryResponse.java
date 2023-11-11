package com.secondhand.web.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductCategoryResponse {
    private String categoryId;
    private String name;

    public ProductCategoryResponse(String categoryId, String name) {
        this.categoryId = categoryId;
        this.name = name;
    }
}
