package com.example.sherlockescape.controller;


import com.example.sherlockescape.dto.ResponseDto;
import com.example.sherlockescape.dto.response.MainAchieveResponseDto;
import com.example.sherlockescape.dto.response.UpdateBadgeResponseDto;
import com.example.sherlockescape.security.user.UserDetailsImpl;
import com.example.sherlockescape.service.MemberBadgeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberBadgeController {

    private final MemberBadgeService memberBadgeService;


    /*
    *
    * 대표 칭호 수정
    * */
    @PutMapping("/badge/{badgeId}")
    public ResponseDto<UpdateBadgeResponseDto> updateBadge(@AuthenticationPrincipal UserDetails userDetails,
                                                           @PathVariable Long badgeId){
        return memberBadgeService.updateBadge(userDetails.getUsername(), badgeId);
    }

    /*
    *
    * 메인페이지 업적 조회
    * */
    @GetMapping("/main/achieve")
    public ResponseDto<MainAchieveResponseDto> getAchieve(@AuthenticationPrincipal UserDetails userDetails){
        return memberBadgeService.getAchieve(userDetails.getUsername());
    }
}
