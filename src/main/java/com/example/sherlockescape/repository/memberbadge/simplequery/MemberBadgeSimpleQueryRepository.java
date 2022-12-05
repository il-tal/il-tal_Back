package com.example.sherlockescape.repository.memberbadge.simplequery;

import com.example.sherlockescape.domain.MemberBadge;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberBadgeSimpleQueryRepository {

    private final EntityManager em;

    public List<MemberBadge> findBadgesWithMemberUsername(String username){
        return em.createQuery(
                "select b from MemberBadge b" +
                        " join b.member m" +
                        " where b.member.username = :username", MemberBadge.class
        ).setParameter("username", username).getResultList();
    }
}
