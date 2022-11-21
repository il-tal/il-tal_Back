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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


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
	public ResponseDto<MyReviewResponseDto> createReview(Long themeId, ReviewRequestDto reviewRequestDto, Long memberId) {

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

		//리뷰 점수 테마 평점에 반영하기
		setThemeScore(themeId);

		//totalAchieveCnt, totalFailCnt 보내주기
		List<Review> reviews = reviewRepository.findReviewsByMember(member);
		int totalAchieveCnt = 0;
		int totalFailCnt = 0;
		for(Review reviewCnt: reviews){
			if(reviewCnt.isSuccess()){
				totalAchieveCnt += 1;
			}else{
				totalFailCnt += 1;
			}
		}
		MyReviewResponseDto myReviewResponseDto =
				MyReviewResponseDto.builder()
						.id(review.getId())
						.themeName(review.getTheme().getThemeName())
						.playTime(review.getTheme().getPlayTime())
						.score(review.getScore())
						.comment(review.getComment())
						.success(review.isSuccess())
						.difficulty(review.getDifficulty())
						.totalAchieveCnt(totalAchieveCnt)
						.totalFailCnt(totalFailCnt)
						.build();
		return ResponseDto.success(myReviewResponseDto);
	}

	// 해당 테마 후기 조회
	@Transactional
	public ResponseDto<?> getReview(Long themeId) {
		themeRepository.findById(themeId);
		List<ReviewResponseDto> reviewAllList = new ArrayList<>();
		List<Review> reviewList = reviewRepository.findAllByThemeId(themeId);
		for(Review review: reviewList) {
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
					.playTime(review.getTheme().getPlayTime())
					.score(review.getScore())
					.success(review.isSuccess())
					.difficulty(review.getDifficulty())
					.comment(review.getComment())
					.build();
			reviewResponseDtoList.add(myReviewResponseDtoList);
		}
		return reviewResponseDtoList;
	}

	//테마 평점 계산
	private void setThemeScore(Long themeId){
		Theme updateThemeScore = themeRepository.findById(themeId).orElseThrow(
				() -> new IllegalArgumentException("테마를 찾을수 없습니다."));

		List<Review> reviewList = reviewRepository.findAllByThemeId(themeId);

		//리뷰에서 score 컬럼 값들 리스트로 변환
		List<Double> scoreList = reviewList.stream()
				.map(Review::getScore)
				.collect(Collectors.toList());

		//리스트 평균 구하기
		double average = scoreList.stream()
				.mapToDouble(Double::doubleValue)
				.average().orElse(0);
		double themeScore = Math.round(average*100)/100.0;

		//해당 테마의 score로 저장하기
		updateThemeScore.updateThemeScore(themeScore);
		themeRepository.save(updateThemeScore);

	}

}