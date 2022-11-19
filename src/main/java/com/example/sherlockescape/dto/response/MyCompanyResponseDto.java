package com.example.sherlockescape.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyCompanyResponseDto {
    private Long id;
    private String companyName;
    private String companyImgUrl;
    private double companyScore;
    private String companyUrl;
    private String location;
    private String address;
    private String phoneNumber;
    private String workHour;
    private int totalReviewCnt;
}
