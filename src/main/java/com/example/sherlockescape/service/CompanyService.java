package com.example.sherlockescape.service;

import com.example.sherlockescape.domain.*;
import com.example.sherlockescape.dto.ResponseDto;
import com.example.sherlockescape.dto.request.CompanyRequestDto;
import com.example.sherlockescape.dto.response.AllCompanyResponseDto;
import com.example.sherlockescape.dto.response.CompanyDetailResponseDto;
import com.example.sherlockescape.dto.response.MyCompanyResponseDto;
import com.example.sherlockescape.repository.*;
import com.example.sherlockescape.exception.ErrorCode;
import com.example.sherlockescape.exception.GlobalException;

import com.example.sherlockescape.utils.CommonUtils;
import com.example.sherlockescape.utils.ValidateCheck;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true) //읽기 전용 쿼리의 성능 최적화
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final ThemeRepository themeRepository;
    private final CompanyLikeRepository companyLikeRepository;
    private final ReviewRepository reviewRepository;
    private final ValidateCheck validateCheck;
    private final CommonUtils commonUtils;

    private final ThemeLikeRepository themeLikeRepository;

    /*
     *
     * 업체 DB 정보 등록
     * */
    @Transactional
    public ResponseDto<String> createCompany(MultipartFile multipartFile, CompanyRequestDto companyRequestDto) throws IOException {

        String imgUrl = commonUtils.createAll(multipartFile.getOriginalFilename(),
                                              multipartFile.getContentType(),
                                              multipartFile.getInputStream());
        Company company = Company.builder()
                .companyName(companyRequestDto.getCompanyName())
                .companyScore(companyRequestDto.getCompanyScore())
                .location(companyRequestDto.getLocation())
                .companyImgUrl(imgUrl)
                .companyUrl(companyRequestDto.getCompanyUrl())
                .address(companyRequestDto.getAddress())
                .phoneNumber(companyRequestDto.getPhoneNumber())
                .workHour(companyRequestDto.getWorkHour())
                .build();
        companyRepository.save(company);
        return ResponseDto.success("업체 등록 성공");
    }

    /*
    *
    * 업체 DB 수정
    * */
    @Transactional
    public Long updateCompany(Long companyId, MultipartFile multipartFile) throws IOException {
        Company company = companyRepository.findById(companyId).orElseThrow(
                () -> new GlobalException(ErrorCode.COMPANY_NOT_FOUND)
        );

        String imgUrl = commonUtils.createAll(multipartFile.getOriginalFilename(),
                multipartFile.getContentType(),
                multipartFile.getInputStream());

        company.updateCompanyImgUrl(imgUrl);

        return company.getId();
    }

    //업체 상세 페이지 조회
    @Transactional
    public ResponseDto<CompanyDetailResponseDto> getCompanyDetail(Long companyId, String username) {
        Company company = companyRepository.findById(companyId).orElseThrow(
                () -> new GlobalException(ErrorCode.COMPANY_NOT_FOUND)
        );

        Optional<CompanyLike> companyLike = companyLikeRepository
                .findByCompanyIdAndMemberUsername(companyId, username);

        //사용자 좋아요 체크 여부
        boolean companyLikeCheck = companyLike.isPresent();
        //좋아요 개수 카운트
        int companyLikeCnt = Math.toIntExact(companyLikeRepository.countByCompanyId(companyId));

        List<Theme> themeList = themeRepository.findAllByCompanyId(companyId);

        //리뷰 개수 카운트
        int totalReviewCnt = 0;
        for(Theme theme: themeList){
            int reviewCnt = Math.toIntExact(reviewRepository.countByThemeId(theme.getId()));
            totalReviewCnt += reviewCnt;
            Optional<ThemeLike> themeLike = themeLikeRepository.findByThemeIdAndMemberUsername(theme.getId(), username);
            boolean themeLikeCheck;
            themeLikeCheck = themeLike.isPresent();
            theme.updateThemeLikeCheck(themeLikeCheck);
        }
        CompanyDetailResponseDto companyDetailResponseDto =
                CompanyDetailResponseDto.builder()
                        .id(companyId)
                        .companyName(company.getCompanyName())
                        .companyImgUrl(company.getCompanyImgUrl())
                        .location(company.getLocation())
                        .companyScore(company.getCompanyScore())
                        .companyUrl(company.getCompanyUrl())
                        .companyLikeCnt(companyLikeCnt)
                        .companyLikeCheck(companyLikeCheck)
                        .address(company.getAddress())
                        .phoneNumber(company.getPhoneNumber())
                        .workHour(company.getWorkHour())
                        .totalReviewCnt(totalReviewCnt)
                        .themeList(themeList)
                        .build();
        return ResponseDto.success(companyDetailResponseDto);
    }

    /*업체 정보 조회*/
    public Page<AllCompanyResponseDto> getAllCompany(Pageable pageable, String location, String username){

        Page<Company> companyList = companyRepository.getCompanyList(pageable, location);
        List<AllCompanyResponseDto> allResponseDtoList = new ArrayList<>();

        for(Company company: companyList){
            Long companyId = company.getId();
            Optional<CompanyLike> companyLike = companyLikeRepository
                    .findByCompanyIdAndMemberUsername(companyId, username);

            //좋아요 여부 체크
            boolean companyLikeCheck = companyLike.isPresent();

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
                            .companyLikeCheck(companyLikeCheck)
                            .totalReviewCnt(totalReviewCnt)
                            .themeList(themeList).build();
            allResponseDtoList.add(allResponseDto);
        }
        return new PageImpl<>(allResponseDtoList, pageable, companyList.getTotalElements());
    }


    /*업체 이름 검색*/
    public Page<AllCompanyResponseDto> searchCompany(Pageable pageable, String companyName, String username){

        Page<Company> companyList = companyRepository.findByCompanyName(pageable, companyName);
        List<AllCompanyResponseDto> allResponseDtoList = new ArrayList<>();

        for(Company company: companyList){
            Long companyId = company.getId();
            Optional<CompanyLike> companyLike = companyLikeRepository
                    .findByCompanyIdAndMemberUsername(companyId, username);

            //좋아요 여부 체크
            boolean companyLikeCheck = companyLike.isPresent();

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
                            .companyLikeCheck(companyLikeCheck)
                            .totalReviewCnt(totalReviewCnt)
                            .themeList(themeList).build();
            allResponseDtoList.add(allResponseDto);
        }
        return new PageImpl<>(allResponseDtoList, pageable, companyList.getTotalElements());
    }


    /*
     *
     * 내가 찜한 업체 조회
     * */
    public List<MyCompanyResponseDto> getMyCompanies(String username) {
        List<CompanyLike> companyLikeList = companyLikeRepository.findCompanyLikesByMemberUsername(username);
        List<MyCompanyResponseDto> myCompanyResponseDtoList = new ArrayList<>();

        for(CompanyLike like: companyLikeList){
            Company company = companyRepository.findById(like.getCompany().getId()).orElseThrow(
                    () -> new GlobalException(ErrorCode.COMPANY_NOT_FOUND)
            );
            int totalLikeCnt = Math.toIntExact(companyLikeRepository.countByCompanyId(company.getId()));
            int reviewCnt = 0;
            List<Theme> themeList = themeRepository.findAllByCompanyId(company.getId());
            for(Theme theme: themeList){
                reviewCnt += Math.toIntExact(reviewRepository.countByThemeId(theme.getId()));
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
