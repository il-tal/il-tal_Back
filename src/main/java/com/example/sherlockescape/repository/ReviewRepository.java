package com.example.sherlockescape.repository;


import com.example.sherlockescape.domain.Member;
import com.example.sherlockescape.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
	List<Review> findAllByThemeId(Long themeId);
    List<Review> findReviewsByMember(Member member);
    Long countByThemeId(Long id);
    List<Review> findReviewsByMemberUsername(String username);
}

