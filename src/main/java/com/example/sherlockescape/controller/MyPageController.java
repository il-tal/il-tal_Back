package com.example.sherlockescape.controller;

import com.example.sherlockescape.dto.ResponseDto;
import com.example.sherlockescape.dto.response.MyReviewResponseDto;
import com.example.sherlockescape.security.user.UserDetailsImpl;
import com.example.sherlockescape.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MyPageController {

    private final ReviewService reviewService;

    /*
    *
    * 내가 작성한 리뷰 조회
    * */
    @GetMapping("/myreviews")
    public ResponseDto<List<MyReviewResponseDto>> getMyReviews(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl){
        List<MyReviewResponseDto> myReviewResponseDtoList = reviewService.getAllReviews(userDetailsImpl.getMember());
        return ResponseDto.success(myReviewResponseDtoList);
    }

    /*
    *
    * 내가 찜한 테마 조회
    */
}
