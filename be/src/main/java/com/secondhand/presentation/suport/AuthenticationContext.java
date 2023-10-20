package com.secondhand.presentation.suport;


import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
public class AuthenticationContext {

    private Long memberId;

    public Optional<Long> getMemberId() {
        return Optional.ofNullable(memberId);
    }

    public void setMemberId(Map<String, Object> claims) {
        this.memberId = Long.valueOf(claims.get("memberId").toString());
    }
}
