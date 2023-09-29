package com.secondhand.web.dto.response;

import com.secondhand.domain.member.Member;
import com.secondhand.domain.login.Token;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MemberLoginResponse {

    private final String name;
    private final String imgUrl;
    private final String getOauthProvider;
    private final Token jwtToken;

    public static MemberLoginResponse of(final Member member, final Token jwtToken) {
        return MemberLoginResponse.builder()
                .name(member.getLoginName())
                .imgUrl(member.getImgUrl())
                .jwtToken(jwtToken)
                .getOauthProvider(member.getOauthProvider())
                .build();
    }
}
