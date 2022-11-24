package com.example.sherlockescape.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoUserInfoDto {

	private Long kakaoId;
	private String nickname;

	public KakaoUserInfoDto(Long kakaoId, String nickname) {
		this.kakaoId = kakaoId;
		this.nickname = nickname;
	}
}