package com.example.sherlockescape.repository;

import com.example.sherlockescape.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewRepositoryCustom {

	Page<Review> getReviewList(Pageable pageable, Long themeId);

}
