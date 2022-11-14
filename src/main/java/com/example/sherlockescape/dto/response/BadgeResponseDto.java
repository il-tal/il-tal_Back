package com.example.sherlockescape.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BadgeResponseDto {
    private Long id;
    private String badgeImgUrl;
    private String badgeName;
    private String badgeExplain;
}
