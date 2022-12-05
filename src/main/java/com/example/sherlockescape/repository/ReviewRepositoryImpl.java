package com.example.sherlockescape.repository;

import com.example.sherlockescape.domain.Review;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

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

	private BooleanExpression eqReview(Long themeId) {
		return themeId != null ? theme.id.eq(themeId) : null;
	}
}
