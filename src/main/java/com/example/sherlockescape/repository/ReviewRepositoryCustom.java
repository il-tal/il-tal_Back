package com.example.sherlockescape.repository;

import com.example.sherlockescape.domain.Review;
import com.example.sherlockescape.dto.response.MyReviewProjectionsDto;
import com.example.sherlockescape.dto.response.MyReviewResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReviewRepositoryCustom {

	Page<Review> getReviewList(Pageable pageable, Long themeId);

	List<MyReviewProjectionsDto> getMyReviewList(String username);

}
