package com.example.sherlockescape.service;

import com.example.sherlockescape.domain.*;
import com.example.sherlockescape.dto.ResponseDto;
import com.example.sherlockescape.dto.request.LoginRequestDto;
import com.example.sherlockescape.dto.request.MemberRequestDto;
import com.example.sherlockescape.dto.request.MyTendencyRequestDto;
import com.example.sherlockescape.dto.request.NicknameRequestDto;
import com.example.sherlockescape.dto.response.AllMyInfoResponseDto;
import com.example.sherlockescape.dto.response.LoginResponseDto;
import com.example.sherlockescape.dto.response.MemberResponseDto;

import com.example.sherlockescape.dto.response.NicknameResponseDto;
import com.example.sherlockescape.exception.ErrorCode;
import com.example.sherlockescape.exception.GlobalException;
import com.example.sherlockescape.repository.*;
import com.example.sherlockescape.security.jwt.JwtUtil;
import com.example.sherlockescape.security.jwt.TokenDto;
import com.example.sherlockescape.utils.ValidateCheck;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final ValidateCheck validateCheck;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final GenrePreferenceRepository genrePreferenceRepository;
    private final TendencyRepository tendencyRepository;
    private final StylePreferenceRepository stylePreferenceRepository;
    private final ReviewRepository reviewRepository;

    //회원가입
    @org.springframework.transaction.annotation.Transactional
    public ResponseEntity<ResponseDto<MemberResponseDto>> signup(MemberRequestDto memberReqDto) {

        // username 중복 검사
        usernameDuplicateCheck(memberReqDto);

        // 비빌번호 확인 & 비빌번호 불일치
        if(!memberReqDto.getPassword().equals(memberReqDto.getPasswordConfirm())){
            throw new GlobalException(ErrorCode.BAD_PASSWORD_CONFIRM);
        }

        Member member = Member.builder()
                .username(memberReqDto.getUsername())
                .nickname(memberReqDto.getNickname())
                .password(passwordEncoder.encode(memberReqDto.getPassword()))
                .build();
        memberRepository.save(member);
        return ResponseEntity.ok().body(ResponseDto.success(
                MemberResponseDto.builder()
                        .username(member.getUsername())
                        .nickname(member.getNickname())
                        .createdAt(member.getCreatedAt())
                        .modifiedAt(member.getModifiedAt())
                        .build()
        ));

    }
    public void usernameDuplicateCheck(MemberRequestDto memberReqDto) {
        if(memberRepository.findByUsername(memberReqDto.getUsername()).isPresent()){
            throw new GlobalException(ErrorCode.DUPLICATE_MEMBER_ID);
            // ex) return ResponseDto.fail()
        }
    }

    //로그인
    @Transactional
    public ResponseEntity<ResponseDto<LoginResponseDto>> login(LoginRequestDto loginReqDto, HttpServletResponse response) {

        Member member = validateCheck.isPresentMember(loginReqDto.getUsername());

        //사용자가 있는지 확인
        if(null == member){
            throw new GlobalException(ErrorCode.MEMBER_NOT_FOUND);
        }
        //비밀번호가 맞는지 확인
        if(!member.validatePassword(passwordEncoder, loginReqDto.getPassword())){
            throw new GlobalException(ErrorCode.BAD_PASSWORD);
        }

        TokenDto tokenDto = jwtUtil.createAllToken(loginReqDto.getUsername());

        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByMemberUsername(loginReqDto.getUsername());

        if(refreshToken.isPresent()) {
            refreshTokenRepository.save(refreshToken.get().updateToken(tokenDto.getRefreshToken()));
        }else {
            RefreshToken newToken = new RefreshToken(tokenDto.getRefreshToken(), loginReqDto.getUsername());
            refreshTokenRepository.save(newToken);
        }
        setHeader(response, tokenDto);

        return ResponseEntity.ok().body(ResponseDto.success(
                LoginResponseDto.builder()
                        .username(member.getUsername())
                        .nickname(member.getNickname())
                        .createdAt(member.getCreatedAt())
                        .modifiedAt(member.getModifiedAt())
                        .build()
        ));
    }
    private void setHeader(HttpServletResponse response, TokenDto tokenDto) {
        response.addHeader(JwtUtil.ACCESS_TOKEN, tokenDto.getAccessToken());
        response.addHeader(JwtUtil.REFRESH_TOKEN, tokenDto.getRefreshToken());

    }

    //로그아웃
    public String logout(Member member) {
        refreshTokenRepository.deleteByMemberUsername(member.getUsername());
        return "로그아웃 완료";
    }

    //닉네임 수정하기
    @Transactional
    public ResponseDto<NicknameResponseDto> updateNickname(Long memberId,NicknameRequestDto nicknameRequestDto) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                ()-> new IllegalArgumentException("사용자를 찾을 수 없습니다.")
        );
        member.updateNickname(nicknameRequestDto.getNickname());
        memberRepository.save(member);
        NicknameResponseDto nicknameResponseDto = NicknameResponseDto.builder()
                .username(member.getUsername())
                .nickname(member.getNickname())
                .build();
        return ResponseDto.success(nicknameResponseDto);
    }

    /*
    *
    * 내 성향 등록하기
    * */
    @Transactional
    public String createMyTendency(Long memberId, MyTendencyRequestDto myTendencyRequestDto) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new GlobalException(ErrorCode.NEED_TO_LOGIN)
        );
        Tendency tendency = Tendency.builder()
                .member(member).device(myTendencyRequestDto.getDevice())
                .excitePreference(myTendencyRequestDto.getExcitePreference())
                .lessScare(myTendencyRequestDto.getLessScare())
                .interior(myTendencyRequestDto.getInterior())
                .lockStyle(myTendencyRequestDto.getLockStyle())
                .roomSize(myTendencyRequestDto.getRoomSize())
                .surprise(myTendencyRequestDto.getSurprise())
                .build();
        tendencyRepository.save(tendency);
            for(GenrePreference preference: myTendencyRequestDto.getGenrePreference()){
                GenrePreference genrePreference = GenrePreference.builder()
                        .genrePreference(preference.getGenrePreference())
                        .tendency(tendency)
                        .build();
                genrePreferenceRepository.save(genrePreference);
            }
            for(StylePreference preference: myTendencyRequestDto.getStylePreference()){
                StylePreference stylePreference = StylePreference.builder()
                        .stylePreference(preference.getStylePreference())
                        .tendency(tendency)
                        .build();
                stylePreferenceRepository.save(stylePreference);
            }
        return "성향 등록 성공";
    }

    /*
    *
    * 내 성향 수정
    * */
    @Transactional
    public String updateMyTendency(Long memberId, MyTendencyRequestDto myTendencyRequestDto) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new GlobalException(ErrorCode.NEED_TO_LOGIN)
        );
        Tendency tendency = tendencyRepository.findByMember(member);
        tendency.updateTendency(myTendencyRequestDto.getLessScare(), myTendencyRequestDto.getRoomSize(),
                                myTendencyRequestDto.getLockStyle(), myTendencyRequestDto.getDevice(),
                                myTendencyRequestDto.getInterior(), myTendencyRequestDto.getExcitePreference(),
                                myTendencyRequestDto.getSurprise());
        tendencyRepository.save(tendency);
        List<GenrePreference> genrePreferenceList = genrePreferenceRepository.findAllByTendencyId(tendency.getId());
        for(GenrePreference genrePreference: genrePreferenceList){
            genrePreference.updateGenrePreference(genrePreference.getGenrePreference());
            genrePreferenceRepository.save(genrePreference);
        }
        List<StylePreference> stylePreferenceList = stylePreferenceRepository.findAllByTendencyId(tendency.getId());
        for(StylePreference stylePreference: stylePreferenceList){
            stylePreference.updateStylePreference(stylePreference.getStylePreference());
            stylePreferenceRepository.save(stylePreference);
        }
        return "성향 수정 성공";
    }

    /*
    *
    * 내정보 전체 불러오기
    * */
    public ResponseDto<AllMyInfoResponseDto> getAllMyInfo(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new GlobalException(ErrorCode.NEED_TO_LOGIN)
        );
        Tendency tendency = tendencyRepository.findByMember(member);
        List<GenrePreference> genrePreferenceList = genrePreferenceRepository.findAllByTendencyId(tendency.getId());
        List<StylePreference> stylePreferenceList = stylePreferenceRepository.findAllByTendencyId(tendency.getId());

        List<Review> reviews = reviewRepository.findReviewsByMember(member);
        int totalAchieveCnt = 0;
        for(Review review: reviews){
            if(review.isSuccess()){
                totalAchieveCnt += 1;
            }
        }
        return ResponseDto.success(new AllMyInfoResponseDto(member, tendency, totalAchieveCnt,genrePreferenceList, stylePreferenceList));
    }
}
