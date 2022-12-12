package com.example.sherlockescape.repository;

import com.example.sherlockescape.dto.response.MyBadgeProjectionsDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.example.sherlockescape.domain.QBadge.badge;
import static com.example.sherlockescape.domain.QMemberBadge.memberBadge;
import static com.example.sherlockescape.domain.QReview.review;
import static com.example.sherlockescape.domain.QTheme.theme;

@RequiredArgsConstructor
public class MemberBadgeRepositoryImpl implements MemberBadgeRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    //query projections, join badge
    @Override
    public List<MyBadgeProjectionsDto> findAllBadges(String username) {
        return jpaQueryFactory
                .select(Projections.constructor(MyBadgeProjectionsDto.class,
                        memberBadge.badge.id,
                        memberBadge.badge.badgeImgUrl,
                        memberBadge.badge.badgeName,
                        memberBadge.badge.badgeExplain
                ))
                .from(memberBadge)
                .join(memberBadge.badge, badge)
                .where(memberBadge.member.username.eq(username))
                .fetch();
    }
}
