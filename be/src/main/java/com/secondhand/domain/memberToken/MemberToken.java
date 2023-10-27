package com.secondhand.domain.memberToken;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberToken {
    @Id
    private Long memberId;

    private String memberToken;

    @Builder
    private MemberToken(Long memberId, String memberToken) {
        this.memberId = memberId;
        this.memberToken = memberToken;
    }
}
