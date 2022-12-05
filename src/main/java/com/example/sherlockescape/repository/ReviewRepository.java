package com.example.sherlockescape.repository;


import com.example.sherlockescape.domain.Member;
import com.example.sherlockescape.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositoryCustom {
	List<Review> findAllByThemeId(Long themeId);
    List<Review> findReviewsByMember(Member member);
    Long countByThemeId(Long id);
    List<Review> findReviewsByMemberUsername(String username);

	Long countAllById (Long id);
}

