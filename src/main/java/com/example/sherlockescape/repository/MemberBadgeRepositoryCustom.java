package com.example.sherlockescape.repository;

import com.example.sherlockescape.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberBadgeRepositoryCustom {

	Page<Member> getHofList(Pageable pageable);
}
