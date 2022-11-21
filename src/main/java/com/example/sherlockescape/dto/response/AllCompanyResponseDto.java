package com.example.sherlockescape.dto.response;


import com.example.sherlockescape.domain.Company;
import com.example.sherlockescape.domain.Theme;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AllCompanyResponseDto {

    private Long id;
    private String companyImgUrl;
    private String location;
    private double companyScore;
    private String companyUrl;

    private Long companyLikeCnt;
    private String address;
    private String phoneNumber;
    private String workHour;

    private int totalReviewCnt;
    private List<Theme> themeList;

    public AllCompanyResponseDto(Company company){
        this.id = company.getId();
        this.companyImgUrl =company.getCompanyImgUrl();
        this.companyScore = company.getCompanyScore();
        this.location = company.getLocation();
        this.companyUrl = company.getCompanyUrl();
        this.address = company.getAddress();
        this.phoneNumber = company.getPhoneNumber();
        this.workHour = company.getWorkHour();
        this.themeList = company.getThemeList();
    }
}
