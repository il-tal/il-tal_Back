package com.example.sherlockescape.repository;

import com.example.sherlockescape.domain.Member;
import com.example.sherlockescape.domain.ThemeLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ThemeLikeRepository extends JpaRepository<ThemeLike, Long> {

    Optional<ThemeLike> findByThemeIdAndMemberId(long parseLong, Long memberId);

    Long countByThemeId(long parseLong);


    List<ThemeLike> findThemeLikesByMember(Member member);
}

