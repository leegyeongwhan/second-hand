package com.secondhand.service;

import com.secondhand.domain.image.Image;
import com.secondhand.domain.image.ImageFile;
import com.secondhand.domain.image.S3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ImageService {

    private final S3Uploader s3Uploader;

    @Transactional
    public List<String> uploadImageList(List<MultipartFile> images) {
        if (images == null) {
            return List.of();
        }
        List<ImageFile> imageFiles = images.stream()
                .map(ImageFile::from)
                .collect(Collectors.toList());
        return s3Uploader.uploadImageFiles(imageFiles);
    }

    @Transactional
    public String upload(MultipartFile multipartFile) {
        ImageFile imageFile = ImageFile.from(multipartFile);
        return s3Uploader.uploadImageFile(imageFile);
    }

    @Async("imageThreadExecutor")
    public void deleteImages(List<Image> itemImages) {
        List<String> imageUrls = itemImages.parallelStream()
                .map(Image::getImgUrl)
                .collect(Collectors.toList());
        s3Uploader.deleteImages(imageUrls);
    }
}
