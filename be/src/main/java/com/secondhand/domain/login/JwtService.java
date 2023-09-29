package com.secondhand.domain.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.secondhand.web.dto.response.MemberProfileResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {
    private final ObjectMapper objectMapper;

    public String setTokenHeader(MemberProfileResponse member, HttpServletResponse response) throws IOException {
        String token = member.getType() + " " + member.getToken();
        //TODO: header 에 토큰 set
        response.setHeader(token, token);
        return token;
    }

    private String getToken(String authorization) {
        return authorization.split(" ")[1];
    }
}
