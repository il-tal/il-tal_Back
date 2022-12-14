package com.example.sherlockescape.service;

import com.example.sherlockescape.domain.Member;
import com.example.sherlockescape.domain.RefreshToken;
import com.example.sherlockescape.dto.request.KakaoUserInfoDto;
import com.example.sherlockescape.dto.response.KakaoUserResponseDto;
import com.example.sherlockescape.repository.MemberRepository;
import com.example.sherlockescape.repository.RefreshTokenRepository;
import com.example.sherlockescape.security.jwt.JwtUtil;
import com.example.sherlockescape.security.jwt.TokenDto;
import com.example.sherlockescape.security.user.UserDetailsImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KakaoUserService {

//	@Value("${kakao.client-id}")
//	String clientId;
//	@Value("${kakao.redirect-uri}")
//	String redirectUri;
//	@Value("${kakao.authorization-grant-type}")
//	String authorization_code;
//	@Value("${kakao.token-uri}")
//	String tokenUri;
//	@Value("${kakao.user-info-uri}")
//	String userInfoUri;


	private final PasswordEncoder passwordEncoder;
	private final MemberRepository memberRepository;
	private final RefreshTokenRepository refreshTokenRepository;
	private final JwtUtil jwtUtil;

	public KakaoUserResponseDto kakaoLogin(String code, HttpServletResponse response) throws JsonProcessingException {

		// 1. "?????? ??????"??? "????????? ??????" ??????
		String accessToken = getAccessToken(code);

		// 2. ???????????? ????????? API ??????
		KakaoUserInfoDto kakaoUserInfo = getKakaoUserInfo(accessToken);

		// 3. ????????? ID ??? ???????????? ??????
		Member kakaoUser = signupKakaoUser(kakaoUserInfo);

		// 4. ?????? ????????? ??????
		forceLogin(kakaoUser);

		// 5. response Header ??? JWT ?????? ??????
		TokenDto tokenDto = jwtUtil.createAllToken(kakaoUserInfo.getId().toString());

		Optional<RefreshToken> refreshToken = refreshTokenRepository.findById(kakaoUser.getId());

		if (refreshToken.isPresent()) {
			refreshTokenRepository.save(refreshToken.get().updateToken(tokenDto.getRefreshToken()));
		} else {
			RefreshToken newToken = new RefreshToken(tokenDto.getRefreshToken(), kakaoUserInfo.getId().toString());
			refreshTokenRepository.save(newToken);
		}
		// ?????? ?????? ??? tokenDto ??? ??????
		setHeader(response, tokenDto);

		Member member = memberRepository.findByKakaoId(kakaoUser.getKakaoId()).orElse(null);
		assert member != null;
		return KakaoUserResponseDto.builder()
				.username(member.getUsername())
				.nickname(member.getNickname())
				.build();
	}

	// 1. "?????? ??????"??? "????????? ??????" ??????
	private String getAccessToken(String code) throws JsonProcessingException {
		// HTTP Header ??????
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		// HTTP Body ??????
		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("grant_type", "authorization_code");
		body.add("client_id", "7555650f225a920f06012d44affc4f03");
		body.add("redirect_uri","https://il-tal.com/kakao/callback");
		body.add("code", code);
		// HTTP ?????? ?????????
		HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(body, headers);
		RestTemplate rt = new RestTemplate();
		ResponseEntity<String> response = rt.exchange(
				"https://Kauth.Kakao.com/oauth/token",
				HttpMethod.POST,
				kakaoTokenRequest,
				String.class
		);
		// HTTP ?????? (JSON) -> ????????? ?????? ??????
		String responseBody = response.getBody();
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(responseBody);
		return jsonNode.get("access_token").asText();
	}

	// 2. ???????????? ????????? API ??????
	private KakaoUserInfoDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
		// HTTP Header ??????
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + accessToken);
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		// HTTP ?????? ?????????
		HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
		RestTemplate rt = new RestTemplate();
		ResponseEntity<String> response = rt.exchange(
				"https://kapi.kakao.com/v2/user/me",
				HttpMethod.POST,
				kakaoUserInfoRequest,
				String.class
		);
		// responseBody ??? ?????? ????????? ??????
		String responseBody = response.getBody();
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(responseBody);
		Long id = jsonNode.get("id").asLong();
		String nickname = jsonNode.get("properties").get("nickname").asText();

		return new KakaoUserInfoDto(id, nickname);
	}

    // 3. ????????? ID ??? ???????????? ??????
    private Member signupKakaoUser (KakaoUserInfoDto kakaoUserInfo) {
        // DB ??? ????????? email ??? ????????? ??????
		String kakaoId = kakaoUserInfo.getId().toString();
		String nickname = kakaoUserInfo.getNickname();
        Member kakaoUser = memberRepository.findByKakaoId(kakaoId)
                .orElse(null);

        if (kakaoUser == null) {
            // ????????????
            // password: random UUID
            String password = UUID.randomUUID().toString();
            String encodedPassword = passwordEncoder.encode(password);
			String mainBadgeImg = "https://mykeejaebucket.s3.ap-northeast-2.amazonaws.com/Serverbasigbadge.1668884794370.png";
			String mainBadgeName = "????????? ????????? ?????????!";

            kakaoUser = new Member(kakaoId, nickname, encodedPassword, mainBadgeImg, mainBadgeName);
            memberRepository.save(kakaoUser);
        }
        return kakaoUser;
    }

	// 4. ?????? ????????? ??????
	private Authentication forceLogin(Member kakaoUser) {
		UserDetails userDetails = new UserDetailsImpl(kakaoUser);
		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);
		return authentication;
	}

	// 5. response Header ??? JWT ?????? ??????
	private void setHeader(HttpServletResponse response, TokenDto tokenDto) {
		response.addHeader(JwtUtil.ACCESS_TOKEN, tokenDto.getAccessToken());
		response.addHeader(JwtUtil.REFRESH_TOKEN, tokenDto.getRefreshToken());
	}
}