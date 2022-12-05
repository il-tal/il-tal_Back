package com.example.sherlockescape.controller;


import com.example.sherlockescape.dto.ResponseDto;
import com.example.sherlockescape.dto.response.MainAchieveResponseDto;
import com.example.sherlockescape.dto.response.MemberBadgeResponseDto;
import com.example.sherlockescape.dto.response.UpdateBadgeResponseDto;
import com.example.sherlockescape.repository.MemberRepository;
import com.example.sherlockescape.service.MemberBadgeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberBadgeController {

    private final MemberBadgeService memberBadgeService;
    private final MemberRepository memberRepository;


    /*
     *
     * 대표 칭호 수정
     * */
    @PutMapping("/badge/{badgeId}")
    public ResponseDto<UpdateBadgeResponseDto> updateBadge(@AuthenticationPrincipal UserDetails userDetails,
                                                           @PathVariable Long badgeId) {
        return memberBadgeService.updateBadge(userDetails.getUsername(), badgeId);
    }

    /*
     *
     * 메인페이지 업적 조회
     * */
    @GetMapping("/main/achieve")
    public ResponseDto<MainAchieveResponseDto> getAchieve(@AuthenticationPrincipal UserDetails userDetails) {
        return memberBadgeService.getAchieve(userDetails.getUsername());
    }

    // 메인페이지 - 명예의 전당
    @GetMapping("/main/hof")
    public ResponseDto<Page<MemberBadgeResponseDto>> getMemberRank(
            @PageableDefault(size = 4, sort = "achieveBadgeCnt", direction = Sort.Direction.DESC) Pageable pageable) {

        //가입회원 비가입회원 구분
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Page<MemberBadgeResponseDto> resDto = memberBadgeService.getMemberRank(pageable);
        return ResponseDto.success(resDto);
    }
}
