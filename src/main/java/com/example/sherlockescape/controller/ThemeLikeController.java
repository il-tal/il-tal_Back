package com.example.sherlockescape.controller;

import com.example.sherlockescape.dto.ResponseDto;
import com.example.sherlockescape.dto.request.ThemeLikeRequestDto;
import com.example.sherlockescape.dto.response.ThemeLikeResponseDto;
import com.example.sherlockescape.security.user.UserDetailsImpl;
import com.example.sherlockescape.service.ThemeLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/theme/wish")
@RequiredArgsConstructor
public class ThemeLikeController {

    private final ThemeLikeService themeLikeService;

    @PostMapping
    public ResponseDto<ThemeLikeResponseDto> themeLikeUp(@RequestBody @Valid ThemeLikeRequestDto themeLikeRequestDto,
                                                         @AuthenticationPrincipal UserDetailsImpl userDetailsImpl){
        return themeLikeService.themeLikeUp(themeLikeRequestDto, userDetailsImpl.getMember().getId());
    }
}
