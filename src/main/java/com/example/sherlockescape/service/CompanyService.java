package com.example.sherlockescape.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.example.sherlockescape.domain.Company;
import com.example.sherlockescape.dto.ResponseDto;
import com.example.sherlockescape.dto.request.CompanyRequestDto;
import com.example.sherlockescape.repository.CompanyRepository;
import com.example.sherlockescape.utils.CommonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    /*
     *
     * 업체 DB 정보 등록
     * */
    public ResponseDto<String> createCompany(MultipartFile multipartFile, CompanyRequestDto companyRequestDto) {
        String fileName = CommonUtils.buildFileName(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        String imgurl = amazonS3Client.getUrl(bucketName, fileName).toString();

        Company company = Company.builder()
                .companyName(companyRequestDto.getCompanyName())
                .companyScore(companyRequestDto.getCompanyScore())
                .location(companyRequestDto.getLocation())
                .imgUrl(imgurl)
                .companyUrl(companyRequestDto.getCompanyUrl())
                .build();
        companyRepository.save(company);
        return ResponseDto.success("업체 등록 성공");
    }
}
