package com.example.sherlockescape.dto.response;


import com.example.sherlockescape.domain.Member;
import com.example.sherlockescape.domain.Tendency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AllMyInfoResponseDto {
    private Long id;
    private String nickname;
    private int totalAchieveCnt;
//    private List<GenrePreference> genrePreferenceList;
//    private List<StylePreference> stylePreferenceList;
    private String genrePreference;
    private String stylePreference;
    private int lessScare;
    private int roomSize;
    private int lockStyle;
    private int device;
    private int interior;
    private int excitePreference;
    private int surprise;

    public AllMyInfoResponseDto(Member member, Tendency tendency, int totalAchieveCnt)
    {
        this.id = member.getId();
        this.nickname = member.getNickname();
        this.genrePreference = tendency.getGenrePreference();
        this.stylePreference = tendency.getStylePreference();
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
