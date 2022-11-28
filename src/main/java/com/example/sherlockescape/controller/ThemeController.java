package com.example.sherlockescape.controller;

import com.example.sherlockescape.dto.ResponseDto;
import com.example.sherlockescape.dto.request.ThemeRequestDto;
import com.example.sherlockescape.dto.SizeResponseDto;
import com.example.sherlockescape.dto.response.ThemeResponseDto;
import com.example.sherlockescape.dto.response.TotalSizeResponseDto;
import com.example.sherlockescape.service.ThemeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ThemeController {

    private final ThemeService themeService;

    /*
     *
     * 테마 DB 등록
     * */
    @PostMapping("/theme/{companyId}")
    public ResponseDto<String> createTheme(@PathVariable Long companyId,
                                           @RequestPart(required = false, value = "file") MultipartFile multipartFile,
                                           @RequestPart(value = "theme") ThemeRequestDto themeRequestDto) throws IOException {
        return themeService.createTheme(companyId, multipartFile, themeRequestDto);

    }

//    //테마 전체 조회
//    @GetMapping("/themes")
//    public ResponseDto<List<ThemeResponseDto>> getAllTheme(@PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
//        return ResponseDto.success(themeService.findAllTheme(pageable));
//    }

    //테마 필터링
    @GetMapping("/themes")
    public SizeResponseDto<TotalSizeResponseDto,List<ThemeResponseDto>> findFilter(@PageableDefault(size = 9, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                                                                              @RequestParam(value = "location", required = false) List<String> location,
                                                                              @RequestParam(value = "genreFilter", required = false) List<String> genreFilter,
                                                                              @RequestParam(value = "themeScore", required = false) List<Integer> themeScore,
                                                                              @RequestParam(value = "difficulty", required = false) List<Integer> difficulty,
                                                                              @RequestParam(value = "people", required = false) List<Integer> people) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        TotalSizeResponseDto totalSize = themeService.filteredThemeSize(pageable, location, genreFilter, themeScore, difficulty, people);
        List<ThemeResponseDto> themeList = themeService.filter(pageable, location, genreFilter, themeScore, difficulty, people, username);
        return SizeResponseDto.success(totalSize,themeList);
    }

    //테마 상세페이지
    @GetMapping("/theme/{themeId}")
    public ResponseDto<?> getTheme(@PathVariable Long themeId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return ResponseDto.success(themeService.findTheme(themeId, username));
    }

    //메인 페이지 인기테마
    @GetMapping("/main/best")
    public ResponseDto<List<ThemeResponseDto>> getBestTheme(@PageableDefault(size=3, sort = "totalLikeCnt", direction = Sort.Direction.DESC) Pageable pageable) {
     return ResponseDto.success(themeService.findBestTheme(pageable));
    }


    //메인페이지 랜덤테마
    @GetMapping("/main/random")
    public ResponseDto<List<ThemeResponseDto>> getRandomTheme() {
        return ResponseDto.success(themeService.findRandomTheme());
    }
}