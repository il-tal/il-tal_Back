package com.example.sherlockescape.service;

import com.example.sherlockescape.domain.Company;
import com.example.sherlockescape.domain.Member;
import com.example.sherlockescape.domain.Review;
import com.example.sherlockescape.domain.Theme;
import com.example.sherlockescape.dto.ResponseDto;
import com.example.sherlockescape.dto.request.ReviewRequestDto;
import com.example.sherlockescape.dto.response.MyReviewProjectionsDto;
import com.example.sherlockescape.dto.response.MyReviewResponseDto;
import com.example.sherlockescape.dto.response.ReviewResponseDto;
import com.example.sherlockescape.exception.ErrorCode;
import com.example.sherlockescape.exception.GlobalException;
import com.example.sherlockescape.repository.CompanyRepository;
import com.example.sherlockescape.repository.ReviewRepository;
import com.example.sherlockescape.repository.ThemeRepository;
import com.example.sherlockescape.utils.ValidateCheck;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) //읽기 전용 쿼리의 성능 최적화
public class ReviewService {

	private final ThemeRepository themeRepository;
	private final CompanyRepository companyRepository;
	private final ReviewRepository reviewRepository;
	private final ValidateCheck validateCheck;


	// 테마 후기 작성
	@Transactional
	public ResponseDto<MyReviewResponseDto> createReview(Long themeId, ReviewRequestDto reviewRequestDto, String username) {

		// 로그인이 필요합니다.
		Member member = validateCheck.getMember(username);

		Theme theme = themeRepository.findById(themeId).orElseThrow(
				() -> new GlobalException(ErrorCode.THEME_NOT_FOUND)
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
		member.updateTotalAchieveCnt(totalAchieveCnt);
		MyReviewResponseDto myReviewResponseDto =
				MyReviewResponseDto.builder()
						.id(review.getId())
						.themeName(review.getTheme().getThemeName())
						.playTime(review.getTheme().getPlayTime())
						.playDate(review.getPlayDate())
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
	public Page<ReviewResponseDto> getReview(Long themeId, Pageable pageable) {

		Theme theme = themeRepository.findById(themeId).orElseThrow(
				() -> new GlobalException(ErrorCode.THEME_NOT_FOUND)
		);
		Long companyId = theme.getCompany().getId();

		Page<Review> reviewList = reviewRepository.getReviewList(pageable, themeId);

		List<ReviewResponseDto> reviewAllList = new ArrayList<>();

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
		//리뷰 점수 테마 평점에 반영하기
		setThemeScore(themeId);

		//리뷰 점수 업체 평점에 반영하기
		setCompanyScore(companyId);

		//리뷰카운트 테마에 저장
		//변경 감지 기능
		theme.updateReviewCnt(reviewRepository.countByThemeId(themeId));

    	return new PageImpl<>(reviewAllList,pageable,reviewList.getTotalElements());
	}

	// 테마 후기 수정
	@Transactional
	public ResponseDto<?> updateReview(String username, Long reviewId, ReviewRequestDto requestDto) {

		Review review = reviewRepository.findById(reviewId).orElseThrow(
				() -> new GlobalException(ErrorCode.REVIEW_NOT_FOUND)
		);

		// 회원님이 작성한 글이 아닙니다.
		if (!username.equals(review.getMember().getUsername())) {
		throw new GlobalException(ErrorCode.AUTHOR_IS_DIFFERENT);
		}

		review.update(requestDto);
		return ResponseDto.success("리뷰 수정 완료!");
	}


	// 테마 후기 삭제
	@Transactional
	public ResponseDto<String> deleteReview (Long reviewId, String username){

		Optional<Review> review = reviewRepository.findById(reviewId);

		// 회원님이 작성한 글이 아닙니다.
		if (!username.equals(review.get().getMember().getUsername())) {
			throw new GlobalException(ErrorCode.AUTHOR_IS_DIFFERENT);
		}

		reviewRepository.deleteById(reviewId);
		return ResponseDto.success("리뷰 삭제 성공!");
	}

	//내가 작성한 후기 조회
	public List<MyReviewProjectionsDto> getMyReviews(String username) {

		//refactoring
		//querydsl projections, theme join 사용
		return reviewRepository.getMyReviewList(username);

	}


	//테마 평점 계산
	private void setThemeScore(Long themeId){
		Theme updateThemeScore = themeRepository.findById(themeId).orElseThrow(
				() -> new GlobalException(ErrorCode.THEME_NOT_FOUND)
		);

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

		//준영속 엔티티 변경 감지 기능 적용
		//해당 테마의 score 로 저장하기
		updateThemeScore.updateThemeScore(themeScore);
	}



	//업체 평점 계산
	private void setCompanyScore(Long companyId) {
		Company updateCompanyScore = companyRepository.findById(companyId).orElseThrow(
				() -> new GlobalException(ErrorCode.COMPANY_NOT_FOUND)
		);

		List<Theme> theme = themeRepository.findAllByCompanyId(companyId);

		//해당 업체의 테마에서 score 컬럼 값들 리스트로 변환
		List<Double> themeScoreList = theme.stream()
				.map(Theme::getThemeScore)
				.collect(Collectors.toList());

		//리스트 평균 구하기(평점 0점인 경우 제외)
		List<Double> zeroScore = new ArrayList<>();
		zeroScore.add(0.0);
		themeScoreList.removeAll(zeroScore);

		double average = themeScoreList.stream()
				.mapToDouble(Double::doubleValue)
				.average().orElse(0);
		double companyScore = Math.round(average*100)/100.0;

		//준영속 엔티티 변경 감지 기능 적용
		//해당 테마의 score 로 저장하기
		updateCompanyScore.updateCompanyScore(companyScore);
	}
}