package com.example.sherlockescape.service;

import com.amazonaws.services.s3.AmazonS3Client;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
    public ResponseDto<String> createTheme(Long companyId, MultipartFile multipartFile, ThemeRequestDto themeReqDto) {
        Company company = companyRepository.findById(companyId).orElseThrow(
                () -> new IllegalArgumentException("해당 업체가 존재하지 않습니다.")
        );

        String fileName = CommonUtils.buildFileName(Objects.requireNonNull(multipartFile.getOriginalFilename()));
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
        List<ThemeResponseDto> themeLists = filteredTheme.stream()
                .map(ThemeResponseDto::new).collect(Collectors.toList());

        return themeLists;
    }


    //테마 상세조회
    public ResponseDto<ThemeDetailResponseDto> findTheme(Long themeId) {

        Theme theme = themeRepository.findById(themeId).orElseThrow(
                () -> new IllegalArgumentException("테마가 존재하지 않습니다"));

        ThemeDetailResponseDto themeDetail = new ThemeDetailResponseDto(theme);
        return ResponseDto.success(themeDetail);

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

            Long themeLikeCnt = themeLikeRepository.countByThemeId(like.getTheme().getId());
            Long reviewCnt = reviewRepository.countByThemeId(like.getTheme().getId());
            MyThemeResponseDto myThemeResponseDto =
                    MyThemeResponseDto.builder()
                            .id(theme.getId())
                            .companyName(theme.getCompany().getCompanyName())
                            .themeName(theme.getThemeName())
                            .themeLikeCnt(themeLikeCnt)
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
        List<ThemeResponseDto> bestThemes = BestTheme.stream()
                .map(ThemeResponseDto::new).collect(Collectors.toList());
        return bestThemes;
    }


    // 랜덤 테마 조회
    public List<ThemeResponseDto> findRandomTheme() {

        List<Theme> randomTheme = themeRepository.findAll();
        List<ThemeResponseDto> randomThemes = randomTheme.stream()
                .map(ThemeResponseDto::new).collect(Collectors.toList());
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
