package com.example.sherlockescape.dto.response;

import com.example.sherlockescape.domain.Theme;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Lob;

@Getter
@AllArgsConstructor
@Builder
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

//    themeLike
//
//    reviewCnt
//

    private String synopsis;

    public ThemeDetailResponseDto(Theme theme) {
        this.id = theme.getId();
        this.themeImgUrl = theme.getThemeImgUrl();
        this.themeName = theme.getThemeName();
        this.companyName = theme.getCompany().getCompanyName();
        this.genre = theme.getGenre();
        this.difficulty = theme.getDifficulty();
        this.minPeople = theme.getMinPeople();
        this.maxPeople = theme.getMaxPeople();
        this.playTime = theme.getPlayTime();
        this.price = theme.getPrice();
        this.themeUrl = theme.getThemeUrl();
        this.themeScore = theme.getThemeScore();
    }

}
