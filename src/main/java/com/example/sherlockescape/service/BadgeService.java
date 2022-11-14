package com.example.sherlockescape.service;


import com.amazonaws.services.s3.AmazonS3Client;
import com.example.sherlockescape.domain.Badge;
import com.example.sherlockescape.dto.ResponseDto;
import com.example.sherlockescape.dto.request.BadgeRequestDto;
import com.example.sherlockescape.dto.response.BadgeResponseDto;
import com.example.sherlockescape.repository.BadgeRepository;
import com.example.sherlockescape.utils.CommonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BadgeService {

    private final BadgeRepository badgeRepository;
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public ResponseDto<BadgeResponseDto> createBadge(MultipartFile multipartFile, BadgeRequestDto badgeRequestDto) {
        String fileName = CommonUtils.buildFileName(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        String imgurl = amazonS3Client.getUrl(bucketName, fileName).toString();

        Badge badge = Badge.builder()
                .badgeName(badgeRequestDto.getBadgeName())
                .badgeExplain(badgeRequestDto.getBadgeExplain())
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
}
