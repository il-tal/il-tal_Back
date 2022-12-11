package com.example.sherlockescape.repository;

import com.example.sherlockescape.domain.Member;
import com.example.sherlockescape.dto.response.MemberBadgeResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static com.example.sherlockescape.domain.QMember.member;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom{

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Page<MemberBadgeResponseDto> findAllMember(Pageable pageable) {


		List<MemberBadgeResponseDto> result = jpaQueryFactory.from(member)
				.select(
						Projections.constructor(MemberBadgeResponseDto.class,
								member.id,
								member.nickname,
								member.mainBadgeImg,
								member.mainBadgeName,
								member.achieveBadgeCnt,
								member.totalAchieveCnt
								))
				.where(member.achieveBadgeCnt.gt(0))
				.limit(pageable.getPageSize())
				.offset(pageable.getOffset())
				.orderBy(member.achieveBadgeCnt.desc(), member.totalAchieveCnt.desc())
				.fetch();

		return new PageImpl<>(result, pageable, result.size());
	}
}
