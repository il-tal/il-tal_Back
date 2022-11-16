package com.example.sherlockescape.controller;

import com.example.sherlockescape.dto.ResponseDto;
import com.example.sherlockescape.dto.request.ReviewRequestDto;
import com.example.sherlockescape.security.user.UserDetailsImpl;
import com.example.sherlockescape.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/theme")
public class ReviewController {

	private final ReviewService reviewService;

	// 테마 후기 작성
	@PostMapping("/{themeId}/review")
	public ResponseDto<?> createReview(@PathVariable Long themeId,
										@RequestBody @Valid ReviewRequestDto requestDto,
										@AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
		return reviewService.createReview(themeId, requestDto, userDetailsImpl.getMember().getId());
	}

	// 테마 후기 조회
	@GetMapping("/{themeId}/reviews")
	public ResponseDto<?> getReview(@PathVariable Long themeId) {
		return reviewService.getReview(themeId);
	}

	// 테마 후기 수정
	@PutMapping("/review/{reviewId}")
	public ResponseDto<?> updateReview(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
										@PathVariable Long reviewId,
										@RequestBody @Valid ReviewRequestDto requestDto) {
		return reviewService.updateReview(userDetailsImpl.getMember(), reviewId, requestDto);
	}

	// 테마 후기 삭제
	@DeleteMapping("/review/{reviewId}")
	public ResponseDto<String> deleteReview(@PathVariable Long reviewId,
											@AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
		return reviewService.deleteReview(reviewId, userDetailsImpl.getMember());
	}

}
