package com.example.sherlockescape.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
//@NoArgsConstructor
@AllArgsConstructor
public class KakaoUserInfoDto {

	private Long id;
	private String nickname;


//	public KakaoUserInfoDto(Long id, String nickname) {
//		this.id = id;
//		this.nickname = nickname;
//	}
}