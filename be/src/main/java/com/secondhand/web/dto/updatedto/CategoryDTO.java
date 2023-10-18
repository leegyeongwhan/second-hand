package com.secondhand.web.dto.updatedto;

import com.secondhand.domain.categoriy.Category;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CategoryDTO {


    private Long id;
    private String name;
    private String imgUrl;
    private String placeholder;

    public static CategoryDTO from(Category category) {
        return CategoryDTO.builder()
                .id(category.getCategoryId())
                .name(category.getName())
                .imgUrl(category.getImgUrl())
                .placeholder(category.getPlaceholder())
                .build();
    }
}
