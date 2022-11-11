package com.example.sherlockescape.controller;

import com.example.sherlockescape.dto.ResponseDto;
import com.example.sherlockescape.dto.request.CompanyRequestDto;
import com.example.sherlockescape.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

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
     * 업체 전체 조회
     * */
}