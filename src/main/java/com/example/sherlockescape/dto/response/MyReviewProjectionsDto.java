package com.example.sherlockescape.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MyReviewProjectionsDto {

    private Long id;
    private String themeName;
    private Integer playTime;
    private String themeImgUrl;
    private LocalDate playDate;
    private Double score;
    private Boolean success;
    private Integer difficulty;
    private String comment;
}
