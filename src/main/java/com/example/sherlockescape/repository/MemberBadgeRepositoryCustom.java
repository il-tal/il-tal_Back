package com.example.sherlockescape.repository;

import com.example.sherlockescape.dto.response.MyBadgeProjectionsDto;

import java.util.List;

public interface MemberBadgeRepositoryCustom {
    List<MyBadgeProjectionsDto> findAllBadges(String username);
}
