package com.secondhand.web.dto.login.request;

import com.secondhand.domain.member.MemberProfile;
import com.secondhand.domain.town.Town;
import com.secondhand.web.dto.login.UserProfile;
import com.secondhand.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {

    @NotBlank
    @Size(min = 2, max = 12, message = "아이디는 2자 ~ 12자여야 합니다.")
    private String loginName;
//    @Size(min = 1, max = 2, message = "주소는 최소 1개, 최대 2개까지 들어올 수 있습니다.")
//    private List<Long> addressIds;

    public Member toMemberEntity(UserProfile userProfile, String oAuthProvider, MemberProfile memberProfile, Town town) {
        return Member.builder()
                .loginName(this.loginName)
                .oauthProvider(oAuthProvider)
                .imgUrl(userProfile.getProfileUrl())
                .memberProfile(memberProfile)
                .memberPassword(null)
                .mainTown(town)
                .build();
    }
}
