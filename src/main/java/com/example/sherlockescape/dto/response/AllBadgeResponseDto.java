package com.example.sherlockescape.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AllBadgeResponseDto {
    private Long id;
    private String badgeImgUrl;
    private String badgeName;
    private String badgeExplain;
    private int badgeGoal;
}
