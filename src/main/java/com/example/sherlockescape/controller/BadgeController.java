package com.example.sherlockescape.controller;

import com.example.sherlockescape.dto.ResponseDto;
import com.example.sherlockescape.dto.request.BadgeRequestDto;
import com.example.sherlockescape.dto.response.BadgeResponseDto;
import com.example.sherlockescape.service.BadgeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class BadgeController {

    private final BadgeService badgeService;

    @PostMapping("/badge")
    public ResponseDto<BadgeResponseDto> createBadge(@RequestPart (value = "file", required = false)MultipartFile multipartFile,
                                                     @RequestPart (value = "badge") BadgeRequestDto badgeRequestDto){
        return badgeService.createBadge(multipartFile, badgeRequestDto);
    }
}
