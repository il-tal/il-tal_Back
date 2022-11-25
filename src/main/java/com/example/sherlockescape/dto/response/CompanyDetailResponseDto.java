package com.example.sherlockescape.dto.response;

import com.example.sherlockescape.domain.Theme;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDetailResponseDto {
    private Long id;

    private String companyName;
    private String companyImgUrl;

    private String location;

    private double companyScore;

    private String companyUrl;

    private int companyLikeCnt;

    private String address;

    private String phoneNumber;

    private String workHour;

    private int totalReviewCnt;

    private boolean companyLikeCheck;

    private List<Theme> themeList;
}