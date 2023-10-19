package com.secondhand.domain.memberToken;

import com.secondhand.domain.member.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_token_id")
    private Long id;

    private String memberToken;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public MemberToken(String memberToken, Member member) {
        this.memberToken = memberToken;
        this.member = member;
    }

    public void update(String refreshToken, Member member) {
        this.memberToken = refreshToken;
        this.member = member;
    }

    @Builder
    private MemberToken(Member member, String memberToken) {
        this.member = member;
        this.memberToken = memberToken;
    }
}
