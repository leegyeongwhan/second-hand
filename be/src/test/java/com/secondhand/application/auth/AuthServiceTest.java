//package com.secondhand.application.auth;
//
//import com.secondhand.application.ApplicationTestSupport;
//import com.secondhand.domain.member.Member;
//import com.secondhand.domain.oauth.OAuthProvider;
//import com.secondhand.service.AuthService;
//import com.secondhand.web.dto.login.UserProfile;
//import com.secondhand.web.dto.login.request.LoginRequest;
//import com.secondhand.web.dto.login.response.LoginResponse;
//import com.secondhand.web.dto.response.OauthTokenResponse;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertAll;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.BDDMockito.given;
//
//@DisplayName("Auth 서비스 테스트")
//public class AuthServiceTest extends ApplicationTestSupport {
//    @Autowired
//    private AuthService authService;
//
//    @Nested
//    class Login {
//
//        @DisplayName("로그인 정보가 주어지면 로그인에 성공해 토큰정보와 유저 정보를 반환한다.")
//        @Test
//        void givenLoginData_whenLogin_thenSuccess() {
//            // given
//            mockingOAuthInfo();
//
//            LoginRequest request = new LoginRequest("감자");
//            supportRepository.save(Member.builder()
//                    .loginName("joy@naver.com")
//                    .l("joy")
//                    .profileUrl("url")
//                    .build());
//
//            // when
//            LoginResponse response = authService.login(OAuthProvider.NAVER, request, "code");
//
//            // then
//            Optional<RefreshToken> token = supportRepository.findById(RefreshToken.class, 1L);
//            assertAll(
//                    () -> assertThat(token).isPresent(),
//                    () -> assertThat(response.getJwt().getAccessToken()).isNotBlank(),
//                    () -> assertThat(response.getJwt().getRefreshToken()).isNotBlank(),
//                    () -> assertThat(response.getUser().getLoginId()).isNotBlank(),
//                    () -> assertThat(response.getUser().getProfileUrl()).isNotBlank()
//            );
//        }
//    }
//
//    private void mockingOAuthInfo() {
//        given(naverRequester.getToken(anyString()))a
//                .willReturn(new OauthTokenResponse("a.a.a", "scope", "bearer"));
//        given(naverRequester.getUserProfile(any(OauthTokenResponse.class)))
//                .willReturn(UserProfile.builder()
//                        .email("joy@naver.com")
//                        .profileUrl("url")
//                        .build());
//
//        given(kakaoRequester.getToken(anyString()))
//                .willReturn(new OauthTokenResponse("a.a.a", "scope", "bearer"));
//        given(kakaoRequester.getUserProfile(any(OauthTokenResponse.class)))
//                .willReturn(UserProfile.builder()
//                        .email("joy@naver.com")
//                        .profileUrl("url")
//                        .build());
//    }
//}
