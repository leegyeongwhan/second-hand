package com.secondhand.web.dto.response;

import com.secondhand.domain.login.Token;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
public class MemberProfileResponse {

    private final String profileImgUrl;
    private final String nickName;
    private final String type;
    private final Token token;

    public static MemberProfileResponse fromMember(MemberLoginResponse member) {
        return MemberProfileResponse.builder()
                .profileImgUrl(member.getImgUrl())
                .nickName(member.getName())
                .type(member.getGetOauthProvider())
                .token(member.getJwtToken()).build();
    }
}
