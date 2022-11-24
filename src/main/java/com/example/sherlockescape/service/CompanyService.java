package com.example.sherlockescape.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.example.sherlockescape.dto.ResponseDto;
import com.example.sherlockescape.dto.request.CompanyRequestDto;
import com.example.sherlockescape.dto.response.AllCompanyResponseDto;
import com.example.sherlockescape.dto.response.CompanyDetailResponseDto;
import com.example.sherlockescape.dto.response.MyCompanyResponseDto;
import com.example.sherlockescape.repository.*;
import com.example.sherlockescape.utils.CommonUtils;
import com.example.sherlockescape.domain.Company;
import com.example.sherlockescape.domain.CompanyLike;
import com.example.sherlockescape.domain.Member;
import com.example.sherlockescape.domain.Theme;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final AmazonS3Client amazonS3Client;
    private final ThemeRepository themeRepository;
    private final MemberRepository memberRepository;
    private final CompanyLikeRepository companyLikeRepository;
    private final ReviewRepository reviewRepository;

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


    //업체 상세 페이지 조회
    public ResponseDto<CompanyDetailResponseDto> getCompanyDetail(Long companyId) {
        Company company = companyRepository.findById(companyId).orElseThrow(
                () -> new IllegalArgumentException("해당 업체가 존재하지 않습니다.")
        );
        int companyLike = Math.toIntExact(companyLikeRepository.countByCompanyId(companyId));
        List<Theme> themeList = themeRepository.findAllByCompanyId(companyId);
        int totalReviewCnt = 0;
        for(Theme theme: themeList){
            int reviewCnt = Math.toIntExact(reviewRepository.countByThemeId(theme.getId()));
            totalReviewCnt += reviewCnt;
        }
        CompanyDetailResponseDto companyDetailResponseDto =
                CompanyDetailResponseDto.builder()
                        .id(companyId)
                        .companyName(company.getCompanyName())
                        .companyImgUrl(company.getCompanyImgUrl())
                        .location(company.getLocation())
                        .companyScore(company.getCompanyScore())
                        .companyUrl(company.getCompanyUrl())
                        .companyLikeCnt(companyLike)
                        .address(company.getAddress())
                        .phoneNumber(company.getPhoneNumber())
                        .workHour(company.getWorkHour())
                        .totalReviewCnt(totalReviewCnt)
                        .themeList(themeList)
                        .build();
        return ResponseDto.success(companyDetailResponseDto);
    }

    /*업체 정보 조회*/
    public List<AllCompanyResponseDto> getAllCompany(Pageable pageable, String location){

        List<Company> companyList = companyRepository.getCompanyList(pageable, location);
        List<AllCompanyResponseDto> allResponseDtoList = new ArrayList<>();
        for(Company company: companyList){
            Long companyId = company.getId();

            List<Theme> themeList = themeRepository.findAllByCompanyId(companyId);
            Long companyLikeCnt = companyLikeRepository.countByCompanyId(companyId);
            //댓글 수 추가
            int totalReviewCnt = 0;
            for(Theme theme: themeList){
                int reviewCnt = Math.toIntExact(reviewRepository.countByThemeId(theme.getId()));
                totalReviewCnt += reviewCnt;
            }
            AllCompanyResponseDto allResponseDto =
                    AllCompanyResponseDto.builder()
                            .id(companyId)
                            .companyName(company.getCompanyName())
                            .companyImgUrl(company.getCompanyImgUrl())
                            .location(company.getLocation())
                            .companyScore(company.getCompanyScore())
                            .companyUrl(company.getCompanyUrl())
                            .workHour(company.getWorkHour())
                            .phoneNumber(company.getPhoneNumber())
                            .address(company.getAddress())
                            .companyLikeCnt(companyLikeCnt)
                            .totalReviewCnt(totalReviewCnt)
                            .themeList(themeList).build();
            allResponseDtoList.add(allResponseDto);
        }
        return allResponseDtoList;
    }

    /*
    *
    * 내가 찜한 업체 조회
    * */
    public List<MyCompanyResponseDto> getMyCompanies(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new IllegalArgumentException("사용자를 찾을 수 없습니다.")
        );
        List<CompanyLike> companyLikeList = companyLikeRepository.findCompanyLikesByMemberId(memberId);
        List<MyCompanyResponseDto> myCompanyResponseDtoList = new ArrayList<>();

        for(CompanyLike like: companyLikeList){
            Company company = companyRepository.findById(like.getCompany().getId()).orElseThrow(
                    () -> new IllegalArgumentException("업체를 찾을 수 없습니다.")
            );
            int totalLikeCnt = Math.toIntExact(companyLikeRepository.countByCompanyId(company.getId()));
            int reviewCnt = 0;
            List<Theme> themeList = themeRepository.findAllByCompanyId(company.getId());
            for(Theme theme: themeList){
                reviewCnt += reviewRepository.countByThemeId(theme.getId());
            }
            MyCompanyResponseDto myCompanyResponseDto =
                    MyCompanyResponseDto.builder()
                            .id(company.getId())
                            .companyUrl(company.getCompanyUrl())
                            .address(company.getAddress())
                            .companyImgUrl(company.getCompanyImgUrl())
                            .companyScore(company.getCompanyScore())
                            .workHour(company.getWorkHour())
                            .phoneNumber(company.getPhoneNumber())
                            .location(company.getLocation())
                            .companyName(company.getCompanyName())
                            .companyScore(company.getCompanyScore())
                            .totalReviewCnt(reviewCnt)
                            .totalLikeCnt(totalLikeCnt)
                            .build();
            myCompanyResponseDtoList.add(myCompanyResponseDto);
        }
        return myCompanyResponseDtoList;
    }
}
