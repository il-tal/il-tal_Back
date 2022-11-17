package com.example.sherlockescape.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import com.example.sherlockescape.domain.Company;
import com.example.sherlockescape.domain.CompanyLike;
import com.example.sherlockescape.domain.Member;
import com.example.sherlockescape.domain.Theme;
import com.example.sherlockescape.dto.ResponseDto;
import com.example.sherlockescape.dto.request.CompanyRequestDto;
import com.example.sherlockescape.dto.response.AllResponseDto;
import com.example.sherlockescape.dto.response.MyCompanyResponseDto;
import com.example.sherlockescape.repository.*;
import com.example.sherlockescape.utils.CommonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
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
    private final MemberRepository memberRepository;
    private final CompanyLikeRepository companyLikeRepository;
    private final ReviewRepository reviewRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    /*
     *
     * 업체 DB 정보 등록
     * */
    public ResponseDto<String> createCompany(MultipartFile multipartFile, CompanyRequestDto companyRequestDto) throws IOException {

        String fileName = CommonUtils.buildFileName(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());

        byte[] bytes = IOUtils.toByteArray(multipartFile.getInputStream());
        objectMetadata.setContentLength(bytes.length);
        ByteArrayInputStream byteArrayIs = new ByteArrayInputStream(bytes);
        amazonS3Client.putObject(new PutObjectRequest(bucketName, fileName, byteArrayIs, objectMetadata)
                .withCannedAcl(CannedAccessControlList.PublicRead));
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

            List<Theme> themeList = themeRepository.findAllByCompanyId(companyId);
            Long companyLikeCnt = companyLikeRepository.countByCompanyId(companyId);

            Optional<CompanyLike> likes = companyLikeRepository.findByCompanyId(companyId);

            boolean likeCheck;
            likeCheck = likes.isPresent();

            //댓글 수 추가
            int totalReviewCnt = 0;
            for(Theme theme: themeList){
                int reviewCnt = Math.toIntExact(reviewRepository.countByThemeId(theme.getId()));
                totalReviewCnt += reviewCnt;
            }
            //평점 계산

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
                            .themeList(themeList)
                            .totalReviewCnt(totalReviewCnt)
                            .build();
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
                            .companyScore(company.getCompanyScore())
                            .workHour(company.getWorkHour())
                            .phoneNumber(company.getPhoneNumber())
                            .location(company.getLocation())
                            .companyName(company.getCompanyName())
                            .companyScore(company.getCompanyScore())
                            .reviewCnt(reviewCnt)
                            .build();
            myCompanyResponseDtoList.add(myCompanyResponseDto);
        }
        return myCompanyResponseDtoList;
    }
}
