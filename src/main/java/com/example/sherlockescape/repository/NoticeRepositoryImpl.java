package com.example.sherlockescape.repository;

import com.example.sherlockescape.domain.Notice;
import com.example.sherlockescape.dto.response.NoticeResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.sherlockescape.domain.QNotice.notice;

@RequiredArgsConstructor
public class NoticeRepositoryImpl implements NoticeRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	/*
	*
	*  공지사항 전체 조회
	* */

	@Override
	public Page<NoticeResponseDto> getAllNotices(Pageable pageable) {
		List<NoticeResponseDto> result = jpaQueryFactory.from(notice)
				.select(Projections.constructor(NoticeResponseDto.class,
						notice.id,
						notice.title,
						notice.noticeContent,
						notice.noticeImgUrl,
						notice.createdAt,
						notice.modifiedAt
				))
				.fetch();

		return new PageImpl<>(result, pageable, result.size());
	}

	/*
	*
	*  공지사항 상세 조회
	* */
	@Override
	public NoticeResponseDto getDetailNotice(Long noticeId) {

		return jpaQueryFactory.from(notice)
				.select(Projections.constructor(NoticeResponseDto.class,
							notice.id,
							notice.title,
							notice.noticeContent,
							notice.noticeImgUrl,
							notice.createdAt,
							notice.modifiedAt
						))
				.where(notice.id.eq(noticeId))
				.fetchOne();
	}


}
