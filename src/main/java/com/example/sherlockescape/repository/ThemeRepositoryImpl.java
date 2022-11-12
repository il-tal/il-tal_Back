package com.example.sherlockescape.repository;

import com.example.sherlockescape.domain.QTheme;
import com.example.sherlockescape.domain.Theme;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.sherlockescape.domain.QTheme.theme;


@RequiredArgsConstructor
@Repository
public class ThemeRepositoryImpl implements ThemeQueryRepository{

    private final JPAQueryFactory queryFactory;


    @Override
    public Page<Theme> findFilter(Pageable pageable, List<String> location, List<String> genre) {

        QTheme theme = QTheme.theme;

        QueryResults<Theme> result = queryFactory
                .from(theme)
                .select(theme)
                .where(theme.location.in(location))
                .where(eqGenres(genre))
                .limit(pageable.getPageSize()) // 현재 제한한 갯수
                .offset(pageable.getOffset())
                .orderBy(theme.id.desc())
                .fetchResults();
        return new PageImpl<>(result.getResults(),pageable,result.getTotal());

    }

    private BooleanExpression eqGenres(List<String> genre) {
        return genre != null ? Expressions.allOf(genre.stream().map(this::isFilteredGenre).toArray(BooleanExpression[]::new)) : null;
    }

    private BooleanExpression isFilteredGenre(String genre) {
        return theme.genre.contains(genre);
    }
}
