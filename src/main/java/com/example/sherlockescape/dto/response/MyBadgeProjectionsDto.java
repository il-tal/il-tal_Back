package com.example.sherlockescape.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MyBadgeProjectionsDto {

    public Long id;
    public String badgeName;
    public String badgeImgUrl;
    public String badgeExplain;
}
