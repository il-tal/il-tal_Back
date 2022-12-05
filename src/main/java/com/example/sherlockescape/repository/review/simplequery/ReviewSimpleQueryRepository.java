package com.example.sherlockescape.repository.review.simplequery;


import com.example.sherlockescape.domain.Review;
import com.example.sherlockescape.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReviewSimpleQueryRepository {

    private final EntityManager em;

    public List<Review> findReviewsWithMember(Member member){
        return em.createQuery(
                "select r from Review r" +
                        " join fetch r.member m" +
                        " where r.member.username = :username", Review.class
        ).setParameter("username", member.getUsername()).getResultList();
    }
}
