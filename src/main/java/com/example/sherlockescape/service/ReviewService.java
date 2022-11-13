package com.example.sherlockescape.service;

import com.example.sherlockescape.domain.Member;
import com.example.sherlockescape.domain.Review;
import com.example.sherlockescape.domain.Theme;
import com.example.sherlockescape.dto.ResponseDto;
import com.example.sherlockescape.dto.request.ReviewRequestDto;
import com.example.sherlockescape.dto.response.ReviewResponseDto;
import com.example.sherlockescape.exception.ErrorCode;
import com.example.sherlockescape.exception.GlobalException;
import com.example.sherlockescape.repository.ReviewRepository;
import com.example.sherlockescape.repository.ThemeRepository;
import com.example.sherlockescape.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {

	private ThemeRepository themeRepository;
	private ReviewRepository reviewRepository;
	private JwtUtil jwtUtil;
	private Review review;


	// 테마 후기 작성
	@Transactional
	public ResponseDto<?> createReview(ReviewRequestDto reviewRequestDto, HttpServletRequest request) {

		//  로그인이 필요합니다.
		if (!jwtUtil.tokenValidation(request.getHeader("Refresh-Token"))) {
			throw new GlobalException(ErrorCode.NEED_TO_LOGIN);
		}

		Review review = Review.builder()
				.nickname(reviewRequestDto.getNickname())
				.playDate(reviewRequestDto.getPlayDate())
				.score(reviewRequestDto.getScore())
				.challenge(reviewRequestDto.getChallenge())
				.difficulty(reviewRequestDto.getDifficulty())
				.hint(reviewRequestDto.getHint())
				.comment(reviewRequestDto.getComment())
				.build();
		reviewRepository.save(review);
		return ResponseDto.success("리뷰 등록 성공");
	}

	// 해당 테마 후기 조회
	@Transactional
	public ResponseDto<?> getReview(Long themeId) {

		themeRepository.findById(themeId);
		List<ReviewResponseDto> reviewAllList = new ArrayList<>();
		List<Review> reviewList = reviewRepository.findAllByThemeId(themeId);
		for(Review review: reviewList){
			reviewAllList.add(
					ReviewResponseDto.builder()
							.nickname(review.getMember().getNickname())
							.playDate(review.getPlayDate())
							.score(review.getScore())
							.challenge(review.getChallenge())
							.difficulty(review.getDifficulty())
							.hint(review.getHint())
							.comment(review.getComment())
							.build()
			);
		}

		return ResponseDto.success(reviewAllList);
	}

	// 테마 후기 수정
	@Transactional
	public ResponseDto<?> updateReview(Member member, Long id, ReviewRequestDto reviewRequestDto, HttpServletRequest request) {

		//  로그인이 필요합니다.
		if (!jwtUtil.tokenValidation(request.getHeader("Refresh-Token"))) {
			throw new GlobalException(ErrorCode.NEED_TO_LOGIN);
		}

		// 회원님이 작성한 글이 아닙니다.
		if(member.getUsername().equals(review.getMember().getUsername())) {
			review.update(member, reviewRequestDto);
			return ResponseDto.success(
					ReviewResponseDto.builder()
							.nickname(member.getNickname())
							.playDate(review.getPlayDate())
							.score(review.getScore())
							.challenge(review.getChallenge())
							.difficulty(review.getDifficulty())
							.hint(review.getHint())
							.comment(review.getComment())
							.build()
			);
		} else {throw new GlobalException(ErrorCode.AUTHOR_IS_DIFFERENT);}

	}

	// 테마 후기 삭제
	@Transactional
	public ResponseDto<String> deleteReview(Member member, Long id, HttpServletRequest request) {

		//  로그인이 필요합니다.
		if (!jwtUtil.tokenValidation(request.getHeader("Refresh-Token"))) {
			throw new GlobalException(ErrorCode.NEED_TO_LOGIN);
		}

		// 회원님이 작성한 글이 아닙니다.
		if(!member.getUsername().equals(review.getMember().getUsername())) {
			throw new GlobalException(ErrorCode.AUTHOR_IS_DIFFERENT);
		}
		// 삭제
		reviewRepository.deleteById(id);
		return ResponseDto.success("테마 후기 삭제 성공");
		}



	/////////////////////////////////////////////////////

	// member 유효성 검사
//	private Member validateMember(HttpServletRequest request) {
//		if (!jwtUtil.tokenValidation(request.getHeader("Refresh-Token"))) {
//			return null;
//		}
//		return jwtUtil.getMemberFromAuthentication();
//	}

	// theme 유효성 검사
	public Theme isPresentTheme(Long id){
		Optional<Theme> optionalTheme = themeRepository.findById(id);
		return optionalTheme.orElse(null);
	}

	// review 유효성 검사
	@Transactional(readOnly = true)
	public Review isPresentReview(Long id){
		Optional<Review> optionalReview = reviewRepository.findById(id);
		return optionalReview.orElse(null);
	}

}