package com.example.sherlockescape.controller;


import com.example.sherlockescape.dto.ResponseDto;
import com.example.sherlockescape.dto.request.ThemeRequestDto;
import com.example.sherlockescape.dto.response.ThemeResponseDto;
import com.example.sherlockescape.service.ThemeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public ResponseDto<ThemeResponseDto> createTheme(@PathVariable Long companyId,
                                                     @RequestPart(required = false, value = "file") MultipartFile multipartFile,
                                                     @RequestPart(value = "theme") ThemeRequestDto themeRequestDto){
        return themeService.createTheme(companyId, multipartFile, themeRequestDto);
    }

    //테마 전체 조회
    @GetMapping("/theme")
    public ResponseDto<List<ThemeResponseDto>> getAllTheme(@PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseDto.success(themeService.findAllTheme(pageable));
    }

    //테마 필터링
    @GetMapping("/theme/filter")
    public ResponseDto<List<ThemeResponseDto>> findFilter(@PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                                                          @RequestParam(value = "location", required = false) List<String> location,
                                                          @RequestParam(value = "genre", required = false) List<String> genre){
        return ResponseDto.success(themeService.filter(pageable,location,genre));
    }
}