package com.secondhand.domain.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.secondhand.domain.categoriy.Category;
import com.secondhand.domain.image.Image;
import com.secondhand.domain.interested.Interested;
import com.secondhand.domain.member.Member;
import com.secondhand.domain.town.Town;
import com.secondhand.exception.NotUserMineProductException;
import com.secondhand.util.BaseTimeEntity;
import com.secondhand.web.dto.requset.ProductSaveRequest;
import com.secondhand.web.dto.requset.ProductUpdateRequest;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Product extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @Column(length = 45, nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    private Integer price;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 45, nullable = false)
    private Status status;

    @Column(nullable = false)
    @ColumnDefault(value = "0")
    private Integer countView;

    @Column(nullable = false)
    @ColumnDefault(value = "0")
    private Integer countLike;

    @Column(length = 512, nullable = false)
    private String thumbnailUrl;

    @Column(nullable = false)
    private Boolean deleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "town_id")
    @JsonIgnore
    private Town towns;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @JsonIgnore
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Image> images;

    @OneToMany(mappedBy = "product")
    private Set<Interested> interesteds;

    public static Product create(ProductSaveRequest requestInfo, Member member, Category category, Town town, String thumbnailImage) {
        return Product.builder()
                .title(requestInfo.getTitle())
                .content(requestInfo.getContent())
                .price(requestInfo.getPrice())
                .thumbnailUrl(thumbnailImage)
                .countLike(0)
                .countView(0)
                .status(Status.SELLING)
                .deleted(false)
                .category(category)
                .towns(town)
                .member(member)
                //   .images(new ArrayList<>())
                //  .interesteds(new HashSet<>())
                .build();
    }

    public void update(ProductUpdateRequest updateRequest, Category category, Town town) {
        this.title = updateRequest.getTitle();
        this.content = updateRequest.getContent();
        this.price = updateRequest.getPrice();
        //    this.thumbnailUrl = updateRequest.getProductImages().get(0).getName();
        this.category = category;
        this.towns = town;
    }

    public void updateStatus(Integer status) {
        this.status = Status.getStatusByValue(status);
    }

    public String[] changeProductImages() {
        return images.stream()
                .map(Image::getImgUrl)
                .toArray(String[]::new);
    }

    public void updateThumbnail(String url) {
        this.thumbnailUrl = url;
    }

    public Boolean findLiked() {
        for (Interested interested : interesteds) {
            if (interested.getProduct().getId() == this.id) {
                return true;
            }
        }
        return false;
    }

    public void increaseCountView() {
        this.countLike++;
    }

    public void decreaseCountView() {
        this.countLike--;
    }

    public boolean checkIsMine(long userId) {
        return member.getId() == userId;
    }

    public boolean checkIsDetailPageMine(long userId) {
        if (member.getId() == userId) {
            return true;
        }
        throw new NotUserMineProductException();
    }
}
