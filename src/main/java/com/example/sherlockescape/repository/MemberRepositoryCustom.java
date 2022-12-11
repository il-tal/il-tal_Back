package com.example.sherlockescape.repository;

import com.example.sherlockescape.domain.Member;
import com.example.sherlockescape.dto.response.MemberBadgeResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberRepositoryCustom {

	Page<MemberBadgeResponseDto> findAllMember(Pageable pageable);
}
