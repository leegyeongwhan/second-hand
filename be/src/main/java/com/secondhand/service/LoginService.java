package com.secondhand.service;

import com.secondhand.domain.login.JwtService;
import com.secondhand.domain.login.JwtTokenProvider;
import com.secondhand.domain.login.Token;
import com.secondhand.domain.member.*;
import com.secondhand.domain.oauth.RequestOAuthInfoService;
import com.secondhand.domain.oauth.dto.OAuthInfoResponse;
import com.secondhand.domain.oauth.dto.req.OAuthLoginParams;
import com.secondhand.exception.JoinException;
import com.secondhand.exception.MemberNotFoundException;
import com.secondhand.exception.token.RefreshTokenNotFoundException;
import com.secondhand.web.dto.requset.JoinRequest;
import com.secondhand.web.dto.requset.SignupSocialRequest;
import com.secondhand.web.dto.response.MemberLoginResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LoginService {
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtService jwtService;
    private final MemberRepository memberRepository;
    private final MemberPasswordRepository memberPasswordRepository;
    private final MemberProfileRepository memberProfileRepository;

    private final MemberTokenRepository memberTokenRepository;
    private final TownService townService;
    private final RequestOAuthInfoService requestOAuthInfoService;

    @Transactional
    public MemberLoginResponse login(OAuthLoginParams params, String userAgent) throws IOException {
        OAuthInfoResponse oAuthInfoResponse = requestOAuthInfoService.request(params, userAgent);

        //이미 있는 멤버라면 토큰을 업데이트 해주고 아니라면 새로만든다
        if (isMemberEmailExists(oAuthInfoResponse.getEmail())) {
            Member member = findMemberByEmail(oAuthInfoResponse.getEmail());
            Token jwtToken = jwtTokenProvider.createToken(member);
            MemberToken memberToken = memberTokenRepository.findByMemberId(member.getId())
                    .orElseThrow(RefreshTokenNotFoundException::new);
            memberToken.update(jwtToken.getRefreshToken(), member);
            log.debug("jwtToken = {}", jwtToken);
            log.debug("기존에 있던 회원 ==========================");
            return MemberLoginResponse.of(member, jwtToken);
        }

        // 이메일이없는 임시회원 깃허브는 이메일을 안줌  , 임시토큰을준다
        else if (oAuthInfoResponse.getEmail() == null) {
            log.debug("깃허브 로그인 임시회원 저장 ================");
            Member findMember = findMemberById(0L);
            log.debug("member id  = {}", findMember.getId());
            findMember.update(oAuthInfoResponse);
            Token jwtToken = jwtTokenProvider.createToken(findMember);
            MemberToken memberToken = memberTokenRepository.findByMemberId(findMember.getId())
                    .orElseThrow(RefreshTokenNotFoundException::new);
            memberToken.update(jwtToken.getRefreshToken(), findMember);
            log.debug("getAccessToken토큰  = {}", jwtToken.getAccessToken());
            log.debug("getRefreshToken토큰  = {}", jwtToken.getRefreshToken());
            return MemberLoginResponse.of(findMember, jwtToken);
        }

        // 새로운 회원
        log.debug("오쓰로부터 받은 닉네임 = {}", oAuthInfoResponse.getNickname());
        log.debug("오쓰로부터 받은 닉네임 깃허브는 안줌 = {}", oAuthInfoResponse.getEmail());
        MemberProfile memberProfile = memberProfileRepository.save(new MemberProfile(oAuthInfoResponse.getEmail()));
        Member member = memberRepository.save(Member.create(oAuthInfoResponse, memberProfile, townService.findById(1L)));
        Token jwtToken = jwtTokenProvider.createToken(member);
        MemberToken memberToken = memberTokenRepository.save(new MemberToken(jwtToken.getRefreshToken(), member));
        log.debug("새로 만든 jwt 토큰 = {}", jwtToken);
        log.debug("회원이 저장할 refresh token = {}", memberToken.getMemberToken());
        log.debug("새로 생긴  회원 ==========================");
        return MemberLoginResponse.of(member, jwtToken);
    }

    @Transactional
    public MemberLoginResponse join(JoinRequest joinRequest) {
        if (isMemberEmailExists(joinRequest.getMemberEmail())) {
            throw new JoinException("이미 존재하는 회원입니다");
        }

        MemberPassword memberPassword = memberPasswordRepository.save(new MemberPassword(joinRequest.getPassword()));
        MemberProfile memberProfile = memberProfileRepository.save(new MemberProfile(joinRequest.getMemberEmail()));
        Member member = memberRepository.save(Member.create(joinRequest.getNickName(),
                "GENERAL", memberProfile, memberPassword, townService.findById(1L)));

        Token jwtToken = jwtTokenProvider.createToken(member);
        memberTokenRepository.save(new MemberToken(jwtToken.getRefreshToken(), member));
        log.debug("jwt token = {}", jwtToken);
        log.debug("새로운 맴버 생성 = {}", member);
        return MemberLoginResponse.of(member, jwtToken);
    }

    @Transactional
    public MemberLoginResponse signupEmail(long userId, SignupSocialRequest signupSocialRequest) {
        Member member = findMemberById(userId);
        log.debug("회원의 이메일 = {}", member.getMemberProfile().getMemberEmail());
        log.debug("입력 받은 이메일 = {}", signupSocialRequest.getEmail());
        log.debug("이메일이있는지 체크 = {}", memberProfileRepository.findMemberByMemberEmail(signupSocialRequest.getEmail()).isEmpty());
        log.debug("이메일이 기본 레코드인지 체크 = {}", member.getMemberProfile().getMemberEmail().equals("0"));

        if (userId == 0L && memberProfileRepository.findMemberByMemberEmail(signupSocialRequest.getEmail()).isEmpty() &&
                member.getMemberProfile().getMemberEmail().equals("0")) {

            MemberProfile memberProfile = memberProfileRepository.save(new MemberProfile(signupSocialRequest.getEmail()));
            Member saveMember = memberRepository.save(Member.toEntity(member.getLoginName(), member.getOauthProvider(), member.getImgUrl(),
                    memberProfile, member.getMemberPassword()));

            Token jwtToken = jwtTokenProvider.createToken(saveMember);
            memberTokenRepository.save(new MemberToken(jwtToken.getRefreshToken(), saveMember));

            resetRecord(userId);
            return MemberLoginResponse.of(member, jwtToken);
        }
        throw new JoinException("이미 이메일이 존재하는 회원입니다");
    }

    private void resetRecord(long userId) {
        Member member = findMemberById(userId);
        MemberToken temporaryMemberToken = memberTokenRepository.findByMemberId(userId)
                .orElseThrow(RefreshTokenNotFoundException::new);
        temporaryMemberToken.update("0", member);
        member.resetUpdateEntity();
    }

    @Transactional
    public void logout(long userId) {
        MemberToken memberToken = memberTokenRepository.findByMemberId(userId).orElseThrow(RefreshTokenNotFoundException::new);
        memberTokenRepository.delete(memberToken);
    }

    private boolean isMemberEmailExists(String email) {
        return memberProfileRepository.findMemberByMemberEmail(email).isPresent();
    }

    public Member findMemberById(long userId) {
        return memberRepository.findById(userId).orElseThrow(MemberNotFoundException::new);
    }

    private Member findMemberByEmail(String email) {
        MemberProfile memberProfile = memberProfileRepository.findMemberByMemberEmail(email).orElseThrow(MemberNotFoundException::new);
        return memberProfile.getMember();
    }
}
