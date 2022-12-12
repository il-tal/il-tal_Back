package com.example.sherlockescape.repository;

import com.example.sherlockescape.domain.Notice;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.example.sherlockescape.domain.QNotice.notice1;

@RequiredArgsConstructor
public class NoticeRepositoryImpl implements NoticeRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Page<Notice> getNoticeList(Pageable pageable) {
		List<Notice> result = jpaQueryFactory
				.selectFrom(notice1)
				.limit(pageable.getPageSize())
				.offset(pageable.getOffset())
				.orderBy(notice1.id.desc())
				.fetch();

		long totalSize = jpaQueryFactory
				.selectFrom(notice1)
				.fetch().size();

		return new PageImpl<>(result, pageable, totalSize);
	}
}
