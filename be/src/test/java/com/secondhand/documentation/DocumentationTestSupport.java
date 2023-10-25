package com.secondhand.documentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.secondhand.infrastructure.jwt.JwtTokenProvider;
import com.secondhand.presentation.support.AuthenticationContext;
import com.secondhand.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@MockBean({
        CategoryService.class,
        ProductService.class,
        ProductQueryService.class,
        TownService.class,
        TokenService.class,
        AuthService.class,
        ImageService.class,
        MemberService.class
})
@DocumentationTest
public abstract class DocumentationTestSupport {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected JwtTokenProvider jwtProvider;

    @MockBean
    protected AuthenticationContext authenticationContext;

    @BeforeEach
    void setUp() {
        given(jwtProvider.createAccessToken(anyLong())).willReturn("gfd.gfdgfdgfd.gfdgfd-dsafds3");
        given(authenticationContext.getMemberId()).willReturn(Optional.of(1L));
    }
}


