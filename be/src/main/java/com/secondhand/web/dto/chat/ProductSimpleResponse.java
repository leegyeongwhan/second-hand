package com.secondhand.web.dto.chat;

import com.secondhand.domain.product.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductSimpleResponse {
    private String title;
    private String thumbnailUrl;
    private Integer price;

    public static ProductSimpleResponse from(Product product) {
        return new ProductSimpleResponse(product.getTitle(), product.getThumbnailUrl(), product.getPrice());
    }
}
