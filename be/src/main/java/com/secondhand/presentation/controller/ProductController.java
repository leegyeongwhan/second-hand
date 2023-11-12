package com.secondhand.presentation.controller;

import com.secondhand.presentation.support.LoginCheck;
import com.secondhand.presentation.support.LoginValue;
import com.secondhand.service.InterestedProductService;
import com.secondhand.service.ProductQueryService;
import com.secondhand.service.ProductReadFacade;
import com.secondhand.service.ProductService;
import com.secondhand.util.BasicResponse;
import com.secondhand.web.dto.filtercondition.ProductCategorySearchCondition;
import com.secondhand.web.dto.filtercondition.ProductSearchCondition;
import com.secondhand.web.dto.requset.ProductSaveRequest;
import com.secondhand.web.dto.requset.ProductUpdateRequest;
import com.secondhand.web.dto.requset.StatusOrLikeRequest;
import com.secondhand.web.dto.response.MainPageCategoryResponse;
import com.secondhand.web.dto.response.MainPageResponse;
import com.secondhand.web.dto.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final ProductQueryService productQueryService;
    private final ProductReadFacade productReadFacade;
    private final InterestedProductService wishItemService;

    @GetMapping
    public BasicResponse<MainPageResponse> viewPage(ProductSearchCondition productSearchCondition,
            @LoginValue Long userId) {
        log.debug("userId = {}", userId);
        MainPageResponse mainPageResponse = productQueryService.getProductList(
                productSearchCondition, userId);
        return BasicResponse.send(HttpStatus.OK.value(), "사용자는 상품을 10개씩 상품 리스프로 볼 수 있다(지역 과 카테고리)",
                mainPageResponse);

    }

    @LoginCheck
    @GetMapping("/like")
    public BasicResponse<MainPageCategoryResponse> productLikeCategoryView(
            ProductCategorySearchCondition condition,
            Pageable pageable,
            @LoginValue long userId) {

        MainPageCategoryResponse likeProductList = productQueryService.getLikeProductList(condition,
                pageable, userId);

        return BasicResponse.send(HttpStatus.OK.value(), "사용자는 자시의 관심 목록을 카테고리 뱔로 확인 가능",
                likeProductList);

    }

    @PatchMapping("/{productId}")
    public BasicResponse<Void> changeLike(
            final @Valid @RequestBody StatusOrLikeRequest request,
            @PathVariable long productId,
            @LoginValue long userId) {

        wishItemService.changeLike(productId, userId, request);
        return BasicResponse.send(HttpStatus.OK.value(), "사용자는상품을 과 관심상품 / 해제 할수 있다.");
    }


    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public BasicResponse<Void> save(@RequestPart(required = false) MultipartFile thumbnailImage,
            @RequestPart(required = false) List<MultipartFile> images,
            @LoginValue Long userId,
            @Valid @RequestPart ProductSaveRequest productSaveRequest) {
        productService.save(userId, productSaveRequest, thumbnailImage, images);
        return BasicResponse.send(HttpStatus.CREATED.value(), "상품 등록.");
    }


//    @PutMapping("/{productId}")
//    public BasicResponse<ProductResponse> update(@LoginValue long userId,
//            @PathVariable Long productId,
//            @Valid @RequestBody ProductUpdateRequest updateRequest) {
//        productService.update(productId, updateRequest, userId);
//        ProductResponse productUpdateResponse = productQueryService.isValidMinePage(productId,
//                userId);
//
//        return BasicResponse.send(HttpStatus.OK.value(), "상품 수정.", productUpdateResponse);
//    }


    @GetMapping("/{productId}")
    public BasicResponse<ProductResponse> readDetail(
            @LoginValue long userId,
            @PathVariable long productId) {

        ProductResponse detailPage = productReadFacade.read(productId, userId);

        return BasicResponse.send(HttpStatus.OK.value(), "상품 디테일 페이지.", detailPage);
    }

    @LoginCheck
    @DeleteMapping("/{productId}")
    public BasicResponse<String> deleteProduct(@LoginValue long userId,
            @PathVariable long productId) {
        productService.delete(userId, productId);

        return BasicResponse.send(HttpStatus.OK.value(), " 상품 삭제");
    }
}
