package com.example.sherlockescape.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyResponseDto {

    private Long id;

    private String companyImgUrl;

    private String location;

    private double companyScore;

    private String companyUrl;

    private Long companyLikeCnt;

    private boolean companyLikeCheck;

    private String address;

    private String phoneNumber;

    private String workHour;

//    private int ReviewCnt;

}
