//package com.secondhand.documentation;
//
//import com.secondhand.service.MemberService;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.MediaType;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
//import org.springframework.web.multipart.MultipartFile;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.BDDMockito.given;
//import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
//import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
//import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
//import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
//import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
//import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
//import static org.springframework.restdocs.payload.JsonFieldType.STRING;
//import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
//import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
//import static org.springframework.restdocs.request.RequestDocumentation.*;
//import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//public class MemberDocumentationTest extends DocumentationTestSupport {
//    @Autowired
//    private MemberService memberService;
//
//    @DisplayName("회원 프로필 이미지 수정")
//    @Test
//    void modifyProfile() throws Exception {
//        // given
//        MockMultipartFile profile = new MockMultipartFile("updateImageFile", "image.png", "image/png",
//                "<<png data>>".getBytes());
//
//        given(memberService.modifyProfileImage(any(MultipartFile.class), anyLong()))
//                .willReturn(new ModifyProfileResponse("profile-image"));
//
//        // when
//        var builder = RestDocumentationRequestBuilders.multipart("/api/members/{loginId}", "bruni");
//        builder.with(mockRequest -> {
//            mockRequest.setMethod(HttpMethod.PUT.name());
//            return mockRequest;
//        });
//
//        var response = mockMvc.perform(builder
//                .file(profile)
//                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtProvider.createAccessToken(1L))
//                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE));
//
//        // then
//        var resultActions = response.andExpect(status().isOk())
//                .andExpect(jsonPath("statusCode").value(200))
//                .andExpect(jsonPath("message").value("성공했습니다."))
//                .andExpect(jsonPath("data.profileImageUrl").value("profile-image"));
//
//        // docs
//        resultActions.andDo(document("member/modify-profile",
//                preprocessRequest(prettyPrint()),
//                preprocessResponse(prettyPrint()),
//                requestHeaders(
//                        headerWithName(HttpHeaders.AUTHORIZATION).description("액세스 토큰을 담는 인증 헤더")
//                ),
//                requestParts(
//                        partWithName("updateImageFile").description("수정할 프로필 이미지 파일")
//                ),
//                pathParameters(
//                        parameterWithName("loginId").description("로그인 아이디 ex. bruni")
//                ),
//                responseFields(
//                        fieldWithPath("statusCode").type(NUMBER).description("응답 상태 코드"),
//                        fieldWithPath("message").type(STRING).description("응답 메시지"),
//                        fieldWithPath("data.profileImageUrl").type(STRING).description("수정된 프로필 이미지 URL")
//                )
//        ));
//    }
//}
