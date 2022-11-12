package com.example.sherlockescape.service;

import com.example.sherlockescape.domain.Company;
import com.example.sherlockescape.domain.CompanyLike;
import com.example.sherlockescape.domain.Member;
import com.example.sherlockescape.dto.ResponseDto;
import com.example.sherlockescape.dto.request.CompanyLikeRequestDto;
import com.example.sherlockescape.dto.response.CompanyLikeResponseDto;
import com.example.sherlockescape.repository.CompanyLikeRepository;
import com.example.sherlockescape.utils.ValidateCheck;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompanyLikeService {

    private final CompanyLikeRepository companyLikeRepository;
    private final ValidateCheck validateCheck;

    public ResponseDto<CompanyLikeResponseDto> companyLikeUp(CompanyLikeRequestDto companyLikeRequestDto, Long memberId) {
        Optional<CompanyLike> likes = companyLikeRepository
                .findByCompanyIdAndMemberId(Long.parseLong(companyLikeRequestDto.getCompanyId()), memberId);

        Member member = validateCheck.getMember(memberId);

        Company company = new Company(Long.parseLong(companyLikeRequestDto.getCompanyId()));
        boolean companyLikeCheck;
        if(likes.isPresent()){
            companyLikeCheck = false;
            companyLikeRepository.delete(likes.get());
        }else{
            companyLikeCheck = true;
            CompanyLike like = new CompanyLike(member, company);
            companyLikeRepository.save(like);
        }
        Long companyLikeCnt = companyLikeRepository
                .countByCompanyId(Long.parseLong(companyLikeRequestDto.getCompanyId()));
        return ResponseDto.success(
                CompanyLikeResponseDto.builder()
                        .companyLikeCheck(companyLikeCheck)
                        .companyLikeCnt(companyLikeCnt)
                        .build()
        );
    }
}
