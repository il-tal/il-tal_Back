package com.example.sherlockescape.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberBadgeResponseDto {

	private Long id;
	private String nickname;
	private String mainBadgeImg;
	private String mainBadgeName;
	private int achieveBadgeCnt;
	private int totalAchieveCnt;

}
