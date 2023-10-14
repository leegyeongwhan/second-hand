package com.secondhand.domain.product.repository.Image;

import com.secondhand.domain.image.Image;

import java.util.List;

public interface ImageRepositoryCustom {
    void saveAllImages(List<Image> images);
}
