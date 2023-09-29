package com.secondhand.util;

import com.secondhand.domain.member.Member;
import com.secondhand.domain.member.MemberRepository;
import com.secondhand.exception.MemberNotFoundException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberServiceUtils {

    public static Member findByMember(MemberRepository memberRepository, long userId) {
        return memberRepository.findById(userId).orElseThrow(MemberNotFoundException::new);
    }
}
