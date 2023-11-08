package com.secondhand.presentation.support;

import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationContext {

    private Long memberId;

    public Optional<Long> getMemberId() {
        return Optional.ofNullable(memberId);
    }

    //일단 이 컨텍스트 이컴포넌트를  주입 받아야하잖아요 config에서
    public void setMemberId(Map<String, Object> claims) {
        this.memberId = Long.valueOf(claims.get("memberId").toString());
    }
}
