package com.example.sherlockescape.controller;

import com.example.sherlockescape.dto.ResponseDto;
import com.example.sherlockescape.dto.request.ReviewRequestDto;
import com.example.sherlockescape.security.user.UserDetailsImpl;
import com.example.sherlockescape.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
	@PutMapping("{themeId}/review/{reviewId}")
	public ResponseDto<?> updateReview(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
									   @PathVariable Long themeId,
									   @PathVariable Long reviewId,
									   @RequestBody ReviewRequestDto requestDto,
									   HttpServletRequest request) {
		return reviewService.updateReview(userDetailsImpl.getMember(), themeId, reviewId, requestDto, request);
	}

	// 테마 후기 삭제
	@DeleteMapping("{themeId}/review/{reviewId}")
	public ResponseDto<String> deleteReview(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
											@PathVariable Long themeId,
											@PathVariable Long reviewId,
											HttpServletRequest request) {
		return reviewService.deleteReview(userDetailsImpl.getMember(), themeId, reviewId, request);
	}

}
