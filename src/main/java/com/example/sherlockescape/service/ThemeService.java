package com.example.sherlockescape.service;

import com.example.sherlockescape.domain.Company;
import com.example.sherlockescape.domain.Member;
import com.example.sherlockescape.domain.Theme;
import com.example.sherlockescape.domain.ThemeLike;
import com.example.sherlockescape.dto.ResponseDto;
import com.example.sherlockescape.dto.request.CompanyRequestDto;
import com.example.sherlockescape.dto.request.ThemeRequestDto;
import com.example.sherlockescape.dto.response.MyThemeResponseDto;
import com.example.sherlockescape.dto.response.ThemeDetailResponseDto;
import com.example.sherlockescape.dto.response.ThemeResponseDto;
import com.example.sherlockescape.repository.CompanyRepository;
import com.example.sherlockescape.repository.ThemeLikeRepository;
import com.example.sherlockescape.repository.ThemeRepository;
import com.example.sherlockescape.exception.ErrorCode;
import com.example.sherlockescape.exception.GlobalException;
import com.example.sherlockescape.utils.CommonUtils;
import com.example.sherlockescape.utils.ValidateCheck;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)//읽기 전용 쿼리의 성능 최적화
public class ThemeService {

    private final ThemeRepository themeRepository;
    private final CompanyRepository companyRepository;
    private final ValidateCheck validateCheck;
    private final ThemeLikeRepository themeLikeRepository;
    private final CommonUtils commonUtils;
    /*
     *
     * 테마 DB 등록
     * */
    @Transactional
    public ResponseDto<Long> createTheme(Long companyId, MultipartFile multipartFile, ThemeRequestDto themeReqDto) throws IOException {
        //업체 조회
        Company company = companyRepository.findById(companyId).orElseThrow(
                () -> new IllegalArgumentException("해당 업체가 존재하지 않습니다.")
        );
        //S3 이미지 올리기 기능
        String imgUrl = commonUtils.createAll(multipartFile.getOriginalFilename(),
                                              multipartFile.getContentType(),
                                              multipartFile.getInputStream());
        Theme theme = Theme.builder()
                .company(company)
                .themeName(themeReqDto.getThemeName())
                .themeImgUrl(imgUrl)
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
        return ResponseDto.success(theme.getId());
    }

    /*
    *
    *
    * 테마 DB 수정
    * */
    @Transactional
    public Long updateTheme(Long themeId, MultipartFile multipartFile) throws IOException {

        Theme theme = themeRepository.findById(themeId).orElseThrow(
                () -> new GlobalException(ErrorCode.THEME_NOT_FOUND)
        );

        String imgUrl = commonUtils.createAll(multipartFile.getOriginalFilename(),
                                              multipartFile.getContentType(),
                                              multipartFile.getInputStream());
        theme.updateThemeImgUrl(imgUrl);

        return theme.getId();
    }


    //테마 필터링
    public Page<ThemeResponseDto> filter(Pageable pageable, List<String> location, List<String> genreFilter, List<Integer> themeScore, List<Integer> difficulty, List<Integer> people, String username){

        Page<Theme> filteredTheme = themeRepository.findFilter(pageable, location, genreFilter, themeScore, difficulty, people);


        List<ThemeResponseDto> themeLists = new ArrayList<>();
        for(Theme theme: filteredTheme) {
            Optional<ThemeLike> themeLike = themeLikeRepository.findByThemeIdAndMemberUsername(theme.getId(), username);

            //테마 좋아요 확인
            boolean themeLikeCheck = themeLike.isPresent();

            ThemeResponseDto themeResponseDto =
                    ThemeResponseDto.builder()
                            .id(theme.getId())
                            .themeImgUrl(theme.getThemeImgUrl())
                            .themeName(theme.getThemeName())
                            .companyName(theme.getCompany().getCompanyName())
                            .genre(theme.getGenre())
                            .themeScore(theme.getThemeScore())
                            .themeLikeCheck(themeLikeCheck)
                            .totalLikeCnt(theme.getTotalLikeCnt())
                            .reviewCnt(theme.getReviewCnt())
                            .build();
            themeLists.add(themeResponseDto);
        }
        Page<ThemeResponseDto> fiteredThemes = new PageImpl<>(themeLists, pageable, filteredTheme.getTotalElements());
        return fiteredThemes;
    }

    //테마 필터 결과 개수 반환
    public Long countFilteredTheme(Pageable pageable, List<String> location, List<String> genreFilter, List<Integer> themeScore, List<Integer> difficulty, List<Integer> people) {
        return themeRepository.findFilter(pageable, location, genreFilter, themeScore, difficulty, people).getTotalElements();
    }

    //테마 이름 검색
    public Page<ThemeResponseDto> searchTheme(Pageable pageable, String themeName, String username){

        Page<Theme> filteredTheme = themeRepository.findByThemeName(pageable, themeName);

        List<ThemeResponseDto> themeLists = new ArrayList<>();
        for(Theme theme: filteredTheme) {
            Optional<ThemeLike> themeLike = themeLikeRepository.findByThemeIdAndMemberUsername(theme.getId(), username);

            //테마 좋아요 확인
            boolean themeLikeCheck = themeLike.isPresent();

            ThemeResponseDto themeResponseDto =
                    ThemeResponseDto.builder()
                            .id(theme.getId())
                            .themeImgUrl(theme.getThemeImgUrl())
                            .themeName(theme.getThemeName())
                            .companyName(theme.getCompany().getCompanyName())
                            .genre(theme.getGenre())
                            .themeScore(theme.getThemeScore())
                            .themeLikeCheck(themeLikeCheck)
                            .totalLikeCnt(theme.getTotalLikeCnt())
                            .reviewCnt(theme.getReviewCnt())
                            .build();
            themeLists.add(themeResponseDto);
        }
        Page<ThemeResponseDto> searchedThemes = new PageImpl<>(themeLists, pageable, filteredTheme.getTotalElements());
        return searchedThemes;
    }


    //테마 상세조회
    public ThemeDetailResponseDto findTheme(Long themeId, String username) {

        Theme theme = themeRepository.findById(themeId).orElseThrow(
                () -> new GlobalException(ErrorCode.THEME_NOT_FOUND)
        );
        Optional<ThemeLike> themeLike = themeLikeRepository.findByThemeIdAndMemberUsername(themeId, username);

        boolean themeLikeCheck = themeLike.isPresent();
        return ThemeDetailResponseDto.builder()
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
                .themeLikeCheck(themeLikeCheck)
                .reviewCnt(theme.getReviewCnt())
                .build();

    }

    //내가 찜한 테마 목록
    public List<MyThemeResponseDto> getMyThemes(String username) {
        Member member = validateCheck.getMember(username);

        List<ThemeLike> themeLikeList = themeLikeRepository.findThemeLikesByMember(member);
        List<MyThemeResponseDto> responseDtoList = new ArrayList<>();
        for(ThemeLike like: themeLikeList){

            Theme theme = themeRepository.findById(like.getTheme().getId())
                    .orElseThrow(
                            ()-> new GlobalException(ErrorCode.THEME_NOT_FOUND)
                    );

            MyThemeResponseDto myThemeResponseDto =
                    MyThemeResponseDto.builder()
                            .id(theme.getId())
                            .companyName(theme.getCompany().getCompanyName())
                            .themeName(theme.getThemeName())
                            .themeLikeCnt((long) theme.getTotalLikeCnt())
                            .themeImgUrl(theme.getThemeImgUrl())
                            .themeScore(theme.getThemeScore())
                            .reviewCnt(theme.getReviewCnt())
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

            ThemeResponseDto themeResponseDto =
                    ThemeResponseDto.builder()
                            .id(theme.getId())
                            .themeImgUrl(theme.getThemeImgUrl())
                            .themeName(theme.getThemeName())
                            .companyName(theme.getCompany().getCompanyName())
                            .genre(theme.getGenre())
                            .themeScore(theme.getThemeScore())
                            .totalLikeCnt(theme.getTotalLikeCnt())
                            .reviewCnt(theme.getReviewCnt())
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
            ThemeResponseDto themeResponseDto =
                    ThemeResponseDto.builder()
                            .id(theme.getId())
                            .themeImgUrl(theme.getThemeImgUrl())
                            .themeName(theme.getThemeName())
                            .companyName(theme.getCompany().getCompanyName())
                            .genre(theme.getGenre())
                            .themeScore(theme.getThemeScore())
                            .totalLikeCnt(theme.getTotalLikeCnt())
                            .reviewCnt(theme.getReviewCnt())
                            .build();
            randomThemes.add(themeResponseDto);
        }

        Collections.shuffle(randomThemes);
        return new ArrayList<>(randomThemes.subList(0,10));
    }

}
