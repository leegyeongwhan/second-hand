package com.secondhand.presentation.controller;

import com.secondhand.service.CategoryService;
import com.secondhand.util.BasicResponse;
import com.secondhand.web.dto.response.CategoryListResponse;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "카테고리")
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("")
    public BasicResponse<CategoryListResponse> read() {
        return BasicResponse.send(HttpStatus.OK.value(),"사용자는 모든 카테고리 목록을 가져올 수 있다", categoryService.readAll());
    }
}
