package com.secondhand.web.dto.response;

import com.secondhand.domain.product.Product;
import com.secondhand.web.dto.updatedto.CountInfoDTO;
import lombok.Getter;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ProductListResponse {

    private Long productId;
    private String title;
    private TownResponse town;
    private String createdAt;
    private Integer status;
    private Integer price;
    private CountInfoDTO countInfo;

    private String imgUrl;

    private Boolean isLiked;
    private Boolean isMine;

    public ProductListResponse(Product product, long userId) {
        this.productId = product.getId();
        this.title = product.getTitle();
        this.town = new TownResponse(product.getTowns());
        this.createdAt = product.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
        this.status = product.getStatus().getValue();
        this.price = product.getPrice();
        this.countInfo = new CountInfoDTO(0, product.getCountLike());
        this.imgUrl = product.getThumbnailUrl();
        this.isLiked = product.findLiked();
        this.isMine = product.checkIsMine(userId);
    }

    public static List<ProductListResponse> fromList(List<Product> page, long userId) {
        List<ProductListResponse> productList = new ArrayList<>();
        for (Product product : page) {
            ProductListResponse response = new ProductListResponse(product, userId);
            productList.add(response);
        }
        return productList;
    }

}
