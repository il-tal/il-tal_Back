package com.example.sherlockescape.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MainAchieveResponseDto {
    private String mainBadgeImg;
    private String mainBadgeName;
    private String nickname;
    private int totalAchieveCnt;
    private int totalFailCnt;
    private int achieveBadgeCnt;
    private List<String> badgeImgUrl;
}
