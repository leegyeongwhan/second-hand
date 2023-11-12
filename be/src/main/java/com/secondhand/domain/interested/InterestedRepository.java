package com.secondhand.domain.interested;

import com.secondhand.domain.member.Member;
import com.secondhand.domain.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface InterestedRepository extends JpaRepository<Interested, Long> {

    @Query("SELECT i FROM Interested i WHERE i.product.id = :productId AND i.member.id = :memberId")
    Optional<Interested> findByMemberIdAndProductId(Long memberId, Long productId);

    @Modifying
    @Query("DELETE FROM Interested interested WHERE interested.product.id = :productId AND interested.member.id = :memberId")
    void deleteByProductIdAndMemberId(@Param("productId") Long productId,
            @Param("memberId") Long memberId);

    void deleteByProductId(Long productId);

    Boolean existsByProductIdAndMemberId(Long productId, Long memberId);
}
