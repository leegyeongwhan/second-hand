package com.secondhand.web.dto.filtercondition;

import lombok.AllArgsConstructor;
import lombok.Getter;
import reactor.util.annotation.Nullable;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;


@Getter
@AllArgsConstructor
public class ProductSearchCondition {
    private Long lastNum;
    private Long townId;
    private String categoryId;

    @Nullable
    private Boolean isLiked;
    @Min(0)
    @Max(2)
    @Nullable
    private Integer status;
}
