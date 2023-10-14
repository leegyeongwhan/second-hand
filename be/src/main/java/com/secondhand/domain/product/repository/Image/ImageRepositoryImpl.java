package com.secondhand.domain.product.repository.Image;

import com.secondhand.domain.image.Image;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ImageRepositoryImpl implements ImageRepositoryCustom {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public void saveAllImages(List<Image> images) {
        // IDENTITY 방식의 한계로 bulk insert query 직접 구현
        String sql = "INSERT INTO image "
                + "(img_url, product_id) VALUES (:imgUrl, :productId)";
        MapSqlParameterSource[] params = images.stream()
                .map(image -> new MapSqlParameterSource()
                        .addValue("imgUrl", image.getImgUrl())
                        .addValue("productId", image.getProduct().getId()))
                .collect(Collectors.toList())
                .toArray(MapSqlParameterSource[]::new);
        jdbcTemplate.batchUpdate(sql, params);
    }
}
