package com.example.sherlockescape.dto.response;


import com.example.sherlockescape.domain.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AllMyInfoResponseDto {
    private String nickname;
    private int totalAchieveCnt;
    private List<GenrePreference> genrePreferenceList;
    private List<StylePreference> stylePreferenceList;
    private int lessScare;
    private int roomSize;
    private int lockStyle;
    private int device;
    private int interior;
    private int excitePreference;
    private int surprise;

    public AllMyInfoResponseDto(Member member, Tendency tendency, int totalAchieveCnt,
                                List<GenrePreference> genrePreferenceList, List<StylePreference> stylePreferenceList)
    {
        this.nickname = member.getNickname();
        this.genrePreferenceList = genrePreferenceList;
        this.stylePreferenceList = stylePreferenceList;
        this.lessScare = tendency.getLessScare();
        this.roomSize = tendency.getRoomSize();
        this.lockStyle = tendency.getLockStyle();
        this.device = tendency.getDevice();
        this.interior = tendency.getInterior();
        this.excitePreference = tendency.getExcitePreference();
        this.surprise = tendency.getSurprise();
        this.totalAchieveCnt = totalAchieveCnt;
    }
}
