package com.example.sherlockescape.repository;

import com.example.sherlockescape.domain.MemberBadge;
import org.springframework.data.domain.Page;

import java.awt.print.Pageable;

public interface MemberBadgeRepositoryCustom {

	Page<MemberBadge> findAllByMemberUsername(Pageable pageable, String username);

}
