package com.secondhand.presentation.interceptor;

import com.secondhand.exception.v2.ErrorMessage;
import com.secondhand.exception.v2.UnAuthorizedException;
import com.secondhand.presentation.suport.AuthenticationContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoginInterceptor implements HandlerInterceptor {

    private static final Long NOT_LOGIN_MEMBER_ID = -1L;
    private static final String NOT_LOGIN_DEFAULT_REGION = "역삼1동";

    private final AuthenticationContext authenticationContext;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!HttpMethod.GET.matches(request.getMethod())) {
            return true;
        }

        String region = Optional.ofNullable(request.getParameter("town"))
                .orElse("역삼1동");

        Long memberId = authenticationContext.getMemberId()
                .orElseThrow(() -> new UnAuthorizedException(ErrorMessage.NOT_LOGIN));
        if (!region.equals(NOT_LOGIN_DEFAULT_REGION) && NOT_LOGIN_MEMBER_ID.equals(memberId)) {
            throw new UnAuthorizedException(ErrorMessage.NOT_LOGIN, "로그인되지 않은 상태에서는 역삼 1동 지역만을 볼 수 있습니다.");
        }
        return true;
    }
}
