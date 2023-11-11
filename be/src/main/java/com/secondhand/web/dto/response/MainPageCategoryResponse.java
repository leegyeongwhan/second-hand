package com.secondhand.web.dto.response;

import com.secondhand.domain.product.Product;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MainPageCategoryResponse {

    private List<ProductListResponse> products;
    private List<String> categoryIds;

    public static MainPageCategoryResponse of(List<Product> page, List<String> categoryIds, long userId) {
        return MainPageCategoryResponse.builder()
                .categoryIds(categoryIds)
                .products(ProductListResponse.fromList(page, userId))
                .build();
    }
}
