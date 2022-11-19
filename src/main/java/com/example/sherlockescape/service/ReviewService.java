package com.example.sherlockescape.service;

import com.example.sherlockescape.domain.Member;
import com.example.sherlockescape.domain.Review;
import com.example.sherlockescape.domain.Theme;
import com.example.sherlockescape.dto.ResponseDto;
import com.example.sherlockescape.dto.request.ReviewRequestDto;
import com.example.sherlockescape.dto.response.MyReviewResponseDto;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {

	private final ThemeRepository themeRepository;
	private final ReviewRepository reviewRepository;
	private final MemberRepository memberRepository;
	private final ValidateCheck validateCheck;


	// 테마 후기 작성
	@Transactional
	public ResponseDto<?> createReview(Long themeId, ReviewRequestDto reviewRequestDto, Long memberId) {

		// 로그인이 필요합니다.
		Member member = memberRepository.findById(memberId).orElseThrow(
				() -> new GlobalException(ErrorCode.NEED_TO_LOGIN)
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


		return ResponseDto.success("리뷰 등록 성공!");
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
							.id(review.getId())
							.nickname(review.getMember().getNickname())
							.playDate(review.getPlayDate())
							.score(review.getScore())
							.success(review.isSuccess())
							.difficulty(review.getDifficulty())
							.hint(review.getHint())
							.comment(review.getComment())
							.build()
			);
//			List<Double> scoreList = reviewList.stream()
//					.map(Review::getScore)
//					.collect(Collectors.toList());
//
//			scoreList.stream()
//					.mapToInt(a -> a)
//					.average().orElse(0);
		}

		return ResponseDto.success(reviewAllList);
	}

	// 테마 후기 수정
	@Transactional
	public ResponseDto<?> updateReview(Member member, Long reviewId, ReviewRequestDto requestDto) {

		Review review = reviewRepository.findById(reviewId).orElseThrow(
				() -> new IllegalArgumentException("리뷰가 존재하지 않습니다"));

		// 회원님이 작성한 글이 아닙니다.
		if (!member.getUsername().equals(review.getMember().getUsername())) {
		throw new GlobalException(ErrorCode.AUTHOR_IS_DIFFERENT);
		}

		review.update(requestDto);
		return ResponseDto.success("리뷰 수정 완료!");
	}



	// 테마 후기 삭제
	@Transactional
	public ResponseDto<String> deleteReview (Long reviewId, Member member){

		Optional<Review> review = reviewRepository.findById(reviewId);

		// 회원님이 작성한 글이 아닙니다.
		if (!member.getUsername().equals(review.get().getMember().getUsername())) {
			throw new GlobalException(ErrorCode.AUTHOR_IS_DIFFERENT);
		}

		reviewRepository.deleteById(reviewId);
		return ResponseDto.success("리뷰 삭제 성공!");
	}

	//내가 작성한 후기 조회
	public List<MyReviewResponseDto> getMyReviews(Member member) {

		List<Review> reviewList = reviewRepository.findReviewsByMember(member);

		List<MyReviewResponseDto> reviewResponseDtoList = new ArrayList<>();
		for(Review review: reviewList){

			MyReviewResponseDto myReviewResponseDtoList = MyReviewResponseDto.builder()
					.id(review.getId())
					.themeName(review.getTheme().getThemeName())
					.playTime(review.getPlayDate())
					.score(review.getScore())
					.difficulty(review.getDifficulty())
					.comment(review.getComment())
					.success(review.isSuccess())
					.build();
			reviewResponseDtoList.add(myReviewResponseDtoList);
		}
		return reviewResponseDtoList;
	}
}