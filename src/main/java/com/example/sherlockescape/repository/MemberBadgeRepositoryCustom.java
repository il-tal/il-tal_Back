package com.example.sherlockescape.repository;

import com.example.sherlockescape.domain.MemberBadge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberBadgeRepositoryCustom {

	Page<MemberBadge> getHofList(Pageable pageable, String username);
}
