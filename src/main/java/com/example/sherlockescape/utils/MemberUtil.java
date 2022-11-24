package com.example.sherlockescape.utils;

import com.example.sherlockescape.security.jwt.JwtUtil;
import com.example.sherlockescape.security.jwt.TokenDto;
import org.springframework.http.HttpHeaders;

public class MemberUtil {
	public static final Long LEAVE_MEMBER_ID = Long.MAX_VALUE;

	public static HttpHeaders getTokenHeaders(TokenDto tokenDto) {
		HttpHeaders headers = new HttpHeaders();
		headers.add(JwtUtil.ACCESS_TOKEN, tokenDto.getAccessToken());
		headers.add(JwtUtil.REFRESH_TOKEN, tokenDto.getRefreshToken());

		return headers;
	}
}

