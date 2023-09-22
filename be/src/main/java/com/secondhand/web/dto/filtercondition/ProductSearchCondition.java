package com.secondhand.web.dto.filtercondition;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductSearchCondition {
    //    private final int pageNum;
    private Long townId;
    private Long categoryId;
    @Min(0)
    @Max(2)
    private Integer status;
}
