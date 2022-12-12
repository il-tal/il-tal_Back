package com.example.sherlockescape.repository;

import com.example.sherlockescape.domain.Review;
import com.example.sherlockescape.dto.response.MyReviewProjectionsDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.example.sherlockescape.domain.QMember.member;
import static com.example.sherlockescape.domain.QReview.review;
import static com.example.sherlockescape.domain.QTheme.theme;

@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepositoryCustom{

	private final JPAQueryFactory jpaQueryFactory;
	@Override
	public Page<Review> getReviewList(Pageable pageable, Long themeId) {
		List<Review> result = jpaQueryFactory
				.selectFrom(review)
				.where(theme.id.eq(themeId))
				.limit(pageable.getPageSize())
				.offset(pageable.getOffset())
				.orderBy(review.id.desc())
				.fetch();

		long totalSize = jpaQueryFactory
				.selectFrom(review)
				.where(eqReview(themeId))
				.fetch().size();

		return new PageImpl<>(result, pageable, totalSize);
	}

	//query projection join theme
	@Override
	public List<MyReviewProjectionsDto> getMyReviewList(String username) {
		return jpaQueryFactory
				.select(Projections.constructor(MyReviewProjectionsDto.class,
						review.theme.id,
						review.theme.themeName,
						review.theme.playTime,
						review.theme.themeImgUrl,
						review.playDate,
						review.score,
						review.success,
						review.difficulty,
						review.comment
						))
				.from(review)
				.join(review.theme, theme)
				.where(review.member.username.eq(username))
				.fetch();
	}

	private BooleanExpression eqReview(Long themeId) {
		return themeId != null ? theme.id.eq(themeId) : null;
	}
}
