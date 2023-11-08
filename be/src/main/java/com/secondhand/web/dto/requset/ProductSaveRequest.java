package com.secondhand.web.dto.requset;

import com.secondhand.domain.categoriy.Category;
import com.secondhand.domain.member.Member;
import com.secondhand.domain.product.Product;
import com.secondhand.domain.product.Status;
import com.secondhand.domain.town.Town;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductSaveRequest {

    @NotBlank(message = "상품의 제목은 비어있을 수 없습니다.")
    @Size(max = 45, message = "상품 제목의 길이는 45자를 넘을 수 없습니다.")
    private String title;
    private Integer price;

    @Size(max = 2000, message = "상품 내용의 길이는 2000자를 넘을 수 없습니다.")
    private String content;
    @NotNull(message = "상품의 지역 이름은 비어있을 수 없습니다.")
    private Long townId;
    @NotNull(message = "상품의 카테고리 아이디를 포함해 요청해주세요.")
    private Long categoryId;


    public Product toEntity(Member member, Category category, Town town, String thumbnailUrl) {
        return Product.builder()
                .title(this.title)
                .content(this.content)
                .price(this.price)
                .thumbnailUrl(thumbnailUrl)
                .status(Status.SELLING)
                .category(category)
                .towns(town)
                .member(member)
                .build();
    }
}
