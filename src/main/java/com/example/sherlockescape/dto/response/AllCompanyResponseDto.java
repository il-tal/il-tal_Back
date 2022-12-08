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
    private String companyName;
    private String companyImgUrl;
    private String location;
    private double companyScore;
    private String companyUrl;
    private Long companyLikeCnt;
    private String address;
    private String phoneNumber;
    private String workHour;
    private boolean companyLikeCheck;
    private int totalReviewCnt;
    private List<Theme> themeList;

}
