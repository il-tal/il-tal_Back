package com.example.sherlockescape.controller;


import com.example.sherlockescape.dto.ResponseDto;
import com.example.sherlockescape.dto.request.ThemeRequestDto;
import com.example.sherlockescape.dto.response.ThemeDetailResponseDto;
import com.example.sherlockescape.dto.response.ThemeResponseDto;
import com.example.sherlockescape.service.ThemeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.example.sherlockescape.domain.QTheme.theme;

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
                                                     @RequestPart(value = "theme") ThemeRequestDto themeRequestDto){
        return themeService.createTheme(companyId, multipartFile, themeRequestDto);

    }

    //테마 전체 조회
    @GetMapping("/themes")
    public ResponseDto<List<ThemeResponseDto>> getAllTheme(@PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseDto.success(themeService.findAllTheme(pageable));
    }

    //테마 필터링
    @GetMapping("/themes/filter")
    public ResponseDto<List<ThemeResponseDto>> findFilter(@PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                                                          @RequestParam(value = "location", required = false) List<String> location,
                                                          @RequestParam(value = "genreFilter", required = false) List<String> genreFilter,
                                                          @RequestParam(value = "themeScore", required = false) List<Integer> themeScore,
                                                          @RequestParam(value = "difficulty", required = false) List<Integer> difficulty,
                                                          @RequestParam(value = "people", required = false) List<Integer> people
                                                          ){
        return ResponseDto.success(themeService.filter(pageable,location,genreFilter,themeScore,difficulty,people));
    }

    //테마 상세페이지
    @GetMapping("/theme/{themeId}")
    public ResponseDto getTheme(@PathVariable Long themeId) {
        return themeService.findTheme(themeId);
    }
}