package com.example.sherlockescape.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.example.sherlockescape.domain.Company;
import com.example.sherlockescape.domain.CompanyLike;
import com.example.sherlockescape.domain.Theme;
import com.example.sherlockescape.dto.ResponseDto;
import com.example.sherlockescape.dto.request.CompanyRequestDto;
import com.example.sherlockescape.dto.response.AllResponseDto;
import com.example.sherlockescape.repository.CompanyLikeRepository;
import com.example.sherlockescape.repository.CompanyRepository;
import com.example.sherlockescape.repository.ThemeRepository;
import com.example.sherlockescape.utils.CommonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final AmazonS3Client amazonS3Client;

    private final ThemeRepository themeRepository;

    private final CompanyLikeRepository companyLikeRepository;

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
                .companyImgUrl(imgurl)
                .companyUrl(companyRequestDto.getCompanyUrl())
                .address(companyRequestDto.getAddress())
                .phoneNumber(companyRequestDto.getPhoneNumber())
                .workHour(companyRequestDto.getWorkHour())
                .build();
        companyRepository.save(company);
        return ResponseDto.success("업체 등록 성공");
    }
    //    /*업체 정보 조회*/
//    public List<CompanyResponseDto> getAll(){
//
//
//        List<Company> companyList = companyRepository.findAll();
//        List<CompanyResponseDto> allResponseDtoList = new ArrayList<>();
//        for(Company company: companyList){
//            Long companyId = company.getId();
//
//            Long companyLikeCnt = companyLikeRepository.countByCompanyId(companyId);
//            Optional<CompanyLike> likes = companyLikeRepository.findByCompanyId(companyId);
//            boolean likeCheck;
//            likeCheck = likes.isPresent();
//
//            //댓글 수 추가
//
//            CompanyResponseDto allResponseDto =
//                    CompanyResponseDto.builder()
//                            .id(companyId)
//                            .companyImgUrl(company.getImgUrl())
//                            .location(company.getLocation())
//                            .companyScore(company.getCompanyScore())
//                            .companyUrl(company.getCompanyUrl())
//                            .workHour(company.getWorkHour())
//                            .phoneNumber(company.getPhoneNumber())
//                            .address(company.getAddress())
//                            .companyLikeCnt(companyLikeCnt)
//                            .companyLikeCheck(likeCheck)
//                            .build();
//            allResponseDtoList.add(allResponseDto);
//        }
//        return allResponseDtoList;
//    }
    /*업체 정보 조회*/
    public List<AllResponseDto> getAllCompany(Pageable pageable, String location){


        List<Company> companyList = companyRepository.getCompanyList(pageable, location);
        List<AllResponseDto> allResponseDtoList = new ArrayList<>();
        for(Company company: companyList){
            Long companyId = company.getId();

            List<Theme> theme = themeRepository.findAllByCompanyId(companyId);
            Long companyLikeCnt = companyLikeRepository.countByCompanyId(companyId);
            Optional<CompanyLike> likes = companyLikeRepository.findByCompanyId(companyId);
            boolean likeCheck;
            likeCheck = likes.isPresent();
            //댓글 수 추가
            AllResponseDto allResponseDto =
                    AllResponseDto.builder()
                            .id(companyId)
                            .companyImgUrl(company.getCompanyImgUrl())
                            .location(company.getLocation())
                            .companyScore(company.getCompanyScore())
                            .companyUrl(company.getCompanyUrl())
                            .workHour(company.getWorkHour())
                            .phoneNumber(company.getPhoneNumber())
                            .address(company.getAddress())
                            .companyLikeCnt(companyLikeCnt)
                            .companyLikeCheck(likeCheck)
                            .themeList(theme).build();
            allResponseDtoList.add(allResponseDto);
        }
        return allResponseDtoList;
    }

}
