package com.secondhand.web.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.secondhand.domain.categoriy.Category;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CategoryResponse {

    @JsonProperty("categoryId")
    private final Long categoryId;

    @JsonProperty("name")
    private final String name;

    @JsonProperty("imgUrl")
    private final String imgUrl;

    @JsonProperty("placeholder")
    private final String placeholder;

    @Builder
    public CategoryResponse(Long categoryId, String name, String imgUrl, String placeholder) {
        this.categoryId = categoryId;
        this.name = name;
        this.imgUrl = imgUrl;
        this.placeholder = placeholder;
    }

    public static CategoryResponse toResponse(Category category) {
        return CategoryResponse.builder()
                .categoryId(category.getCategoryId())
                .imgUrl(category.getImgUrl())
                .name(category.getName())
                .placeholder(category.getPlaceholder())
                .build();
    }
}
