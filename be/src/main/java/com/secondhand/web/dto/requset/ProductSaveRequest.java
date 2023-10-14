package com.secondhand.web.dto.requset;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.util.List;


@Getter
@Setter
@ToString
@Builder
public class ProductSaveRequest {
    @NotNull
    private String title;
    @NotNull
    private String content;
    @NotNull
    private Long categoryId;
    @NotNull
    private Integer price;
    @NotNull
    private Long townId;
    private List<MultipartFile> productImages;
}
