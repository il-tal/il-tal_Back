package com.example.sherlockescape.controller;


import com.example.sherlockescape.dto.ResponseDto;
import com.example.sherlockescape.dto.request.*;
import com.example.sherlockescape.dto.response.LoginResponseDto;
import com.example.sherlockescape.dto.response.MemberResponseDto;
import com.example.sherlockescape.dto.response.NicknameResponseDto;
import com.example.sherlockescape.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.nio.charset.StandardCharsets;

@RestController
@RequiredArgsConstructor
@Api(tags = {"sherlock API"})
public class MemberController {

    private final MemberService memberService;

    //닉네임 중복확인
    @PostMapping("/nickname")
    @ApiOperation(value = "닉네임 중복 확인", notes = "닉네임 중복 확인 API")
    public ResponseEntity<ResponseDto<String>> nicknameDuplicateCheck(@RequestBody @Valid NicknameDuplicateRequestDto nicknameDuplicateRequestDto){
        return memberService.nicknameDuplicateCheck(nicknameDuplicateRequestDto.getNickname());
    }

    //아이디 중복 확인
    @PostMapping("/username")
    @ApiOperation(value = "아이디 중복 확인", notes = "아이디 중복 확인 API")
    public ResponseEntity<ResponseDto<String>> usernameDuplicateCheck(@RequestBody @Valid UsernameRequestDto usernameRequestDto){
        return memberService.usernameDuplicateCheck(usernameRequestDto.getUsername());
    }

    //회원가입
    @PostMapping("/signup")
    public ResponseEntity<ResponseDto<MemberResponseDto>> registerMember(@RequestBody @Valid MemberRequestDto memberRequestDto){
        return memberService.signup(memberRequestDto);
    }

    //로그인
    @PostMapping("/login")
    public ResponseEntity<ResponseDto<LoginResponseDto>> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse httpServletResponse){
        return memberService.login(loginRequestDto, httpServletResponse);
    }

    //로그아웃
    @PostMapping("/api/logout")
    public HttpHeaders setHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
        return headers;
    }

    /*
    *
    * 닉네임 수정하기
    * */
    @PutMapping("/nickname")
    public ResponseDto<NicknameResponseDto> updateNickname(@AuthenticationPrincipal UserDetails userDetails,
                                                           @RequestBody NicknameRequestDto nicknameRequestDto){
        return memberService.updateNickname(userDetails.getUsername(), nicknameRequestDto);
    }

}
