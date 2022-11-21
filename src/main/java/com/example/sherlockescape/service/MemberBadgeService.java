package com.example.sherlockescape.service;

import com.example.sherlockescape.domain.Badge;
import com.example.sherlockescape.domain.Member;
import com.example.sherlockescape.domain.MemberBadge;
import com.example.sherlockescape.domain.Review;
import com.example.sherlockescape.dto.ResponseDto;
import com.example.sherlockescape.dto.response.MainAchieveResponseDto;
import com.example.sherlockescape.dto.response.UpdateBadgeResponseDto;
import com.example.sherlockescape.repository.BadgeRepository;
import com.example.sherlockescape.repository.MemberBadgeRepository;
import com.example.sherlockescape.repository.MemberRepository;
import com.example.sherlockescape.repository.ReviewRepository;
import com.example.sherlockescape.utils.ValidateCheck;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberBadgeService {

    private final ValidateCheck validateCheck;
    private final BadgeRepository badgeRepository;
    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;
    private final MemberBadgeRepository memberBadgeRepository;


    //대표 뱃지 수정
    @Transactional
    public ResponseDto<UpdateBadgeResponseDto> updateBadge(Long memberId, Long badgeId) {

        Member member = validateCheck.getMember(memberId);

        Badge badge = badgeRepository.findById(badgeId).orElseThrow(
                () -> new IllegalArgumentException("뱃지가 존재하지 않습니다.")
        );

        member.updateBadge(badge.getBadgeImgUrl(), badge.getBadgeName());
        memberRepository.save(member);

        UpdateBadgeResponseDto updateBadgeResponseDto = UpdateBadgeResponseDto.builder()
                                .mainBadgeImg(member.getMainBadgeImg())
                                .mainBadgeName(member.getMainBadgeName())
                                .build();
        return ResponseDto.success(updateBadgeResponseDto);
    }

    //main achieve 전체 조회
    public ResponseDto<MainAchieveResponseDto> getAchieve(Long memberId) {
        Member member = validateCheck.getMember(memberId);

        List<Review> reviewList = reviewRepository.findReviewsByMember(member);
        List<MemberBadge> memberBadgeList = memberBadgeRepository.findAllByMemberId(memberId);
        int totalAchieveCnt = 0;
        int totalFailCnt = 0;
        for(Review review: reviewList){
            if(review.isSuccess()){
                totalAchieveCnt += 1;
            }else{
                totalFailCnt +=1;
            }
        }
        List<String> badgeImgUrl = new ArrayList<>();
        for(MemberBadge memberBadge: memberBadgeList){
            String badgeImg = memberBadge.getBadge().getBadgeImgUrl();
            badgeImgUrl.add(badgeImg);
        }
        MainAchieveResponseDto mainAchieveResponseDto =
                MainAchieveResponseDto.builder()
                        .nickname(member.getNickname())
                        .mainBadgeImg(member.getMainBadgeImg())
                        .mainBadgeName(member.getMainBadgeName())
                        .totalAchieveCnt(totalAchieveCnt)
                        .totalFailCnt(totalFailCnt)
                        .badgeImgUrl(badgeImgUrl)
                        .build();
        return ResponseDto.success(mainAchieveResponseDto);
    }
}
