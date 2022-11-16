package com.example.sherlockescape.service;

import com.example.sherlockescape.domain.Member;
import com.example.sherlockescape.domain.Theme;
import com.example.sherlockescape.domain.ThemeLike;
import com.example.sherlockescape.dto.ResponseDto;
import com.example.sherlockescape.dto.request.ThemeLikeRequestDto;
import com.example.sherlockescape.dto.response.ThemeLikeResponseDto;
import com.example.sherlockescape.repository.ThemeLikeRepository;
import com.example.sherlockescape.repository.ThemeRepository;
import com.example.sherlockescape.utils.ValidateCheck;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ThemeLikeService {

    private final ThemeLikeRepository themeLikeRepository;

    private final ThemeRepository themeRepository;
    private final ValidateCheck validateCheck;
    public ResponseDto<ThemeLikeResponseDto> themeLikeUp(ThemeLikeRequestDto themeLikeRequestDto, Long memberId) {
        Optional<ThemeLike> likes = themeLikeRepository
                .findByThemeIdAndMemberId(Long.parseLong(themeLikeRequestDto.getThemeId()), memberId);

        Member member = validateCheck.getMember(memberId);
        Theme theme = new Theme(Long.parseLong(themeLikeRequestDto.getThemeId()));

        boolean themeLikeCheck;
        if(likes.isPresent()){
            themeLikeCheck = false;
            themeLikeRepository.delete(likes.get());

        }else{
            themeLikeCheck = true;
            ThemeLike like = new ThemeLike(member, theme);
            themeLikeRepository.save(like);

        }
         Long themeLikeCnt = themeLikeRepository
                .countByThemeId(Long.parseLong(themeLikeRequestDto.getThemeId()));


        return ResponseDto.success(
                ThemeLikeResponseDto.builder()
                        .themeLikeCheck(themeLikeCheck)
                        .themeLikeCnt(themeLikeCnt)
                        .build()
        );
    }
}
