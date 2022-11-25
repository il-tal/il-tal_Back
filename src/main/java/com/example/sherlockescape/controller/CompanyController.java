package com.example.sherlockescape.controller;

import com.example.sherlockescape.dto.ResponseDto;
import com.example.sherlockescape.dto.request.CompanyRequestDto;
import com.example.sherlockescape.dto.response.AllCompanyResponseDto;
import com.example.sherlockescape.dto.response.CompanyDetailResponseDto;
import com.example.sherlockescape.service.CompanyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CompanyController {

    private final CompanyService companyService;

    /*
     *
     * 업체 DB 등록
     * */
    @PostMapping("/company")
    public ResponseDto<String> createCompany(@RequestPart(required = false, value = "file") MultipartFile multipartFile,
                                             @RequestPart(value = "company") @Valid CompanyRequestDto companyRequestDto) throws IOException {
        return companyService.createCompany(multipartFile, companyRequestDto);
    }

    /*
     *
     * 업체 상세 페이지
     * *
     */
    @GetMapping("/company/{companyId}")
    public ResponseDto<CompanyDetailResponseDto> getAll(@PathVariable Long companyId){
        //가입회원 비가입회원 구분
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return companyService.getCompanyDetail(companyId, username);
    }

    /*
     *
     *업체,테마 정보 전체
     */
    @GetMapping("/companies")
    public ResponseDto<List<AllCompanyResponseDto>> getAllCompany(@PageableDefault(size = 5) Pageable pageable,
                                                                  @RequestParam(value = "location", required = false) String location){

        //가입회원 비가입회원 구분
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        List<AllCompanyResponseDto> resDto = companyService.getAllCompany(pageable, location, username);
        return ResponseDto.success(resDto);
    }


}