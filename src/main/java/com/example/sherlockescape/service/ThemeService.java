package com.example.sherlockescape.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.example.sherlockescape.domain.Company;
import com.example.sherlockescape.domain.Theme;
import com.example.sherlockescape.dto.ResponseDto;
import com.example.sherlockescape.dto.request.ThemeRequestDto;
import com.example.sherlockescape.dto.response.ThemeDetailResponseDto;
import com.example.sherlockescape.dto.response.ThemeResponseDto;
import com.example.sherlockescape.repository.CompanyRepository;
import com.example.sherlockescape.repository.ThemeLikeRepository;
import com.example.sherlockescape.repository.ThemeRepository;
import com.example.sherlockescape.utils.CommonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.example.sherlockescape.domain.QCompany.company;
import static com.example.sherlockescape.domain.QTheme.theme;

@Service
@RequiredArgsConstructor
public class ThemeService {

    private final ThemeRepository themeRepository;
    private final CompanyRepository companyRepository;
    private final AmazonS3Client amazonS3Client;

    private final ThemeLikeRepository themeLikeRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;


    /*
     *
     * 테마 DB 등록
     * */
    public ResponseDto createTheme(Long companyId, MultipartFile multipartFile, ThemeRequestDto themeReqDto) {
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

    //테마 전체 조회
    public List<ThemeResponseDto> findAllTheme(Pageable pageable) {

        Page<Theme> allTheme = themeRepository.findAll(pageable);
        List<ThemeResponseDto> themeLists = allTheme.stream()
                .map(ThemeResponseDto::new).collect(Collectors.toList());
        return themeLists;
    }

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
}
