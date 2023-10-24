package com.secondhand.presentation.controller;

import com.secondhand.service.ImageService;
import com.secondhand.web.dto.response.ImageResponse;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Api(tags = "상품 이미지")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/images")
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/upload")
    public String upload(@RequestParam("image") MultipartFile multipartFile) throws IOException {
        log.debug("image 업로드 요청");

        return imageService.upload(multipartFile);
    }
}
