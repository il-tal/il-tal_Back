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
    private String themeName;
    private LocalDate playTime;
    private int difficulty;
    private double score;
    private String comment;
}
