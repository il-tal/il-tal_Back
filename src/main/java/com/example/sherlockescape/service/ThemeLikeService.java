package com.example.sherlockescape.service;

import com.example.sherlockescape.domain.Member;
import com.example.sherlockescape.domain.Theme;
import com.example.sherlockescape.domain.ThemeLike;
import com.example.sherlockescape.dto.ResponseDto;
import com.example.sherlockescape.dto.request.ThemeLikeRequestDto;
import com.example.sherlockescape.dto.response.ThemeLikeResponseDto;
import com.example.sherlockescape.repository.review.exception.ErrorCode;
import com.example.sherlockescape.repository.review.exception.GlobalException;
import com.example.sherlockescape.repository.ThemeLikeRepository;
import com.example.sherlockescape.repository.ThemeRepository;
import com.example.sherlockescape.utils.ValidateCheck;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ThemeLikeService {

    private final ThemeLikeRepository themeLikeRepository;
    private final ThemeRepository themeRepository;
    private final ValidateCheck validateCheck;

    @Transactional
    public ResponseDto<ThemeLikeResponseDto> themeLikeUp(ThemeLikeRequestDto themeLikeRequestDto, String username) {
        Optional<ThemeLike> likes = themeLikeRepository
                .findByThemeIdAndMemberUsername(Long.parseLong(themeLikeRequestDto.getThemeId()), username);

        Member member = validateCheck.getMember(username);
        Theme theme = new Theme(Long.parseLong(themeLikeRequestDto.getThemeId()));

        Theme updateTheme = themeRepository.findById(theme.getId()).orElseThrow(
                () -> new GlobalException(ErrorCode.THEME_NOT_FOUND)
        );

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

        //준영속 엔티티 변경 감지 기능 적용
        updateTheme.updateTotalLikeCnt(themeLikeCnt.intValue());
        themeRepository.save(updateTheme);

        return ResponseDto.success(
                ThemeLikeResponseDto.builder()
                        .themeLikeCheck(themeLikeCheck)
                        .themeLikeCnt(themeLikeCnt)
                        .build()
        );
    }
}
