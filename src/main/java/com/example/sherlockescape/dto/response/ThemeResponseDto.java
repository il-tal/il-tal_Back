package com.example.sherlockescape.dto.response;

import com.example.sherlockescape.domain.Theme;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ThemeResponseDto {

    //테마아이디


    private String imgUrl;

    private String themeName;

//    private String companyName;

    private String genre;

    private double themeScore;

//    themeLike
//
//    reviewCnt
//

    public ThemeResponseDto(Theme theme) {
        this.imgUrl = theme.getImgUrl();
        this.themeName = theme.getThemeName();
        this.genre = theme.getGenre();
        this.themeScore = theme.getThemeScore();
    }
}
