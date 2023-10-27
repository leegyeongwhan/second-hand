package com.secondhand.application.member;

import com.secondhand.application.ApplicationTestSupport;
import com.secondhand.config.FixtureFactory;
import com.secondhand.domain.image.ImageFile;
import com.secondhand.domain.member.Member;
import com.secondhand.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayName("멤버 서비스 테스트")
class MemberServiceTest extends ApplicationTestSupport {

}

