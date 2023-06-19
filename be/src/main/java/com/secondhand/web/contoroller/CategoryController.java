package com.secondhand.web.contoroller;

import com.secondhand.domain.categorie.Category;
import com.secondhand.domain.categorie.CategoryService;
import com.secondhand.util.BasicResponse;
import com.secondhand.web.dto.response.CategoryDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @Operation(
            summary = "모든 카테고리 목록",
            tags = "categories",
            description = "사용자는 모든 카테고리 목록을 가져올 수 있다."
    )

    @GetMapping("")
    public ResponseEntity<BasicResponse> read() {
        List<Category> categoryList = categoryService.readAll();
        List<CategoryDTO> categoryDTOList = new ArrayList<>();

        // Category 객체를 CategoryDTO로 변환
        for (Category category : categoryList) {
            CategoryDTO categoryDTO = CategoryDTO.builder()
                    .categoryId(category.getId())
                    .name(category.getName())
                    .imgUrl(category.getImgUrl())
                    .build();
            categoryDTOList.add(categoryDTO);
        }

        BasicResponse message = BasicResponse.builder()
                .success(true)
                .message("")
                .apiStatus(20000)
                .httpStatus(HttpStatus.OK)
                .data(categoryDTOList)
                .build();

        return ResponseEntity.ok()
                .body(message);
    }

    @GetMapping("/test")
    public ResponseEntity<BasicResponse> view() {
        List<CategoryDTO> categoryDTOList = List.of(new CategoryDTO(1L, "비디오", "url경로"),
                new CategoryDTO(1L, "비디오", "url경로"),
                new CategoryDTO(1L, "비디오", "url경로"));

        BasicResponse message = BasicResponse.builder()
                .success(true)
                .message("")
                .apiStatus(20000)
                .httpStatus(HttpStatus.OK)
                .data(categoryDTOList)
                .build();

        return ResponseEntity.ok()
                .body(message);
    }
}