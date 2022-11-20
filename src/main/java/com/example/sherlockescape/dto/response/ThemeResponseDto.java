package com.example.sherlockescape.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ThemeResponseDto {

    private Long id;

    private String themeImgUrl;

    private String themeName;

    private String companyName;

    private String genre;

    private double themeScore;

    private int totalLikeCnt;

    private int reviewCnt;
}
