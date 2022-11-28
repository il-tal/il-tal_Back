package com.example.sherlockescape.controller;

import com.example.sherlockescape.dto.ResponseDto;
import com.example.sherlockescape.dto.request.CompanyLikeRequestDto;
import com.example.sherlockescape.dto.response.CompanyLikeResponseDto;
import com.example.sherlockescape.service.CompanyLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/company/wish")
@RequiredArgsConstructor
public class CompanyLikeController {

    private final CompanyLikeService companyLikeService;

    /*
    *
    * 업체 좋아요
    * */
    @PostMapping
    public ResponseDto<CompanyLikeResponseDto> companyLikeUp(@RequestBody @Valid CompanyLikeRequestDto companyLikeRequestDto,
                                                             @AuthenticationPrincipal UserDetails userDetails){
        return companyLikeService.companyLikeUp(companyLikeRequestDto, userDetails.getUsername());
    }
}