package com.example.sherlockescape.dto.request;

import com.example.sherlockescape.security.jwt.TokenDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SocialUserDto {
	private MemberDto memberDto;
	private TokenDto tokenDto;
}
