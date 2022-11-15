package com.example.sherlockescape.controller;

import com.example.sherlockescape.dto.ResponseDto;
import com.example.sherlockescape.dto.response.MyCompanyResponseDto;
import com.example.sherlockescape.dto.response.MyReviewResponseDto;
import com.example.sherlockescape.dto.response.MyThemeResponseDto;
import com.example.sherlockescape.security.user.UserDetailsImpl;
import com.example.sherlockescape.service.CompanyService;
import com.example.sherlockescape.service.ReviewService;
import com.example.sherlockescape.service.ThemeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MyPageController {

    private final ReviewService reviewService;
    private final ThemeService themeService;
    private final CompanyService companyService;

    /*
    *
    * 내가 작성한 리뷰 조회
    * */
    @GetMapping("/myreviews")
    public ResponseDto<List<MyReviewResponseDto>> getMyReviews(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl){
        List<MyReviewResponseDto> myReviewResponseDtoList = reviewService.getMyReviews(userDetailsImpl.getMember());
        return ResponseDto.success(myReviewResponseDtoList);
    }

    /*
    *
    * 내가 찜한 테마 조회
    */
    @GetMapping("/mythemes")
    public ResponseDto<List<MyThemeResponseDto>> getMyThemes(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl){
        List<MyThemeResponseDto> myThemeResponseDtoList = themeService.getMyThemes(userDetailsImpl.getMember().getId());
        return ResponseDto.success(myThemeResponseDtoList);
    }

    /*
     *
     * 내가 찜한 업체 조회
     * */
    @GetMapping("/mycompanies")
    public ResponseDto<List<MyCompanyResponseDto>> getMyCompanies(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl){
        List<MyCompanyResponseDto> myCompanyResponseDtoList = companyService.getMyCompanies(userDetailsImpl.getMember().getId());
        return ResponseDto.success(myCompanyResponseDtoList);
    }
}
