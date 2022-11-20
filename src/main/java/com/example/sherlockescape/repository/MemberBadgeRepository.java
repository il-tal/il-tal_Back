package com.example.sherlockescape.repository;


import com.example.sherlockescape.domain.MemberBadge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberBadgeRepository extends JpaRepository<MemberBadge, Long> {
    MemberBadge findByBadgeId(Long badgeId);
}
