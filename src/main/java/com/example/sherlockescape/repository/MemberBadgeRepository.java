package com.example.sherlockescape.repository;


import com.example.sherlockescape.domain.Member;
import com.example.sherlockescape.domain.MemberBadge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberBadgeRepository extends JpaRepository<MemberBadge, Long> {
    MemberBadge findByBadgeId(Long badgeId);
}
