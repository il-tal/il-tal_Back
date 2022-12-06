package com.example.sherlockescape.repository.badge.simplequery;

import lombok.AllArgsConstructor;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BadgeSimpleQueryDto {
    private Long id;
    private String badgeImgUrl;
    private String badgeName;
    private String badgeExplain;
    private int badgeGoal;
}
