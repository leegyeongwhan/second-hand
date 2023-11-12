package com.secondhand.service;

import com.secondhand.web.dto.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ProductReadFacade {

    private final ProductService productService;
    private final ViewCountService viewCountService;

    public ProductResponse read(Long memberId, Long itemId) {
        ProductResponse response = productService.read(memberId, itemId);

        if (!response.getIsSeller()) {
            viewCountService.increaseViewCount(itemId);
        }

        return response;
    }
}
