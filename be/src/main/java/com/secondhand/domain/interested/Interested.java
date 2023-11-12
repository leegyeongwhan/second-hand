package com.secondhand.domain.interested;

import com.secondhand.domain.member.Member;
import com.secondhand.domain.product.Product;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Interested {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "interested_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private boolean isLiked;


    @Builder
    private Interested(Long id, Member member, Product product) {
        this.id = id;
        this.member = member;
        this.product = product;
    }

    public static Interested of(Long productId, Long memberId) {
        return Interested.builder()
                .product(Product.builder()
                        .id(productId)
                        .build())
                .member(Member.builder()
                        .id(memberId)
                        .build())
                .build();
    }

    public void changeInterested(Interested newInterested, Member member, Product product) {
        newInterested.setMember(member);
        newInterested.setProduct(product);
        member.getInteresteds().add(newInterested);
        product.getInteresteds().add(newInterested);
        newInterested.setLiked(true);
    }

    public void deleteInterested(Interested existInterested, Member member, Product product) {
        member.getInteresteds().remove(existInterested);
        product.getInteresteds().remove(existInterested);
    }
}
