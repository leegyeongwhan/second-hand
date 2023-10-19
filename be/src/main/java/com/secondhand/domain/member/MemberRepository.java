package com.secondhand.domain.member;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByLoginName(String loginName);

    Optional<Member> findByLoginName(String loginName);
}
