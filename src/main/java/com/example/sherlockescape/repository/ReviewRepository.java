package com.example.sherlockescape.repository;


import com.example.sherlockescape.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
	List<Review> findAllByThemeId(Long themeId);

}

