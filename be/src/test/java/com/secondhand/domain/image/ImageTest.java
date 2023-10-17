package com.secondhand.domain.image;

import com.secondhand.exception.BadRequestException;
import com.secondhand.exception.v2.ErrorMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DisplayName("이미지 테스트")
class ImageTest {

    @DisplayName("올바른 이미지 파일이 들어와 ImageFile 인스턴스 생성에 성공한다.")
    @Test
    void givenMockImageFile_whenCreateImageFile_thenSuccess() {
        // given
        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "test-image",
                "image.png",
                MediaType.IMAGE_PNG_VALUE,
                "imageBytes".getBytes(StandardCharsets.UTF_8)
        );

        // when & then
        assertThatCode(() -> ImageFile.from(mockMultipartFile)).doesNotThrowAnyException();
    }

    @DisplayName("지원하지 않는 이미지 확장자가 들어오면 예외를 던진다.")
    @Test
    void givenNotSupportedFileExtension_whenCreateImageFile_thenThrowsException() {
        // given
        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "test-image",
                "image.gif",
                MediaType.IMAGE_GIF_VALUE,
                "imageBytes".getBytes(StandardCharsets.UTF_8)
        );

        // when & then
        assertThatThrownBy(() -> ImageFile.from(mockMultipartFile))
                .isInstanceOf(BadRequestException.class)
                .extracting("errorMessage").isEqualTo(ErrorMessage.INVALID_FILE_EXTENSION);
    }
}
