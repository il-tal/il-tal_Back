package com.example.sherlockescape.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class BadgeCreateRequestDto {

    @NotBlank
    private String badgeName;

    @NotBlank
    private String badgeExplain;

    @NotBlank
    private int badgeGoal;
}
