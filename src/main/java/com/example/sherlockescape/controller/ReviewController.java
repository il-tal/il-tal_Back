package com.example.sherlockescape.controller;

import com.example.sherlockescape.dto.ResponseDto;
import com.example.sherlockescape.dto.request.ReviewRequestDto;
import com.example.sherlockescape.dto.response.MyReviewResponseDto;
import com.example.sherlockescape.dto.response.ReviewResponseDto;
import com.example.sherlockescape.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/theme")
public class ReviewController {

	private final ReviewService reviewService;

	// 테마 후기 작성
	@PostMapping("/{themeId}/review")
	public ResponseDto<MyReviewResponseDto> createReview(@PathVariable Long themeId,
														 @RequestBody @Valid ReviewRequestDto requestDto,
														 @AuthenticationPrincipal UserDetails userDetails) {
		return reviewService.createReview(themeId, requestDto, userDetails.getUsername());
	}

	// 테마 후기 조회
	@GetMapping("/{themeId}/reviews")
	public ResponseDto<Page<ReviewResponseDto>> getReview (@PathVariable Long themeId,
														   @PageableDefault(size = 6, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
		return ResponseDto.success(reviewService.getReview(themeId, pageable));
	}

	// 테마 후기 수정
	@PutMapping("/review/{reviewId}")
	public ResponseDto<?> updateReview(@AuthenticationPrincipal UserDetails userDetails,
										@PathVariable Long reviewId,
										@RequestBody @Valid ReviewRequestDto requestDto) {
		return reviewService.updateReview(userDetails.getUsername(), reviewId, requestDto);
	}

	// 테마 후기 삭제
	@DeleteMapping("/review/{reviewId}")
	public ResponseDto<String> deleteReview(@PathVariable Long reviewId,
											@AuthenticationPrincipal UserDetails userDetails) {
		return reviewService.deleteReview(reviewId, userDetails.getUsername());
	}

}
