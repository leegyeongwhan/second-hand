package com.secondhand.documentation;

import com.secondhand.config.FixtureFactory;
import com.secondhand.service.ProductService;
import com.secondhand.web.dto.requset.ProductSaveRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static com.secondhand.documentation.ConstraintsHelper.withPath;
import static javax.management.openmbean.SimpleType.BOOLEAN;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class ProductDocumentationTest extends DocumentationTestSupport {

    @Autowired
    private ProductService productService;


    @DisplayName("상품 등록")
    @Test
    void saveProduct() throws Exception {
        // given

        ProductSaveRequest request = FixtureFactory.createProductSaveRequest();

        willDoNothing()
                .given(productService)
                .save(anyLong(), any(ProductSaveRequest.class), any(MultipartFile.class),
                        anyList());

        MockMultipartFile thumbnail = new MockMultipartFile("thumbnailImage", "image.png",
                "image/png",
                "<<png data>>".getBytes());
        MockMultipartFile itemImage = new MockMultipartFile("images", "image.png", "image/png",
                "<<png data>>".getBytes());
        MockMultipartFile itemRegisterData = new MockMultipartFile("productSaveRequest", "",
                MediaType.APPLICATION_JSON_VALUE, objectMapper.writeValueAsBytes(request));

        // when
        var response = mockMvc.perform(multipart(HttpMethod.POST, "/api/products")
                .file(thumbnail)
                .file(itemImage)
                .file(itemRegisterData)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtProvider.createAccessToken(1L))
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE));

        // then
        var resultActions = response
                .andExpect(status().isCreated())
                .andExpect(jsonPath("httpStatus").value(201))
                .andExpect(jsonPath("message").value("상품 등록."))
                .andExpect(jsonPath("apiStatus").value("20000"))
                .andExpect(jsonPath("success").value("true"));

        // docs
        resultActions.andDo(document("product/save",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestHeaders(
                        headerWithName(HttpHeaders.AUTHORIZATION).description("액세스 토큰을 담는 인증 헤더")
                ),
                requestParts(
                        partWithName("thumbnailImage").description("썸네일 이미지"),
                        partWithName("images").optional().description("상품 이미지")
                                .attributes(key("nullable").value(true)),
                        partWithName("productSaveRequest").description("상품 데이터")
                ),
                requestPartFields("productSaveRequest",
                        withPath("title", ProductSaveRequest.class).description("상품 제목"),
                        withPath("price", ProductSaveRequest.class).description("상품 가격"),
                        withPath("content", ProductSaveRequest.class).description("상품 설명"),
                        withPath("townId", ProductSaveRequest.class).description("상품 판매 지역"),
                        withPath("categoryId", ProductSaveRequest.class).description("상품 카테고리 아이디")
                ),
                responseFields(
                        fieldWithPath("httpStatus").type(NUMBER).description("응답코드"),
                        fieldWithPath("message").type(STRING).description("응답 메시지"),
                        fieldWithPath("apiStatus").type(NUMBER).description("메서드 상태 코드"),
                        fieldWithPath("success").type(BOOLEAN).description("성공 여부"),
                        fieldWithPath("data").ignored()
                )
        ));
    }

//    @DisplayName("상품 수정")
//    @Test
//    void updateProduct() throws Exception {
//
//    }
//
//    @DisplayName("상품 삭제")
//    @Test
//    void updateProduct() throws Exception {
//
//    }
}
