package com.example.sherlockescape.controller;

import com.example.sherlockescape.dto.ResponseDto;
import com.example.sherlockescape.dto.request.MyTendencyRequestDto;
import com.example.sherlockescape.dto.response.AllMyInfoResponseDto;
import com.example.sherlockescape.dto.response.MyCompanyResponseDto;
import com.example.sherlockescape.dto.response.MyReviewResponseDto;
import com.example.sherlockescape.dto.response.MyThemeResponseDto;
import com.example.sherlockescape.service.CompanyService;
import com.example.sherlockescape.service.MemberService;
import com.example.sherlockescape.service.ReviewService;
import com.example.sherlockescape.service.ThemeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MyPageController {

    private final ReviewService reviewService;
    private final ThemeService themeService;
    private final CompanyService companyService;

    private final MemberService memberService;

    /*
    *
    * 내가 작성한 리뷰 조회
    * */
    @GetMapping("/myreviews")
    public ResponseDto<List<MyReviewResponseDto>> getMyReviews(@AuthenticationPrincipal UserDetails userDetails){
        List<MyReviewResponseDto> myReviewResponseDtoList = reviewService.getMyReviews(userDetails.getUsername());
        return ResponseDto.success(myReviewResponseDtoList);
    }

    /*
    *
    * 내가 찜한 테마 조회
    */
    @GetMapping("/mythemes")
    public ResponseDto<List<MyThemeResponseDto>> getMyThemes(@AuthenticationPrincipal UserDetails userDetails){
        List<MyThemeResponseDto> myThemeResponseDtoList = themeService.getMyThemes(userDetails.getUsername());
        return ResponseDto.success(myThemeResponseDtoList);
    }

    /*
     *
     * 내가 찜한 업체 조회
     * */
    @GetMapping("/mycompanies")
    public ResponseDto<List<MyCompanyResponseDto>> getMyCompanies(@AuthenticationPrincipal UserDetails userDetails){
        List<MyCompanyResponseDto> myCompanyResponseDtoList = companyService.getMyCompanies(userDetails.getUsername());
        return ResponseDto.success(myCompanyResponseDtoList);
    }

    /*
    *
    * 내 성향 등록하기
    * */
    @PostMapping("/tendency")
    public String createMyTendency(@AuthenticationPrincipal UserDetails userDetails,
                                   @RequestBody @Valid MyTendencyRequestDto myTendencyRequestDto){
       return memberService.createMyTendency(userDetails.getUsername(), myTendencyRequestDto);
    }

    /*
    *
    *내 성향 수정하기
    * */
    @PutMapping("/tendency")
    public String updateMyTendency(@AuthenticationPrincipal UserDetails userDetails,
                                   @RequestBody @Valid MyTendencyRequestDto myTendencyRequestDto){
        return memberService.updateMyTendency(userDetails.getUsername(), myTendencyRequestDto);
    }

    /*
    *
    * 내 정보 전체 불러오기
    * */
    @GetMapping("/mypage")
    public ResponseDto<AllMyInfoResponseDto> getAllMyInfo(@AuthenticationPrincipal UserDetails userDetails){
        return memberService.getAllMyInfo(userDetails.getUsername());
    }

}
