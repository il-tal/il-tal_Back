package com.example.sherlockescape.controller;

import com.example.sherlockescape.dto.request.KakaoUserInfoDto;
import com.example.sherlockescape.service.KakaoUserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
public class SocialLoginController {
	private final KakaoUserService kakaoUserService;

	// 카카오 로그인
	@GetMapping("/kakao/callback")
	//-JIAlL23y9MDLlFj3QZ9d975eJujlOVm2iy_pzUItSE6xUvuVveeA-qwFio6P_wD3e4I9wo9dJcAAAGEwiHisg
	public KakaoUserInfoDto kakaoLogin(@RequestParam(name="code") String code,
									   HttpServletResponse response) throws JsonProcessingException {
		return kakaoUserService.kakaoLogin(code, response);
	}
}