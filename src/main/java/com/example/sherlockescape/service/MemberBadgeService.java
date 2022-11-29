package com.example.sherlockescape.service;

import com.example.sherlockescape.domain.*;
import com.example.sherlockescape.dto.ResponseDto;
import com.example.sherlockescape.dto.response.MemberBadgeResponseDto;
import com.example.sherlockescape.dto.response.MainAchieveResponseDto;
import com.example.sherlockescape.dto.response.UpdateBadgeResponseDto;
import com.example.sherlockescape.exception.ErrorCode;
import com.example.sherlockescape.exception.GlobalException;
import com.example.sherlockescape.repository.BadgeRepository;
import com.example.sherlockescape.repository.MemberBadgeRepository;
import com.example.sherlockescape.repository.MemberRepository;
import com.example.sherlockescape.repository.ReviewRepository;
import com.example.sherlockescape.utils.ValidateCheck;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.List;

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
    public ResponseDto<UpdateBadgeResponseDto> updateBadge(String username, Long badgeId) {

        Member member = validateCheck.getMember(username);

        Badge badge = badgeRepository.findById(badgeId).orElseThrow(
                () -> new GlobalException(ErrorCode.BADGE_NOT_FOUND)
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
    public ResponseDto<MainAchieveResponseDto> getAchieve(String username) {
        Member member = validateCheck.getMember(username);

        List<Review> reviewList = reviewRepository.findReviewsByMember(member);
        List<MemberBadge> memberBadgeList = memberBadgeRepository.findAllByMemberUsername(username);
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

    //  메인 badge 조회 + 획득한 badge 개수 조회
    @Transactional(readOnly = true)
    public ResponseDto<MemberBadgeResponseDto> getMemberRank(Pageable pageable, String username){

        Member member = validateCheck.getMember(username);

        List<MemberBadge> memberBadgeList = memberBadgeRepository.findAllByMemberUsername(pageable, username);

        List<String> badgeImgUrl = new ArrayList<>();
        for(MemberBadge memberBadge: memberBadgeList){
            String badgeImg = memberBadge.getBadge().getBadgeImgUrl();
            badgeImgUrl.add(badgeImg);
        }

        MemberBadgeResponseDto memberBadgeResponseDto =
                MemberBadgeResponseDto.builder()
                        .nickname(member.getNickname())
                        .mainBadgeImg(member.getMainBadgeImg())
                        .mainBadgeName(member.getMainBadgeName())
                        .achieveBadgeCnt(memberBadgeList.size())
                        .build();
        return ResponseDto.success(memberBadgeResponseDto);
    }

    // main achieve 전체멤버 리스트로 조회 -> 획득한 뱃지 수 별로 나열
    // 로그인 하지 않은 사람도 로그인 한 사람의 리스트를 볼 수 있음
    // 획득한 뱃지 개수글 Cnt 해야함

}
