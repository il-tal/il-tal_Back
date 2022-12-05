package com.example.sherlockescape.repository;

import com.example.sherlockescape.domain.MemberBadge;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.example.sherlockescape.domain.QMemberBadge.memberBadge;

@RequiredArgsConstructor
public class MemberBadgeRepositoryImpl implements MemberBadgeRepositoryCustom{

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Page<MemberBadge> getHofList(Pageable pageable, String username) {
		List<MemberBadge> result = jpaQueryFactory
				.selectFrom(memberBadge)
				.where(memberBadge.member.username.eq(username))
				.limit(pageable.getPageSize())
				.offset(pageable.getOffset())
				.orderBy(memberBadge.member.achieveBadgeCnt.desc())
				.fetch();

		long totalSize = jpaQueryFactory
				.selectFrom(memberBadge)
				.where(eqMemberBadge(username))
				.fetch().size();

		return new PageImpl<>(result, pageable, totalSize);
	}

	private BooleanExpression eqMemberBadge(String username) {
		return username != null ? memberBadge.member.username.eq(username) : null;
	}
}
