package com.secondhand.service;

import com.secondhand.domain.login.OAuthRequester;
import com.secondhand.domain.member.MemberProfile;
import com.secondhand.domain.member.MemberProfileRepository;
import com.secondhand.domain.memberToken.MemberToken;
import com.secondhand.domain.oauth.RequestOAuthInfoService;
import com.secondhand.domain.town.Town;
import com.secondhand.exception.v2.UnAuthorizedException;
import com.secondhand.web.dto.login.AuthToken;
import com.secondhand.web.dto.login.UserProfile;
import com.secondhand.domain.member.Member;
import com.secondhand.domain.member.MemberRepository;
import com.secondhand.domain.memberToken.MemberTokenRepository;
import com.secondhand.domain.oauth.OAuthProvider;
import com.secondhand.exception.v2.DuplicatedException;
import com.secondhand.exception.v2.ErrorMessage;
import com.secondhand.infrastructure.jwt.JwtTokenProvider;
import com.secondhand.web.dto.login.request.LoginRequest;
import com.secondhand.web.dto.login.request.SignUpRequest;
import com.secondhand.web.dto.login.response.LoginResponse;
import com.secondhand.web.dto.login.response.UserResponse;
import com.secondhand.web.dto.response.OauthTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AuthService {

    private final ImageService imageService;
    //  private final ResidenceService residenceService;
    private final MemberTokenRepository tokenRepository;
    private final MemberRepository memberRepository;
    private final MemberProfileRepository memberProfileRepository;
    private final JwtTokenProvider jwtProvider;
    //  private final RedisService redisService;
    private final RequestOAuthInfoService requestOAuthInfoService;
    private final TownService townService;

    @Transactional
    public LoginResponse login(OAuthProvider oAuthProvider, LoginRequest request, String code) {
        OAuthRequester oAuthRequester = oAuthProvider.getOAuthRequester();
        OauthTokenResponse tokenResponse = oAuthRequester.getToken(code);
        UserProfile userProfile = oAuthRequester.getUserProfile(tokenResponse);

        Member member = verifyUser(request, userProfile);
        Long memberId = member.getId();

        String refreshToken = jwtProvider.createRefreshToken(member.getId());
        tokenRepository.deleteByMemberId(member.getId());

        tokenRepository.save(MemberToken.builder()
                .member(member)
                .memberToken(refreshToken)
                .build());

        //  List<AddressData> addressData = residenceService.readResidenceOfMember(memberId);
        return new LoginResponse(
                new AuthToken(jwtProvider.createAccessToken(memberId), refreshToken),
                new UserResponse(member.getLoginName(), member.getImgUrl(), String.valueOf(oAuthProvider.getOAuthRequester()))
        );
    }

    private Member verifyUser(LoginRequest request, UserProfile userProfile) {
        Member member = memberRepository.findByLoginName(request.getLoginName())
                .orElseThrow(() -> new UnAuthorizedException(ErrorMessage.INVALID_LOGIN_DATA));
        if (!member.isSameEmail(userProfile.getEmail())) {
            throw new UnAuthorizedException(ErrorMessage.INVALID_LOGIN_DATA);
        }
        return member;
    }

    @Transactional
    public void signUp(OAuthProvider oAuthProvider, SignUpRequest request, String code, String userAgent) {
        verifyDuplicated(request);

        //github naver
        OAuthRequester oAuthRequester = oAuthProvider.getOAuthRequester();
        OauthTokenResponse tokenResponse = oAuthRequester.getToken(code);
        UserProfile userProfile = oAuthRequester.getUserProfile(tokenResponse);

//        if (profile != null) {
//            String profileUrl = imageService.upload(profile);
//            userProfile.changeProfileUrl(profileUrl);
//        }

        MemberProfile memberProfile = memberProfileRepository.save(new MemberProfile(userProfile.getEmail()));
        saveMember(request, userProfile, oAuthProvider.getName(), memberProfile);
        //  residenceService.saveResidence(request.getAddressIds(), savedMember);
    }

    private void verifyDuplicated(SignUpRequest request) {
        if (memberRepository.existsByLoginName(request.getLoginName())) {
            throw new DuplicatedException(ErrorMessage.DUPLICATED_LOGIN_ID);
        }
    }

    //    @Transactional
//    public void logout(HttpServletRequest request, String refreshToken) {
//        JwtExtractor.extract(request).ifPresent(token -> {
//            Long expiration = jwtProvider.getExpiration(token);
//            redisService.set(token, "logout", expiration);
//        });
//        tokenRepository.deleteByToken(refreshToken);
//    }
//
//    private Member verifyUser(LoginRequest request, UserProfile userProfile) {
//        Member member = memberRepository.findByLoginId(request.getLoginId())
//                .orElseThrow(() -> new UnAuthorizedException(ErrorCode.INVALID_LOGIN_DATA));
//        if (!member.isSameEmail(userProfile.getEmail())) {
//            throw new UnAuthorizedException(ErrorCode.INVALID_LOGIN_DATA);
//        }
//        return member;
//    }
//
    private Member saveMember(SignUpRequest request, UserProfile userProfile, String oAuthProvider, MemberProfile memberProfile) {
        Town town = townService.findById(1L);
        return memberRepository.save(request.toMemberEntity(userProfile, oAuthProvider, memberProfile, town));
    }
}
