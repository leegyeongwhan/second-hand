package com.secondhand.application.category;

import com.secondhand.domain.categoriy.Category;
import com.secondhand.domain.categoriy.CategoryRepository;
import com.secondhand.service.CategoryService;
import com.secondhand.web.dto.response.CategoryListResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("카테고리 서비스 테스트")
class CategoryServiceTest {

    @Autowired
    private CategoryService categoryService;
    private CategoryRepository categoryRepository;

    @DisplayName("카테고리 목록을 반환한다.")
    @Test
    void read() {
        // given
        for (int i = 0; i < 18; i++) {
            categoryRepository.save(Category.builder()
                    .name("test" + i)
                    .imgUrl("test-img")
                    .placeholder("가구, 좋아")
                    .build());
        }

        // when
        CategoryListResponse response = categoryService.readAll();

        // then
        assertThat(response.getCategories()).hasSize(18);
    }
}
