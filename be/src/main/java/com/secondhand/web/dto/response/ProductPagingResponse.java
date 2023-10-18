package com.secondhand.web.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import com.secondhand.domain.categoriy.Category;
import com.secondhand.domain.member.Member;
import com.secondhand.domain.product.CountInfo;
import com.secondhand.domain.product.Status;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@Setter
@NoArgsConstructor
public class ProductPagingResponse {

    private Boolean isMine;
    private Member seller;
    private Status status;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private Category category;
    private Integer price;
    private CountInfo countInfo;
    private Boolean isLiked;
    private List[] imgUrls;

    @QueryProjection
    public ProductPagingResponse(boolean isMine, Member seller, Status status, String title, String content, LocalDateTime createdAt, Category category, Integer price, CountInfo countInfo, boolean isLiked, List[] imgUrls) {
        this.isMine = isMine;
        this.seller = seller;
        this.status = status;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.category = category;
        this.price = price;
        this.countInfo = countInfo;
        this.isLiked = isLiked;
        this.imgUrls = imgUrls;
    }
}
