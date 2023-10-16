package com.secondhand.documentation;

import com.secondhand.domain.categorie.Category;
import com.secondhand.service.CategoryService;
import com.secondhand.web.dto.response.CategoryListResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

import java.util.List;

import static javax.management.openmbean.SimpleType.STRING;
import static javax.swing.text.html.parser.DTDConstants.NUMBER;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.request;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CategoryDocumentationTest extends DocumentationTestSupport {

    @Autowired
    private CategoryService categoryService;

    @DisplayName("카테고리 목록 조회")
    @Test
    void readAllCategories() throws Exception {
        // given
        given(categoryService.readAll()).willReturn((CategoryListResponse.toResponse(
                List.of(Category.builder()
                        .categoryId(1L)
                        .name("전자제품")
                        .imgUrl("test-image-url")
                        .build()))));

        // when
        var response = mockMvc.perform(request(HttpMethod.GET, "/api/categories"));

        // then
        var resultActions = response
                .andExpect(status().isOk())
                .andExpect(jsonPath("statusCode").value(200))
                .andExpect(jsonPath("message").value("성공했습니다."))
                .andExpect(jsonPath("data.categories[0].id").value(1))
                .andExpect(jsonPath("data.categories[0].name").value("전자제품"))
                .andExpect(jsonPath("data.categories[0].imageUrl").value("test-image-url"));

        // docs
        resultActions.andDo(document("categories",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                responseFields(
                        fieldWithPath("statusCode").type(NUMBER).description("응답코드"),
                        fieldWithPath("message").type(STRING).description("응답 메시지"),
                        fieldWithPath("data.categories[*].id").type(NUMBER).description("카테고리 아이디"),
                        fieldWithPath("data.categories[*].name").type(STRING).description("카테고리 이름"),
                        fieldWithPath("data.categories[*].imageUrl").type(STRING).description("카테고리 이미지 URL")
                )));
    }
}
