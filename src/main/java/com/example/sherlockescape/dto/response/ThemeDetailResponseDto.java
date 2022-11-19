package com.example.sherlockescape.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ThemeDetailResponseDto {

    private Long id;

    private String themeImgUrl;

    private String themeName;

    private String companyName;

    private String genre;

    private double difficulty;

    private int minPeople;

    private int maxPeople;
    private int playTime;

    private int price;

    private String themeUrl;

    private double themeScore;

    private String synopsis;

    private int totalLikeCnt;

    private int reviewCnt;
}
