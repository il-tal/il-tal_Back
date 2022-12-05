package com.example.sherlockescape.repository;

import com.example.sherlockescape.domain.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.example.sherlockescape.domain.QMember.member;

@RequiredArgsConstructor
public class MemberBadgeRepositoryImpl implements MemberBadgeRepositoryCustom{

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Page<Member> getHofList(Pageable pageable) {
		List<Member> result = jpaQueryFactory
				.selectFrom(member)
				.limit(pageable.getPageSize())
				.offset(pageable.getOffset())
				.orderBy(member.achieveBadgeCnt.desc())
				.fetch();

		long totalSize = jpaQueryFactory
				.selectFrom(member)
				.fetch().size();

		return new PageImpl<>(result, pageable, totalSize);
	}
}
