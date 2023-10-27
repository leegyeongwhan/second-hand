package com.secondhand.domain.memberToken;

import com.secondhand.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberTokenRepository extends JpaRepository<MemberToken, Long> {
    Optional<MemberToken> findByMemberId(Long id);

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM MemberToken refreshToken WHERE refreshToken.memberId = :memberId")
    void deleteByMemberId(@Param("memberId") Long memberId);

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM MemberToken refreshToken WHERE refreshToken.memberToken = :token")
    void deleteByMemberToken(@Param("token") String token);
}
