package com.example.sherlockescape.service;

import com.example.sherlockescape.domain.Member;
import com.example.sherlockescape.domain.Review;
import com.example.sherlockescape.domain.Theme;
import com.example.sherlockescape.dto.ResponseDto;
import com.example.sherlockescape.dto.request.ReviewRequestDto;
import com.example.sherlockescape.dto.response.ReviewResponseDto;
import com.example.sherlockescape.exception.ErrorCode;
import com.example.sherlockescape.exception.GlobalException;
import com.example.sherlockescape.repository.MemberRepository;
import com.example.sherlockescape.repository.ReviewRepository;
import com.example.sherlockescape.repository.ThemeRepository;
import com.example.sherlockescape.utils.ValidateCheck;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {

	private final ThemeRepository themeRepository;
	private final ReviewRepository reviewRepository;
	private final MemberRepository memberRepository;
	private Review review;
	private final ValidateCheck validateCheck;


	// 테마 후기 작성
	@Transactional
	public ResponseDto<?> createReview(Long themeId, ReviewRequestDto reviewRequestDto, Long memberId) {


		Member member = memberRepository.findById(memberId).orElseThrow(
				() -> new IllegalArgumentException("사용자를 찾을수 없습니다.")
		);
		Theme theme = themeRepository.findById(themeId).orElseThrow(
				() -> new IllegalArgumentException("테마를 찾을수 없습니다.")
		);


		Review review = Review.builder()
				.theme(theme)
				.member(member)
				.nickname(member.getNickname())
				.playDate(reviewRequestDto.getPlayDate())
				.score(reviewRequestDto.getScore())
				.success(reviewRequestDto.isSuccess())
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
							.success(review.isSuccess())
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
	public ResponseDto<?> updateReview(Member member, Long themeId, Long reviewId, ReviewRequestDto reviewRequestDto, HttpServletRequest request) {

		// 회원님이 작성한 글이 아닙니다.
		if(member.getUsername().equals(review.getMember().getUsername())) {
			review.update(member, reviewRequestDto);
			return ResponseDto.success(
					ReviewResponseDto.builder()
							.nickname(member.getNickname())
							.playDate(review.getPlayDate())
							.score(review.getScore())
							.success(review.isSuccess())
							.difficulty(review.getDifficulty())
							.hint(review.getHint())
							.comment(review.getComment())
							.build()
			);
		} else {throw new GlobalException(ErrorCode.AUTHOR_IS_DIFFERENT);}

	}

	// 테마 후기 삭제
	@Transactional
	public ResponseDto<String> deleteReview(Member member, Long themeId, Long reviewId, HttpServletRequest request) {

		// 회원님이 작성한 글이 아닙니다.
		if(!member.getUsername().equals(review.getMember().getUsername())) {
			throw new GlobalException(ErrorCode.AUTHOR_IS_DIFFERENT);
		}
		reviewRepository.deleteById(reviewId);
		return ResponseDto.success("테마 후기 삭제 성공");
		}

}