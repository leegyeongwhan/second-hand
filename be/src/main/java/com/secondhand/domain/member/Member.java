package com.secondhand.domain.member;

import com.secondhand.domain.interested.Interested;
import com.secondhand.domain.town.Town;
import com.secondhand.domain.oauth.dto.OAuthInfoResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String loginName;
    private String imgUrl;
    private String oauthProvider;

    // 프록시 지연로딩이 안되고 즉시로딩 되는 문제가 있다.
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_profile_id")
    private MemberProfile memberProfile;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_password_id")
    private MemberPassword memberPassword;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "main_town_id")
    private Town mainTown;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_town_id")
    private Town subTown;

    @OneToMany(mappedBy = "member")
    private Set<Interested> interesteds = new HashSet<>();


    //Ouath로그인
    public static Member create(OAuthInfoResponse memberInfo, MemberProfile memberProfile, Town mainTown) {
        return Member.builder()
                .loginName(memberInfo.getNickname())
                .imgUrl(memberInfo.getAvatarUrl())
                .memberProfile(memberProfile)
                .oauthProvider(memberInfo.getOAuthProvider().name())
                .mainTown(mainTown)
                .build();
    }

    //일반로그인
    public static Member create(String loginName, String oauthProvider,
                                MemberProfile memberProfile, MemberPassword memberPassword, Town mainTown) {
        return Member.builder()
                .loginName(loginName)
                .oauthProvider(oauthProvider)
                .memberProfile(memberProfile)
                .memberPassword(memberPassword)
                .mainTown(mainTown)
                .build();
    }

    public static Member toEntity(String loginName, String oauthProvider, String imgUrl,
                                  MemberProfile memberProfile, MemberPassword memberPassword) {
        return Member.builder()
                .loginName(loginName)
                .oauthProvider(oauthProvider)
                .imgUrl(imgUrl)
                .memberProfile(memberProfile)
                .memberPassword(memberPassword)
                .build();
    }

    //깃허브 oauth 로그인 제대로처리
    public void update(OAuthInfoResponse memberInfo) {
        this.loginName = memberInfo.getNickname();
        this.imgUrl = memberInfo.getAvatarUrl();
        this.oauthProvider = memberInfo.getOAuthProvider().name();
    }

    public void changeTown(Town town) {
        this.mainTown = town;
    }

    public void updateTowns(Town mainTown, Town subTown) {
        this.mainTown = mainTown;
        this.subTown = subTown;
    }

    public void updateMainTowns(Town mainTown) {
        this.mainTown = mainTown;
    }

    public void updateNickName(String nickName) {
        this.loginName = nickName;
    }

    public void updateEmail(String email) {
        this.memberProfile.setEmail(email);
    }

    public boolean checkProductIsMine(long id) {
        return this.getId() == id;
    }

    public void resetUpdateEntity() {
        this.loginName = "0";
        this.imgUrl = "0";
        this.oauthProvider = "0";
    }


    public boolean isSameEmail(String email) {
        return this.memberProfile.getMemberEmail().equals(email);
    }
}
