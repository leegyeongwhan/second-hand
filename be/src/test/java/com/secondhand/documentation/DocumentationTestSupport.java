package com.secondhand.documentation;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import com.secondhand.domain.chat.service.ChatRoomService;
import com.secondhand.infrastructure.jwt.JwtTokenProvider;
import com.secondhand.presentation.suport.AuthenticationContext;
import com.secondhand.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@MockBean({
       // ChatLogService.class,
        LoginService.class,
        CategoryService.class,
        ChatRoomService.class,
        ImageService.class,
        ProductService.class,
        MemberService.class,
        TownService.class,
        ProductQueryService.class
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
