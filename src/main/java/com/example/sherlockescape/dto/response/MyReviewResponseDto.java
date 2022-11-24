package com.example.sherlockescape.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyReviewResponseDto {
    private Long id;
    private String themeName;
    private int playTime;
    private LocalDate playDate;
    private int difficulty;
    private double score;
    private String comment;
    private boolean success;
    private int totalAchieveCnt;
    private int totalFailCnt;
}
