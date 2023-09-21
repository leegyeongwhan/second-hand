package com.secondhand.web.dto.filtercondition;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductSearchCondition {
    //    private final int pageNum;
    private Long townId;
    private Long categoryId;
}
