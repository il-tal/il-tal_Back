package com.example.sherlockescape.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import com.example.sherlockescape.domain.Company;
import com.example.sherlockescape.domain.Member;
import com.example.sherlockescape.domain.Theme;
import com.example.sherlockescape.domain.ThemeLike;
import com.example.sherlockescape.dto.ResponseDto;
import com.example.sherlockescape.dto.request.ThemeRequestDto;
import com.example.sherlockescape.dto.response.MyThemeResponseDto;
import com.example.sherlockescape.dto.response.ThemeDetailResponseDto;
import com.example.sherlockescape.dto.response.ThemeResponseDto;
import com.example.sherlockescape.repository.*;
import com.example.sherlockescape.utils.CommonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ThemeService {

    private final ThemeRepository themeRepository;
    private final CompanyRepository companyRepository;
    private final AmazonS3Client amazonS3Client;

    private final MemberRepository memberRepository;

    private final ReviewRepository reviewRepository;
    private final ThemeLikeRepository themeLikeRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;


    /*
     *
     * 테마 DB 등록
     * */
    public ResponseDto<String> createTheme(Long companyId, MultipartFile multipartFile, ThemeRequestDto themeReqDto) throws IOException {
        Company company = companyRepository.findById(companyId).orElseThrow(
                () -> new IllegalArgumentException("해당 업체가 존재하지 않습니다.")
        );

        String fileName = CommonUtils.buildFileName(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());

        byte[] bytes = IOUtils.toByteArray(multipartFile.getInputStream());
        objectMetadata.setContentLength(bytes.length);
        ByteArrayInputStream byteArrayIs = new ByteArrayInputStream(bytes);
        amazonS3Client.putObject(new PutObjectRequest(bucketName, fileName, byteArrayIs, objectMetadata)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        String imgurl = amazonS3Client.getUrl(bucketName, fileName).toString();

        Theme theme = Theme.builder()
                .company(company)
                .themeName(themeReqDto.getThemeName())
                .themeImgUrl(imgurl)
                .difficulty(themeReqDto.getDifficulty())
                .genre(themeReqDto.getGenre())
                .genreFilter(themeReqDto.getGenreFilter())
                .playTime(themeReqDto.getPlayTime())
                .synopsis(themeReqDto.getSynopsis())
                .themeScore(themeReqDto.getThemeScore())
                .themeUrl(themeReqDto.getThemeUrl())
                .minPeople(themeReqDto.getMinPeople())
                .maxPeople(themeReqDto.getMaxPeople())
                .price(themeReqDto.getPrice())
                .build();
        themeRepository.save(theme);
        return ResponseDto.success("테마 등록 성공");
    }

//    //테마 전체 조회
//    public List<ThemeResponseDto> findAllTheme(Pageable pageable) {
//
//        Page<Theme> allTheme = themeRepository.findAll(pageable);
//        List<ThemeResponseDto> themeLists = allTheme.stream()
//                .map(ThemeResponseDto::new).collect(Collectors.toList());
//        return themeLists;
//    }

    //테마 필터링
    public List<ThemeResponseDto> filter(Pageable pageable, List<String> location, List<String> genreFilter, List<Integer> themeScore, List<Integer> difficulty, List<Integer> people){

        Page<Theme> filteredTheme = themeRepository.findFilter(pageable, location, genreFilter, themeScore, difficulty, people);

        List<ThemeResponseDto> themeLists = new ArrayList<>();
        for(Theme theme: filteredTheme) {
            int reviewCnt = Math.toIntExact(reviewRepository.countByThemeId(theme.getId()));
            ThemeResponseDto themeResponseDto =
                    ThemeResponseDto.builder()
                            .id(theme.getId())
                            .themeImgUrl(theme.getThemeImgUrl())
                            .themeName(theme.getThemeName())
                            .companyName(theme.getCompany().getCompanyName())
                            .genre(theme.getGenre())
                            .themeScore(theme.getThemeScore())
                            .totalLikeCnt(theme.getTotalLikeCnt())
                            .reviewCnt(reviewCnt)
                            .build();
            themeLists.add(themeResponseDto);
        }
        return themeLists;
    }


    //테마 상세조회
    public ThemeDetailResponseDto findTheme(Long themeId) {

        Theme theme = themeRepository.findById(themeId).orElseThrow(
                () -> new IllegalArgumentException("테마가 존재하지 않습니다"));

        int reviewCnt = Math.toIntExact(reviewRepository.countByThemeId(theme.getId()));
        ThemeDetailResponseDto themeDetailResponseDto =
                ThemeDetailResponseDto.builder()
                        .id(theme.getId())
                        .themeImgUrl(theme.getThemeImgUrl())
                        .themeName(theme.getThemeName())
                        .companyId(theme.getCompany().getId())
                        .companyName(theme.getCompany().getCompanyName())
                        .genre(theme.getGenre())
                        .difficulty(theme.getDifficulty())
                        .minPeople(theme.getMinPeople())
                        .maxPeople(theme.getMaxPeople())
                        .playTime(theme.getPlayTime())
                        .price(theme.getPrice())
                        .themeUrl(theme.getThemeUrl())
                        .themeScore(theme.getThemeScore())
                        .synopsis((theme.getSynopsis()))
                        .totalLikeCnt(theme.getTotalLikeCnt())
                        .reviewCnt(reviewCnt)
                        .build();
        return themeDetailResponseDto;

    }

    //내가 찜한 테마 목록
    public List<MyThemeResponseDto> getMyThemes(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new IllegalArgumentException("사용자를 찾을 수 없습니다.")
        );

        List<ThemeLike> themeLikeList = themeLikeRepository.findThemeLikesByMember(member);
        List<MyThemeResponseDto> responseDtoList = new ArrayList<>();
        for(ThemeLike like: themeLikeList){

            Theme theme = themeRepository.findById(like.getTheme().getId())
                    .orElseThrow(
                            ()-> new IllegalArgumentException("테마를 찾을 수 없습니다.")
                    );

            Long reviewCnt = reviewRepository.countByThemeId(like.getTheme().getId());
            MyThemeResponseDto myThemeResponseDto =
                    MyThemeResponseDto.builder()
                            .id(theme.getId())
                            .companyName(theme.getCompany().getCompanyName())
                            .themeName(theme.getThemeName())
                            .themeLikeCnt((long) theme.getTotalLikeCnt())
                            .themeImgUrl(theme.getThemeImgUrl())
                            .themeScore(theme.getThemeScore())
                            .reviewCnt(reviewCnt)
                            .build();
            responseDtoList.add(myThemeResponseDto);
        }
        return responseDtoList;
    }

    //인기 테마 조회
    public List<ThemeResponseDto> findBestTheme(Pageable pageable) {

        Page<Theme> BestTheme = themeRepository.findAllByOrderByTotalLikeCntDesc(pageable);

        List<ThemeResponseDto> bestThemes = new ArrayList<>();
        for(Theme theme: BestTheme) {
            int reviewCnt = Math.toIntExact(reviewRepository.countByThemeId(theme.getId()));

            ThemeResponseDto themeResponseDto =
                    ThemeResponseDto.builder()
                            .id(theme.getId())
                            .themeImgUrl(theme.getThemeImgUrl())
                            .themeName(theme.getThemeName())
                            .companyName(theme.getCompany().getCompanyName())
                            .genre(theme.getGenre())
                            .themeScore(theme.getThemeScore())
                            .totalLikeCnt(theme.getTotalLikeCnt())
                            .reviewCnt(reviewCnt)
                            .build();
            bestThemes.add(themeResponseDto);
        }
        return bestThemes;
    }


    // 랜덤 테마 조회
    public List<ThemeResponseDto> findRandomTheme() {

        List<Theme> randomTheme = themeRepository.findAll();

        List<ThemeResponseDto> randomThemes = new ArrayList<>();
        for(Theme theme: randomTheme) {
            int reviewCnt = Math.toIntExact(reviewRepository.countByThemeId(theme.getId()));
            ThemeResponseDto themeResponseDto =
                    ThemeResponseDto.builder()
                            .id(theme.getId())
                            .themeImgUrl(theme.getThemeImgUrl())
                            .themeName(theme.getThemeName())
                            .companyName(theme.getCompany().getCompanyName())
                            .genre(theme.getGenre())
                            .themeScore(theme.getThemeScore())
                            .totalLikeCnt(theme.getTotalLikeCnt())
                            .reviewCnt(reviewCnt)
                            .build();
            randomThemes.add(themeResponseDto);
        }

        Collections.shuffle(randomThemes);
        List<ThemeResponseDto> randomThemeList = new ArrayList<>(randomThemes.subList(0,10));
        return randomThemeList;
    }


//    //랜덤 테마 조회
//    public List<ThemeResponseDto> findRandomTheme(Pageable pageable) {
//
//        int qty = themeRepository.findAll().size();
//        int idx = (int)(Math.random() * qty);
//
//        Page<Theme> randomTheme = themeRepository.findAll(PageRequest.of(idx,1));
//        List<ThemeResponseDto> randomThemes = randomTheme.stream()
//                .map(ThemeResponseDto::new).collect(Collectors.toList());
//        return randomThemes;
//    }

}
