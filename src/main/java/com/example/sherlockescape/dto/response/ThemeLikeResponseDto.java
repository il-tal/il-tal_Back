package com.example.sherlockescape.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ThemeLikeResponseDto {
    private boolean themeLikeCheck;
    private Long themeLikeCnt;
}