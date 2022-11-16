package com.example.sherlockescape.dto.response;

import com.example.sherlockescape.domain.Theme;
import com.example.sherlockescape.repository.ReviewRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ThemeResponseDto {

    private Long id;

    private String themeImgUrl;

    private String themeName;

    private String companyName;

    private String genre;

    private double themeScore;

    private Long themeLikeCnt;

    private Long reviewCnt;


    public ThemeResponseDto(Theme theme/*, ReviewRepository reviewRepository*/) {
        this.id = theme.getId();
        this.themeImgUrl = theme.getThemeImgUrl();
        this.themeName = theme.getThemeName();
        this.companyName = theme.getCompany().getCompanyName();
        this.genre = theme.getGenre();
        this.themeScore = theme.getThemeScore();
        this.themeLikeCnt = theme.getThemeLikeCnt();
//        this.reviewCnt = (long) reviewRepository.findAllByThemeId(id).size();
    }
}
