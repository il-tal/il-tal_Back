package com.example.sherlockescape.service;


import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import com.example.sherlockescape.dto.ResponseDto;
import com.example.sherlockescape.dto.request.BadgeCreateRequestDto;
import com.example.sherlockescape.dto.request.BadgeGiveRequestDto;
import com.example.sherlockescape.dto.response.AllBadgeResponseDto;
import com.example.sherlockescape.dto.response.BadgeResponseDto;
import com.example.sherlockescape.repository.BadgeRepository;
import com.example.sherlockescape.repository.MemberBadgeRepository;
import com.example.sherlockescape.repository.ReviewRepository;
import com.example.sherlockescape.utils.CommonUtils;
import com.example.sherlockescape.utils.ValidateCheck;
import com.example.sherlockescape.domain.Badge;
import com.example.sherlockescape.domain.Member;
import com.example.sherlockescape.domain.MemberBadge;
import com.example.sherlockescape.domain.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BadgeService {

    private final BadgeRepository badgeRepository;
    private final AmazonS3Client amazonS3Client;

    private final ValidateCheck validateCheck;
    private final MemberBadgeRepository memberBadgeRepository;
    private final ReviewRepository reviewRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    //뱃지 db 저장
    public ResponseDto<BadgeResponseDto> createBadge(MultipartFile multipartFile, BadgeCreateRequestDto badgeCreateRequestDto) throws IOException {
        String fileName = CommonUtils.buildFileName(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());

        byte[] bytes = IOUtils.toByteArray(multipartFile.getInputStream());
        objectMetadata.setContentLength(bytes.length);
        ByteArrayInputStream byteArrayIs = new ByteArrayInputStream(bytes);
        amazonS3Client.putObject(new PutObjectRequest(bucketName, fileName, byteArrayIs, objectMetadata)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        String imgurl = amazonS3Client.getUrl(bucketName, fileName).toString();

        Badge badge = Badge.builder()
                .badgeName(badgeCreateRequestDto.getBadgeName())
                .badgeExplain(badgeCreateRequestDto.getBadgeExplain())
                .badgeImgUrl(imgurl)
                .build();
        badgeRepository.save(badge);
        return ResponseDto.success(BadgeResponseDto.builder()
                .id(badge.getId())
                .badgeImgUrl(badge.getBadgeImgUrl())
                .badgeName(badge.getBadgeName())
                .badgeExplain(badge.getBadgeExplain())
                .build());
    }

    //뱃지 전체 조회
    public List<AllBadgeResponseDto> getAllBadge() {
        List<Badge> badgeList = badgeRepository.findAll();
        List<AllBadgeResponseDto> badgeResponseDtoList = new ArrayList<>();
        for(Badge badge: badgeList){
            AllBadgeResponseDto allBadgeResponseDto = AllBadgeResponseDto.builder()
                    .id(badge.getId())
                    .badgeImgUrl(badge.getBadgeImgUrl())
                    .badgeName(badge.getBadgeName())
                    .badgeExplain(badge.getBadgeExplain())
                    .build();
            badgeResponseDtoList.add(allBadgeResponseDto);
        }
        return badgeResponseDtoList;
    }
    //뱃지 부여
    public ResponseDto<BadgeResponseDto> giveBadge(Long memberId, BadgeGiveRequestDto badgeGiveRequestDto) {
        Member member = validateCheck.getMember(memberId);
        List<Badge> badgeList = badgeRepository.findAll();
        MemberBadge memberBadge = memberBadgeRepository.findByBadgeId(Long.parseLong(badgeGiveRequestDto.getBadgeId()));
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
        MemberBadge createBadge = null;
        if(badgeList.get(0).getId() == Long.parseLong(badgeGiveRequestDto.getBadgeId())){
            if(null == memberBadge && totalAchieveCnt >= 10 && totalAchieveCnt < 20){
                createBadge = new MemberBadge(member, badgeList.get(0));
                memberBadgeRepository.save(createBadge);
            }else if(null != memberBadge){
                throw new IllegalArgumentException("해당 뱃지를 이미 획득하셨습니다.");
            }else{
                throw new IllegalArgumentException("성공 횟수가 부족합니다.");
            }
        }else if(badgeList.get(1).getId() == Long.parseLong(badgeGiveRequestDto.getBadgeId())){
            if(totalAchieveCnt >= 20 && totalAchieveCnt <30){
                createBadge = new MemberBadge(member, badgeList.get(1));
                memberBadgeRepository.save(createBadge);
            }else if(null != memberBadge){
                throw new IllegalArgumentException("해당 뱃지를 이미 획득하셨습니다.");
            }else{
                throw new IllegalArgumentException("성공 횟수가 부족합니다.");
            }
        }else if(badgeList.get(2).getId() == Long.parseLong(badgeGiveRequestDto.getBadgeId())) {
            if (totalAchieveCnt >= 30 && totalAchieveCnt < 40) {
                createBadge = new MemberBadge(member, badgeList.get(2));
                memberBadgeRepository.save(createBadge);
            }else{
                throw new IllegalArgumentException("해당 뱃지를 이미 획득하셨습니다.");
            }
        }
        assert createBadge != null;
        BadgeResponseDto badgeResponseDto =
                    BadgeResponseDto.builder()
                            .id(createBadge.getBadge().getId())
                            .badgeImgUrl(createBadge.getBadge().getBadgeImgUrl())
                            .badgeName(createBadge.getBadge().getBadgeName())
                            .badgeExplain(createBadge.getBadge().getBadgeExplain())
                            .build();
            return ResponseDto.success(badgeResponseDto);
    }
}
