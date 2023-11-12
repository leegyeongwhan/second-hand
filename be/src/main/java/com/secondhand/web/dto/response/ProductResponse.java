package com.secondhand.web.dto.response;

import com.secondhand.domain.image.Image;
import com.secondhand.domain.product.Product;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ProductResponse {

    private final Boolean isSeller;
    private final List<String> imageUrls;
    private final String seller;
    private final String status;
    private final String title;
    private final String categoryName;
    private final LocalDateTime createdAt;
    private final String content;
    private final int chatCount;
    private final int wishCount;
    private final int viewCount;
    private final Integer price;
    private final Boolean isInWishList;
    private final Long chatRoomId;

    @Builder
    public ProductResponse(Boolean isSeller, List<String> imageUrls, String seller, String status,
            String title,
            String categoryName, LocalDateTime createdAt, String content, int chatCount,
            int wishCount,
            int viewCount, Integer price, Boolean isInWishList, Long chatRoomId) {
        this.isSeller = isSeller;
        this.imageUrls = imageUrls;
        this.seller = seller;
        this.status = status;
        this.title = title;
        this.categoryName = categoryName;
        this.createdAt = createdAt;
        this.content = content;
        this.chatCount = chatCount;
        this.wishCount = wishCount;
        this.viewCount = viewCount;
        this.price = price;
        this.isInWishList = isInWishList;
        this.chatRoomId = chatRoomId;
    }

    public static ProductResponse toSellerResponse(Product product, List<Image> images) {
        return ProductResponse.builder()
                .isSeller(true)
                .imageUrls(images.stream()
                        .map(Image::getImgUrl)
                        .collect(Collectors.toUnmodifiableList()))
                .seller(product.getMember().getLoginName())
                .status(product.getStatus().name())
                .title(product.getTitle())
                .categoryName(product.getCategory())
                .createdAt(product.getCreatedAt())
                .content(product.getContent())
                .chatCount(product.getChatCount())
                .wishCount(product.getInteresteds().size())
                .viewCount(product.getCountView())
                .price(product.getPrice())
                .build();
    }

    public static ProductResponse toBuyerResponse(Product product, List<Image> images,
            Boolean isInWishList, Long chatRoomId) {
        return ProductResponse.builder()
                .isSeller(false)
                .imageUrls(images.stream()
                        .map(Image::getImgUrl)
                        .collect(Collectors.toUnmodifiableList()))
                .seller(product.getMember().getLoginName())
                .title(product.getTitle())
                .categoryName(product.getCategory())
                .createdAt(product.getCreatedAt())
                .content(product.getContent())
                .chatCount(product.getChatCount())
                .wishCount(product.getInteresteds().size())
                .viewCount(product.getCountView())
                .price(product.getPrice())
                .isInWishList(isInWishList)
                .chatRoomId(chatRoomId)
                .build();
    }
}
