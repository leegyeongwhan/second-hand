package com.secondhand.domain.product.repository.Image;

import com.secondhand.domain.image.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long>, ImageRepositoryCustom {

    List<Image> findByProductId(Long productId);

    //void deleteByProduct_IdAndImgUrlIn(Long itemId, List<String> deleteImgUrls);

    @Modifying
    @Query("DELETE FROM Image image  WHERE image.product.id = :productId")
    void deleteByProductId(@Param("productId") Long productId);
}
