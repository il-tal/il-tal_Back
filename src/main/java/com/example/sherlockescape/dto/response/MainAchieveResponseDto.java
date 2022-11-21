package com.example.sherlockescape.dto.response;


import com.example.sherlockescape.domain.MemberBadge;
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
    private List<String> badgeImgUrl;
}