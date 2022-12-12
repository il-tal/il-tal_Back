package com.example.sherlockescape.repository;


import com.example.sherlockescape.domain.MemberBadge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberBadgeRepository extends JpaRepository<MemberBadge, Long>, MemberBadgeRepositoryCustom{
    List<MemberBadge> findAllByMemberUsername(String username);
    MemberBadge findByBadgeIdAndMemberUsername(long parseLong, String username);

    int countAllByMemberId(Long memberId);
}
