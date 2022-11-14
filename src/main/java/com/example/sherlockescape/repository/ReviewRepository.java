package com.example.sherlockescape.repository;


import com.example.sherlockescape.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
	List<Review> findAllByThemeId(Long themeId);


    Long countByMemberId(Long memberId);
}

