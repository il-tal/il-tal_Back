package com.example.sherlockescape.controller;

import com.example.sherlockescape.dto.ResponseDto;
import com.example.sherlockescape.dto.request.CompanyRequestDto;
import com.example.sherlockescape.dto.response.AllResponseDto;
import com.example.sherlockescape.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
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
     * 업체 정보 조회
     * *
     */
//    @GetMapping("/company")
//    public ResponseDto<List<CompanyResponseDto>> getAll(){
//        List<CompanyResponseDto> resDto = companyService.getAll();
//        return ResponseDto.success(resDto);
//    }

    /*
     *
     *업체,테마 정보 전체
     */
    @GetMapping("/companies")
    public ResponseDto<List<AllResponseDto>> getAllCompany(@PageableDefault(size = 5) Pageable pageable,
                                                           @RequestParam(value = "location", required = false) String location){
        List<AllResponseDto> resDto = companyService.getAllCompany(pageable, location);
        return ResponseDto.success(resDto);
    }


}