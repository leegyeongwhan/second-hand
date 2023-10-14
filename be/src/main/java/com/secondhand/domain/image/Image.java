package com.secondhand.domain.image;

import com.secondhand.domain.product.Product;
import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;

@Getter
@Entity
//@Table(name = "PRODUCT_IMG")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_img_id")
    private Long id;

    @NotNull
    private String imgUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Builder
    private Image(String imgUrl, Product product) {
        this.imgUrl = imgUrl;
        this.product = product;
    }

    public static Image of(String imgUrl, Product product) {
        return Image.builder()
                .imgUrl(imgUrl)
                .product(product)
                .build();
    }
}
