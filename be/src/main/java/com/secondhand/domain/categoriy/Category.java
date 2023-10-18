package com.secondhand.domain.categoriy;

import com.secondhand.web.dto.updatedto.CategoryDTO;
import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
public class Category {
    @Id
    @Column(name = "category_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @Column(length = 45, nullable = false)
    private String name;

    @Column(name = "img_url", length = 200, nullable = false)
    private String imgUrl;

    @Column(length = 300, nullable = false)
    private String placeholder;

    public Category changeEntity(CategoryDTO categoryDTO) {
        return Category.builder()
                .categoryId(categoryDTO.getId())
                .name(categoryDTO.getName())
                .imgUrl(categoryDTO.getImgUrl())
                .placeholder(categoryDTO.getPlaceholder())
                .build();
    }
}
