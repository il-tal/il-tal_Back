package com.example.sherlockescape.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MyThemeResponseDto {
    private String themeImgUrl;
    private String themeName;
    private String companyName;
    private double themeScore;
    private Long themeLikeCnt;
    private Long reviewCnt;
}
