package com.secondhand.web.controller;

import com.secondhand.domain.categorie.Category;
import com.secondhand.service.CategoryService;
import com.secondhand.util.BasicResponse;
import com.secondhand.web.dto.response.CategoryResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Api(tags = "카테고리")
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "모든 카테고리 목록", description = "사용자는 모든 카테고리 목록을 가져올 수 있다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "BAD REQUEST"),
            @ApiResponse(code = 404, message = "NOT FOUND"),
            @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR"),
    })

    @GetMapping("")
    public BasicResponse<List<CategoryResponse>> read() {
        List<Category> categoryList = categoryService.readAll();
        List<CategoryResponse> categoryResponseList = new ArrayList<>();

        // Category 객체를 CategoryDTO로 변환
        for (Category category : categoryList) {
            CategoryResponse categoryResponse = CategoryResponse.builder()
                    .categoryId(category.getCategoryId())
                    .name(category.getName())
                    .imgUrl(category.getImgUrl())
                    .placeholder(category.getPlaceholder())
                    .build();
            categoryResponseList.add(categoryResponse);
        }

        return BasicResponse.send("사용자는 모든 카테고리 목록을 가져올 수 있다", categoryResponseList);
    }
}
