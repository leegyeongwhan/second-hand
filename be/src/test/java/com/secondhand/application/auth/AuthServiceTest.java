package com.secondhand.application.auth;

import com.secondhand.application.ApplicationTestSupport;
import com.secondhand.config.FixtureFactory;
import com.secondhand.domain.member.Member;
import com.secondhand.domain.member.MemberProfile;
import com.secondhand.domain.memberToken.MemberToken;
import com.secondhand.domain.oauth.OAuthProvider;
import com.secondhand.service.AuthService;
import com.secondhand.web.dto.login.UserProfile;
import com.secondhand.web.dto.login.request.LoginRequest;
import com.secondhand.web.dto.login.response.LoginResponse;
import com.secondhand.web.dto.response.OauthTokenResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@DisplayName("Auth 서비스 통합 테스트")
public class AuthServiceTest extends ApplicationTestSupport {

    @Autowired
    private AuthService authService;

    @Nested
    class Login {

        @DisplayName("로그인 성공시 토큰정보와 유저 정보를 반환한다.")
        @Test
        void givenLoginData_whenLogin_thenSuccess() {
            // given
            given(kakaoRequester.getToken(anyString()))
                    .willReturn(new OauthTokenResponse("xxx.xxx.xxx", "scope", "bearer"));
            given(kakaoRequester.getUserProfile(any(OauthTokenResponse.class)))
                    .willReturn(UserProfile.builder()
                            .email("gamja@naver.com")
                            .profileUrl("url")
                            .build());

            LoginRequest request = new LoginRequest("gamja");

            //supportRepository.save(FixtureFactory.createMemberProfile());
            MemberProfile profile = supportRepository.save(MemberProfile.builder()
                    .memberEmail("gamja@naver.com")
                    .build());

            supportRepository.save(Member.builder()
                    .loginName("gamja")
                    .imgUrl("url")
                    .oauthProvider("KAKAO")
                    .memberProfile(profile)
                    .build());
            // when
            LoginResponse response = authService.login(OAuthProvider.KAKAO, request, "code");

            // then
            Optional<MemberToken> token = supportRepository.findById(MemberToken.class, 1L);
            assertAll(
                    () -> assertThat(token).isPresent(),
                    () -> assertThat(response.getJwt().getAccessToken()).isNotBlank(),
                    () -> assertThat(response.getJwt().getRefreshToken()).isNotBlank(),
                    () -> assertThat(response.getUser().getLoginId()).isNotBlank(),
                    () -> assertThat(response.getUser().getProfileUrl()).isNotBlank()
            );
        }

//        @DisplayName("리프레시 토큰이 존재하는 사용자가 다시 로그인을 시도할 때 존재하는 리프레시 토큰을 삭제하고 새로운 리프레시 토큰을 저장한다.")
//        @Test
//        void givenLoginDataAndAlreadyHasRefreshToken_whenLogin_thenSuccess() {
//            // given
//            mockingOAuthInfo();
//
//            LoginRequest request = new LoginRequest("joy");
//            supportRepository.save(Member.builder()
//                    .email("joy@naver.com")
//                    .loginId("joy")
//                    .profileUrl("url")
//                    .build());
//            supportRepository.save(RefreshToken.builder()
//                    .memberId(1L)
//                    .token("token.token.token")
//                    .build());
//
//            // when
//            authService.login(OAuthProvider.NAVER, request, "code");
//
//            // then
//            assertThat(supportRepository.findById(RefreshToken.class, 1L).get().getToken())
//                    .isNotEqualTo("token.token.token");
//        }
//
//        @DisplayName("아이디는 존재하지만 해당 아이디의 이메일과 Naver 이메일 정보가 일치하지 않는 로그인 정보가 주어지면 예외를 던진다.")
//        @Test
//        void givenInvalidLoginData_whenLogin_thenThrowsException() {
//            // given
//            mockingOAuthInfo();
//
//            LoginRequest request = new LoginRequest("joy");
//            supportRepository.save(Member.builder()
//                    .email("joooy@naver.com")
//                    .loginId("joy")
//                    .profileUrl("url")
//                    .build());
//
//            // when & then
//            assertThatThrownBy(() -> authService.login(OAuthProvider.NAVER, request, "code"))
//                    .isInstanceOf(UnAuthorizedException.class)
//                    .extracting("errorCode").isEqualTo(ErrorCode.INVALID_LOGIN_DATA);
//        }
//
//        @DisplayName("아이디가 존재하지 않아 로그인할 때 예외를 던진다.")
//        @Test
//        void givenNotExistsLoginId_whenLogin_thenThrowsException() {
//            // given
//            mockingOAuthInfo();
//            LoginRequest request = new LoginRequest("joy");
//
//            // when & then
//            assertThatThrownBy(() -> authService.login(OAuthProvider.NAVER, request, "code"))
//                    .isInstanceOf(UnAuthorizedException.class)
//                    .extracting("errorCode").isEqualTo(ErrorCode.INVALID_LOGIN_DATA);
//        }
//
//        private void mockingOAuthInfo() {
//
//        }
    }
}
