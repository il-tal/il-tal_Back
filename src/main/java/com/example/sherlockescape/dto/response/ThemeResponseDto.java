package com.example.sherlockescape.dto.response;

import com.example.sherlockescape.domain.Theme;
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

    private Long reviewCnt;


    public ThemeResponseDto(Theme theme) {
        this.id = theme.getId();
        this.themeImgUrl = theme.getThemeImgUrl();
        this.themeName = theme.getThemeName();
        this.companyName = theme.getCompany().getCompanyName();
        this.genre = theme.getGenre();
        this.themeScore = theme.getThemeScore();
        this.totalLikeCnt = theme.getTotalLikeCnt();
//        this.reviewCnt = (long) reviewRepository.findAllByThemeId(id).size();
    }
}
