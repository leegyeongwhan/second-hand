package com.secondhand.presentation.filter;

import com.secondhand.exception.v2.ErrorMessage;
import com.secondhand.exception.v2.UnAuthorizedException;
import com.secondhand.infrastructure.jwt.AuthorizationExtractor;
import com.secondhand.infrastructure.jwt.JwtTokenProvider;
import com.secondhand.presentation.support.AuthenticationContext;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.filter.OncePerRequestFilter;


@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    private final List<String> excludeUrlPatterns =
            List.of("/api/auth/**", "/api/categories");
    private final List<String> excludeGetUrlPatterns =
            List.of("/api/products/**");
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationContext authenticationContext; //이게 제대로 들어와야 하는거죠

    public JwtFilter(JwtTokenProvider jwtTokenProvider, AuthenticationContext authenticationContext) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationContext = authenticationContext;
    }


    //필터적용여뷰
    //필터링에서 제외시키고 싶은 request에서 true를 반환 하면 된다.
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        HttpMethod method = HttpMethod.resolve(request.getMethod());
        log.debug(" shouldNotFilter 수행  = {}", method);

        if (method == HttpMethod.GET && isExcludeGetUrl(request.getRequestURI())) {
            extractToken(request).ifPresentOrElse(
                    token -> {
                        jwtTokenProvider.validateToken(token);
                        authenticationContext.setMemberId(jwtTokenProvider.extractClaims(token));
                        log.debug(" shouldNotFilter 수행 id  = {}", authenticationContext.getMemberId());
                    },
                    () -> authenticationContext.setMemberId(Map.of("memberId", -1L)));
            return true;
        }

        //일반적인 경우 (GET이 아니거나, 특정 URI 패턴에 매칭되지 않는 경우):
        // excludeUrlPatterns에 정의된 URI 패턴 중에서 현재 요청의 URI와 매치되는지를 확인합니다.
        // 만약 매치되면 필터링을 수행하지 않고, true를 반환하여 요청을 무시합니다.
        //oauth,카테고리를 제외하고 필터링 거친다
        return excludeUrlPatterns.stream()
                .anyMatch(pattern -> pathMatcher.match(pattern, request.getRequestURI()));
    }

    private boolean isExcludeGetUrl(String uri) {
        return excludeGetUrlPatterns.stream()
                .anyMatch(pattern -> pathMatcher.match(pattern, uri));
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        log.debug(" doFilter 수행");
        log.debug(" shouldNotFilter 수행  = {}", authenticationContext);

        if (CorsUtils.isPreFlightRequest(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = extractToken(request)
                .orElseThrow(() -> new UnAuthorizedException(ErrorMessage.INVALID_AUTH_HEADER));
        //    jwtProvider.validateBlackToken(token);
        jwtTokenProvider.validateToken(token);
        authenticationContext.setMemberId(jwtTokenProvider.extractClaims(token));
        log.debug("검증된 회원id = {}", authenticationContext.getMemberId());
        filterChain.doFilter(request, response);
    }

    private Optional<String> extractToken(HttpServletRequest request) {
        return AuthorizationExtractor.extract(request);
    }
}
