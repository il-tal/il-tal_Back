package com.example.sherlockescape.repository.badge.simplequery;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BadgeSimpleQueryRepository {

    private final EntityManager em;

    public List<BadgeSimpleQueryDto> findBadgeDto(){
        return em.createQuery(
                "select new com.example.sherlockescape.repository.badge.simplequery.BadgeSimpleQueryDto(b.id, b.badgeImgUrl, b.badgeName, b.badgeExplain, b.badgeGoal)" +
                        " from Badge b", BadgeSimpleQueryDto.class)
                .getResultList();
    }
}
