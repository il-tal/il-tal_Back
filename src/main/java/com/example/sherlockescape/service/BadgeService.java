package com.example.sherlockescape.service;



import com.example.sherlockescape.dto.ResponseDto;
import com.example.sherlockescape.dto.request.BadgeCreateRequestDto;
import com.example.sherlockescape.dto.request.BadgeGiveRequestDto;
import com.example.sherlockescape.dto.response.BadgeResponseDto;
import com.example.sherlockescape.dto.response.MyBadgeProjectionsDto;
import com.example.sherlockescape.exception.ErrorCode;
import com.example.sherlockescape.exception.GlobalException;
import com.example.sherlockescape.repository.BadgeRepository;
import com.example.sherlockescape.repository.MemberBadgeRepository;
import com.example.sherlockescape.repository.ReviewRepository;
import com.example.sherlockescape.repository.badge.simplequery.BadgeSimpleQueryDto;
import com.example.sherlockescape.repository.badge.simplequery.BadgeSimpleQueryRepository;
import com.example.sherlockescape.utils.CommonUtils;
import com.example.sherlockescape.utils.ValidateCheck;
import com.example.sherlockescape.domain.Badge;
import com.example.sherlockescape.domain.Member;
import com.example.sherlockescape.domain.MemberBadge;
import com.example.sherlockescape.domain.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) //읽기 전용 쿼리의 성능 최적화
public class BadgeService {

    private final BadgeRepository badgeRepository;
    private final ValidateCheck validateCheck;
    private final MemberBadgeRepository memberBadgeRepository;
    private final ReviewRepository reviewRepository;
    private final CommonUtils commonUtils;
    private final BadgeSimpleQueryRepository badgeSimpleQueryRepository;


    //뱃지 db 저장
    @Transactional
    public ResponseDto<BadgeResponseDto> createBadge(MultipartFile multipartFile, BadgeCreateRequestDto badgeCreateRequestDto) throws IOException {

       /*
       * CommonUtils refactoring
       * */
       String imgUrl = commonUtils.createAll(multipartFile.getOriginalFilename(),
                              multipartFile.getContentType(),
                              multipartFile.getInputStream());

        Badge badge = Badge.builder()
                .badgeName(badgeCreateRequestDto.getBadgeName())
                .badgeExplain(badgeCreateRequestDto.getBadgeExplain())
                .badgeImgUrl(imgUrl)
                .badgeGoal(badgeCreateRequestDto.getBadgeGoal())
                .build();
        badgeRepository.save(badge);
        return ResponseDto.success(BadgeResponseDto.builder()
                .id(badge.getId())
                .badgeImgUrl(badge.getBadgeImgUrl())
                .badgeName(badge.getBadgeName())
                .badgeExplain(badge.getBadgeExplain())
                .badgeGoal(badge.getBadgeGoal())
                .build());
    }

    //뱃지 전체 조회
    //jpql simple query dto 성능 개선
    public List<BadgeSimpleQueryDto> getAllBadge() {

        return badgeSimpleQueryRepository.findBadgeDto();
    }

    //뱃지 부여
    @Transactional
    public ResponseDto<BadgeResponseDto> giveBadge(String username, BadgeGiveRequestDto badgeGiveRequestDto) {
        Member member = validateCheck.getMember(username);
        List<Badge> badgeList = badgeRepository.findAll();
        MemberBadge memberBadge = memberBadgeRepository.
                findByBadgeIdAndMemberUsername(Long.parseLong(badgeGiveRequestDto.getBadgeId()), username);

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
        //성공뱃지
        MemberBadge createBadge = null;
        if(badgeList.get(0).getId() == Long.parseLong(badgeGiveRequestDto.getBadgeId())){

            if(null == memberBadge && totalAchieveCnt >= 1){
                createBadge = new MemberBadge(member, badgeList.get(0));
                memberBadgeRepository.save(createBadge);
            }else if(null != memberBadge){
                throw new GlobalException(ErrorCode.MEMBER_BADGE_ALREADY_EXIST);
            }else{
                throw new GlobalException(ErrorCode.SUCCESS_NOT_ENOUGH);
            }
        }else if(badgeList.get(1).getId() == Long.parseLong(badgeGiveRequestDto.getBadgeId())){

            if(null == memberBadge && totalAchieveCnt >= 3){
                createBadge = new MemberBadge(member, badgeList.get(1));
                memberBadgeRepository.save(createBadge);
            }else if(null != memberBadge){
                throw new GlobalException(ErrorCode.MEMBER_BADGE_ALREADY_EXIST);
            }else{
                throw new GlobalException(ErrorCode.SUCCESS_NOT_ENOUGH);
            }
        }else if(badgeList.get(2).getId() == Long.parseLong(badgeGiveRequestDto.getBadgeId())) {

            if (null == memberBadge && totalAchieveCnt >= 7 ) {
                createBadge = new MemberBadge(member, badgeList.get(2));
                memberBadgeRepository.save(createBadge);
            }else if(null != memberBadge){
                throw new GlobalException(ErrorCode.MEMBER_BADGE_ALREADY_EXIST);
            }else{
                throw new GlobalException(ErrorCode.SUCCESS_NOT_ENOUGH);
            }
        }else if(badgeList.get(3).getId() == Long.parseLong(badgeGiveRequestDto.getBadgeId())) {

            if (null == memberBadge && totalAchieveCnt >= 20) {
                createBadge = new MemberBadge(member, badgeList.get(3));
                memberBadgeRepository.save(createBadge);
            }else if(null != memberBadge){
                throw new GlobalException(ErrorCode.MEMBER_BADGE_ALREADY_EXIST);
            }else{
                throw new GlobalException(ErrorCode.SUCCESS_NOT_ENOUGH);
            }
        }else if(badgeList.get(4).getId() == Long.parseLong(badgeGiveRequestDto.getBadgeId())) {

            if (null == memberBadge && totalAchieveCnt >= 50) {
                createBadge = new MemberBadge(member, badgeList.get(4));
                memberBadgeRepository.save(createBadge);
            }else if(null != memberBadge){
                throw new GlobalException(ErrorCode.MEMBER_BADGE_ALREADY_EXIST);
            }else{
                throw new GlobalException(ErrorCode.SUCCESS_NOT_ENOUGH);
            }
        }

        //실패뱃지
        if(badgeList.get(5).getId() == Long.parseLong(badgeGiveRequestDto.getBadgeId())){
            if(null == memberBadge && totalAchieveCnt >= 1 ){
                createBadge = new MemberBadge(member, badgeList.get(5));
                memberBadgeRepository.save(createBadge);
            }else if(null != memberBadge){
                throw new GlobalException(ErrorCode.MEMBER_BADGE_ALREADY_EXIST);
            }else{
                throw new GlobalException(ErrorCode.FAIL_NOT_ENOUGH);
            }
        }else if(badgeList.get(6).getId() == Long.parseLong(badgeGiveRequestDto.getBadgeId())){
            if(null == memberBadge && totalAchieveCnt >= 7){
                createBadge = new MemberBadge(member, badgeList.get(6));
                memberBadgeRepository.save(createBadge);
            }else if(null != memberBadge){
                throw new GlobalException(ErrorCode.MEMBER_BADGE_ALREADY_EXIST);
            }else{
                throw new GlobalException(ErrorCode.FAIL_NOT_ENOUGH);
            }
        }else if(badgeList.get(7).getId() == Long.parseLong(badgeGiveRequestDto.getBadgeId())) {
            if (null == memberBadge && totalAchieveCnt >= 10) {
                createBadge = new MemberBadge(member, badgeList.get(7));
                memberBadgeRepository.save(createBadge);
            }else if(null != memberBadge){
                throw new GlobalException(ErrorCode.MEMBER_BADGE_ALREADY_EXIST);
            }else{
                throw new GlobalException(ErrorCode.FAIL_NOT_ENOUGH);
            }
        }else if(badgeList.get(8).getId() == Long.parseLong(badgeGiveRequestDto.getBadgeId())) {
            if (null == memberBadge && totalAchieveCnt >= 30) {
                createBadge = new MemberBadge(member, badgeList.get(8));
                memberBadgeRepository.save(createBadge);
            }else if(null != memberBadge){
                throw new GlobalException(ErrorCode.MEMBER_BADGE_ALREADY_EXIST);
            }else{
                throw new GlobalException(ErrorCode.FAIL_NOT_ENOUGH);
            }
        }else if(badgeList.get(9).getId() == Long.parseLong(badgeGiveRequestDto.getBadgeId())) {
            if (null == memberBadge && totalAchieveCnt + totalFailCnt >= 50) {
                createBadge = new MemberBadge(member, badgeList.get(9));
                memberBadgeRepository.save(createBadge);
            }else if(null != memberBadge){
                throw new GlobalException(ErrorCode.MEMBER_BADGE_ALREADY_EXIST);
            }else{
                throw new GlobalException(ErrorCode.FAIL_NOT_ENOUGH);
            }
        }

        int achieveBadgeCnt = memberBadgeRepository.countAllByMemberId(member.getId());
        member.updateMemberBadgeCnt(achieveBadgeCnt);

        assert createBadge != null;
        BadgeResponseDto badgeResponseDto =
                    BadgeResponseDto.builder()
                            .id(createBadge.getBadge().getId())
                            .badgeImgUrl(createBadge.getBadge().getBadgeImgUrl())
                            .badgeName(createBadge.getBadge().getBadgeName())
                            .badgeExplain(createBadge.getBadge().getBadgeExplain())
                            .badgeGoal(createBadge.getBadge().getBadgeGoal())
                            .gotBadge(true)
                            .build();
            return ResponseDto.success(badgeResponseDto);
    }

    //멤버가 가진 칭호 조회
    public List<MyBadgeProjectionsDto> getMemberBadges(String username) {

        return memberBadgeRepository.findAllBadges(username);
    }
}
