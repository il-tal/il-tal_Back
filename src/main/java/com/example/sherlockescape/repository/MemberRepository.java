package com.example.sherlockescape.repository;

import com.example.sherlockescape.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

    Optional<Member> findByUsername(String username);

    Optional<Member> findByNickname(String nickname);

    Optional<Member> findByKakaoId(String kakaoId);

    boolean existsByNickname(String nickname);

    boolean existsByUsername(String username);
}
