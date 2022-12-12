package com.example.sherlockescape.controller;

import com.example.sherlockescape.dto.ResponseDto;
import com.example.sherlockescape.dto.request.BadgeCreateRequestDto;
import com.example.sherlockescape.dto.request.BadgeGiveRequestDto;
import com.example.sherlockescape.dto.response.BadgeResponseDto;
import com.example.sherlockescape.dto.response.MyBadgeProjectionsDto;
import com.example.sherlockescape.repository.badge.simplequery.BadgeSimpleQueryDto;
import com.example.sherlockescape.service.BadgeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class BadgeController {

    private final BadgeService badgeService;

    /*
    *
    * badge DB등록
    * */
    @PostMapping("/badge")
    public ResponseDto<BadgeResponseDto> createBadge(@RequestPart (value = "file", required = false)MultipartFile multipartFile,
                                                     @RequestPart (value = "badge") BadgeCreateRequestDto badgeCreateRequestDto) throws IOException {
        return badgeService.createBadge(multipartFile, badgeCreateRequestDto);
    }
    /*
    *
    * badge전체조회
    * */
    @GetMapping("/badges")
    public ResponseDto<List<BadgeSimpleQueryDto>> getAllBadge(){
        List<BadgeSimpleQueryDto> allBadgeResponseDtoList = badgeService.getAllBadge();
        return ResponseDto.success(allBadgeResponseDtoList);
    }
    /*
    *
    * member badge 조회
    * */
    @GetMapping("/member/badges")
    public ResponseDto<List<MyBadgeProjectionsDto>> getMemberBadges(@AuthenticationPrincipal UserDetails userDetails){
        List<MyBadgeProjectionsDto> myBadgeProjectionsDto = badgeService.getMemberBadges(userDetails.getUsername());
        return ResponseDto.success(myBadgeProjectionsDto);
    }

    /*
    *
    * badge 부여
    * */
    @PostMapping("/badge/give")
    public ResponseDto<BadgeResponseDto> giveBadge(@AuthenticationPrincipal UserDetails userDetails,
                                                   @RequestBody BadgeGiveRequestDto badgeGiveRequestDto){
        return badgeService.giveBadge(userDetails.getUsername(), badgeGiveRequestDto);
    }

}
