package com.example.sherlockescape.repository;

import com.example.sherlockescape.domain.QReview;
import com.example.sherlockescape.domain.QTheme;
import com.example.sherlockescape.domain.Theme;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import java.util.Collections;
import java.util.List;

import static com.example.sherlockescape.domain.QTheme.theme;


@RequiredArgsConstructor
@Repository
public class ThemeRepositoryImpl implements ThemeQueryRepository {

    private final JPAQueryFactory queryFactory;


    @Override
    public Page<Theme> findFilter(Pageable pageable, List<String> location, List<String> genreFilter, List<Integer> themeScore, List<Integer> difficulty, List<Integer> people) {


//        QueryResults<Theme> result = queryFactory
//                .selectFrom(theme)
//                .where(
//                        eqLocation(location),
//                        eqGenres(genreFilter),
//                        eqThemeScore(themeScore),
//                        eqDifficulty(difficulty),
//                        eqPeople(people)
//                )
//                .limit(pageable.getPageSize()) // 현재 제한한 갯수
//                .offset(pageable.getOffset())
//                .orderBy(theme.id.desc())
//                .fetchResults();
//        return new PageImpl<>(result.getResults(), pageable, result.getTotal());

        List<Theme> result = queryFactory
                .selectFrom(theme)
                .where(
                        eqLocation(location),
                        eqGenres(genreFilter),
                        eqThemeScore(themeScore),
                        eqDifficulty(difficulty),
                        eqPeople(people)
                )
                .limit(pageable.getPageSize()) // 현재 제한한 갯수
                .offset(pageable.getOffset())
                .orderBy(theme.id.desc())
                .fetch();

        long totalSize = queryFactory
                .selectFrom(theme)
                .where(
                        eqLocation(location),
                        eqGenres(genreFilter),
                        eqThemeScore(themeScore),
                        eqDifficulty(difficulty),
                        eqPeople(people)
                )
                .fetch().size();

        return new PageImpl<>(result, pageable, totalSize);
    }

    private BooleanExpression eqLocation(List<String> location) {
        return location != null ? Expressions.anyOf(location.stream().map(this::isFilteredLoacation).toArray(BooleanExpression[]::new)) : null;
    }

    private BooleanExpression isFilteredLoacation(String location) {
        return theme.company.location.contains(location);
    }


    private BooleanExpression eqGenres(List<String> genreFilter) {
        return genreFilter != null ? Expressions.anyOf(genreFilter.stream().map(this::isFilteredGenre).toArray(BooleanExpression[]::new)) : null;
    }

    private BooleanExpression isFilteredGenre(String genreFilter) {
        return theme.genreFilter.contains(genreFilter);
    }


    private BooleanExpression eqThemeScore(List<Integer> themeScore) {
        if (themeScore != null) {
            Integer minScore = Collections.min(themeScore);
            Integer maxScore = Collections.max(themeScore);
            return theme.themeScore.goe(minScore).and(theme.themeScore.lt(maxScore + 1));
        }  else { return null; }
    }


    private BooleanExpression eqDifficulty(List<Integer> difficulty) {
        if (difficulty != null) {
            Integer minDifficulty = Collections.min(difficulty);
            Integer maxDifficulty = Collections.max(difficulty);
            return theme.difficulty.goe(minDifficulty).and(theme.difficulty.lt(maxDifficulty + 1));
        }  else {  return null; }
    }

    private BooleanExpression eqPeople(List<Integer> people) {
        return people != null ? Expressions.anyOf(people.stream().map(this::isFilteredPeople).toArray(BooleanExpression[]::new)) : null;
    }

    private BooleanExpression isFilteredPeople(Integer people) {
        return theme.minPeople.loe(people).and(theme.maxPeople.goe(people));
    }
}


