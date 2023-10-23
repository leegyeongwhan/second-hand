package com.secondhand.documentation;

import com.secondhand.domain.oauth.OAuthProvider;
import com.secondhand.service.AuthService;
import com.secondhand.service.TokenService;
import com.secondhand.web.dto.login.AuthToken;
import com.secondhand.web.dto.login.request.LoginRequest;
import com.secondhand.web.dto.login.request.SignUpRequest;
import com.secondhand.web.dto.login.response.LoginResponse;
import com.secondhand.web.dto.login.response.UserResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import static com.secondhand.documentation.ConstraintsHelper.withPath;
import static javax.management.openmbean.SimpleType.BOOLEAN;
import static javax.management.openmbean.SimpleType.STRING;
import static javax.swing.text.html.parser.DTDConstants.NUMBER;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthDocumentationTest extends DocumentationTestSupport {

    @Autowired
    private AuthService authService;

    @Autowired
    private TokenService tokenService;

    @DisplayName("회원가입")
    @Test
    void signup() throws Exception {
        // given

        SignUpRequest request = new SignUpRequest("gamja");
        String requestJson = objectMapper.writeValueAsString(request);

        willDoNothing().given(authService)
                .signUp(any(OAuthProvider.class), any(SignUpRequest.class), anyString(), anyString());

        // when
        var response = mockMvc.perform(request(HttpMethod.POST, "/api/auth/kakao/signup") // URL 수정
                .header("User-Agent", "your-user-agent-value")
                .param("code", "authorization-code")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON));  // 요청의 컨텐츠 타입을 JSON으로 설정합니다.
        // then
        var resultActions = response
                .andExpect(status().isCreated())
                .andExpect(jsonPath("httpStatus").value(201))
                .andExpect(jsonPath("message").value("소셜 가입"))
                .andExpect(jsonPath("apiStatus").value("20000"))
                .andExpect(jsonPath("success").value("true"));

        // docs
        resultActions.andDo(document("auth/kakao/signup",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestHeaders(
                        headerWithName(HttpHeaders.USER_AGENT).description("유저 에이전트")
                ),
                requestParameters(
                        parameterWithName("code").description("OAuth 서버에서 받은 인가코드")
                ),
                requestFields(
                        withPath("loginName", SignUpRequest.class).type(STRING)
                                .description("아이디는 2자 ~ 12자여야 합니다.)")
                ),
                responseFields(
                        fieldWithPath("httpStatus").type(NUMBER).description("응답코드"),
                        fieldWithPath("message").type(STRING).description("응답 메시지"),
                        fieldWithPath("apiStatus").type(NUMBER).description("메서드 상태 코드"),
                        fieldWithPath("success").type(BOOLEAN).description("성공 여부"),
                        fieldWithPath("data").description("응답 데이터").optional()
                )
        ));
    }

    @DisplayName("로그인")
    @Test
    void login() throws Exception {
        // given
        LoginRequest request = new LoginRequest("gamja");
        LoginResponse loginResponse = new LoginResponse(
                new AuthToken("access-token", "refresh-token"),
                new UserResponse("gamja",
                        "profileUrl"));

        given(authService.login(any(OAuthProvider.class), any(LoginRequest.class), anyString()))
                .willReturn(loginResponse);

        // when
        var response = mockMvc.perform(post("/api/auth/kakao/login")
                .param("code", "authorization-code")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        var resultActions = response
                .andExpect(status().isOk())
                .andExpect(jsonPath("httpStatus").value(200))
                .andExpect(jsonPath("message").value("카카오 로그인"))
                .andExpect(jsonPath("apiStatus").value("20000"))
                .andExpect(jsonPath("success").value("true"))
                .andExpect(jsonPath("data.jwt.accessToken").value("access-token"))
                .andExpect(jsonPath("data.jwt.refreshToken").value("refresh-token"))
                .andExpect(jsonPath("data.user.loginId").value("gamja"))
                .andExpect(jsonPath("data.user.profileUrl").value("profileUrl"));

        // docs
        resultActions.andDo(document("auth/kakao/login",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestParameters(
                        parameterWithName("code").description("OAuth 서버에서 받은 인가코드")
                ),
                requestFields(
                        withPath("loginName", LoginRequest.class).description("로그인 아이디")
                ),
                responseFields(
                        fieldWithPath("httpStatus").type(NUMBER).description("응답코드"),
                        fieldWithPath("message").type(STRING).description("응답 메시지"),
                        fieldWithPath("apiStatus").type(NUMBER).description("메서드 상태 코드"),
                        fieldWithPath("success").type(BOOLEAN).description("성공 여부"),
                        fieldWithPath("data.jwt.accessToken").type(JsonFieldType.STRING).description("액세스 토큰"),
                        fieldWithPath("data.jwt.refreshToken").type(JsonFieldType.STRING).description("리프레시 토큰"),
                        fieldWithPath("data.user.loginId").type(JsonFieldType.STRING).description("로그인 아이디"),
                        fieldWithPath("data.user.profileUrl").type(JsonFieldType.STRING).description("프로필 이미지 URL")
                )
        ));
    }


}
