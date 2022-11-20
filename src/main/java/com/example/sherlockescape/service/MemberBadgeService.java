package com.example.sherlockescape.service;

import com.example.sherlockescape.domain.Badge;
import com.example.sherlockescape.domain.Member;
import com.example.sherlockescape.dto.ResponseDto;
import com.example.sherlockescape.dto.response.UpdateBadgeResponseDto;
import com.example.sherlockescape.repository.BadgeRepository;
import com.example.sherlockescape.repository.MemberRepository;
import com.example.sherlockescape.utils.ValidateCheck;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberBadgeService {

    private final ValidateCheck validateCheck;
    private final BadgeRepository badgeRepository;
    private final MemberRepository memberRepository;


    //대표 뱃지 수정
    @Transactional
    public ResponseDto<UpdateBadgeResponseDto> updateBadge(Long memberId, Long badgeId) {

        Member member = validateCheck.getMember(memberId);

        Badge badge = badgeRepository.findById(badgeId).orElseThrow(
                () -> new IllegalArgumentException("뱃지가 존재하지 않습니다.")
        );

        member.updateBadge(badge.getBadgeImgUrl(), badge.getBadgeName());
        memberRepository.save(member);

        UpdateBadgeResponseDto updateBadgeResponseDto = UpdateBadgeResponseDto.builder()
                                .mainBadgeImg(member.getMainBadgeImg())
                                .mainBadgeName(member.getMainBadgeName())
                                .build();
        return ResponseDto.success(updateBadgeResponseDto);
    }
}
