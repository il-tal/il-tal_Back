package com.example.sherlockescape.service;

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
import com.example.sherlockescape.domain.Member;
import com.example.sherlockescape.domain.RefreshToken;
import com.example.sherlockescape.domain.Review;
import com.example.sherlockescape.domain.Tendency;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
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
    private final ReviewRepository reviewRepository;
    //    private final StylePreferenceRepository stylePreferenceRepository;
    //    private final GenrePreferenceRepository genrePreferenceRepository;
    private final TendencyRepository tendencyRepository;
    private final MemberBadgeRepository memberBadgeRepository;
    private BadgeRepository badgeRepository;

    //회원가입
    @org.springframework.transaction.annotation.Transactional
    public ResponseEntity<ResponseDto<MemberResponseDto>> signup(MemberRequestDto memberReqDto) {

        // username 중복 검사
        validateCheck.usernameDuplicateCheck(memberReqDto);
        // nickname 중복 검사
        validateCheck.userNicknameDuplicateCheck(memberReqDto);
        // 비빌번호 확인 & 비빌번호 불일치
        if(!memberReqDto.getPassword().equals(memberReqDto.getPasswordConfirm())){
            throw new GlobalException(ErrorCode.BAD_PASSWORD_CONFIRM);
        }
        Member member = Member.builder()
                .username(memberReqDto.getUsername())
                .nickname(memberReqDto.getNickname())
                .mainBadgeImg("https://mykeejaebucket.s3.ap-northeast-2.amazonaws.com/Serverbasigbadge.1668884794370.png")
                .mainBadgeName("뱃지를 획득해 보세요!")
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
        //성향 등록 로직
        // Tendency.findByMember
        // Tendency.ispresent() ->
        // else 성향 등록
        Optional<Tendency> tendency = Optional.ofNullable(tendencyRepository.findByMember(member));
        if(tendency.isPresent()){
            throw new IllegalArgumentException("성향을 이미 등록하셨습니다.");
        }
        Tendency createTendency = Tendency.builder()
                .member(member).device(myTendencyRequestDto.getDevice())
                .genrePreference(myTendencyRequestDto.getGenrePreference())
                .stylePreference(myTendencyRequestDto.getStylePreference())
                .excitePreference(myTendencyRequestDto.getExcitePreference())
                .lessScare(myTendencyRequestDto.getLessScare())
                .interior(myTendencyRequestDto.getInterior())
                .lockStyle(myTendencyRequestDto.getLockStyle())
                .roomSize(myTendencyRequestDto.getRoomSize())
                .surprise(myTendencyRequestDto.getSurprise())
                .build();
        tendencyRepository.save(createTendency);
//            for(GenrePreference preference: myTendencyRequestDto.getGenrePreference()){
//                GenrePreference genrePreference = GenrePreference.builder()
//                        .genrePreference(preference.getGenrePreference())
//                        .tendency(tendency)
//                        .build();
//                genrePreferenceRepository.save(genrePreference);
//            }
//            for(StylePreference preference: myTendencyRequestDto.getStylePreference()){
//                StylePreference stylePreference = StylePreference.builder()
//                        .stylePreference(preference.getStylePreference())
//                        .tendency(tendency)
//                        .build();
//                stylePreferenceRepository.save(stylePreference);
//            }
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
        Optional<Tendency> tendency = Optional.ofNullable(tendencyRepository.findByMember(member));
        if(tendency.isEmpty()) {
            throw new IllegalArgumentException("성향을 등록해주세요");
        }else{
            Tendency createdTendency = tendencyRepository.findByMember(member);
            createdTendency.updateTendency(myTendencyRequestDto.getGenrePreference(), myTendencyRequestDto.getStylePreference(),
                    myTendencyRequestDto.getLessScare(), myTendencyRequestDto.getRoomSize(),
                    myTendencyRequestDto.getLockStyle(), myTendencyRequestDto.getDevice(),
                    myTendencyRequestDto.getInterior(), myTendencyRequestDto.getExcitePreference(),
                    myTendencyRequestDto.getSurprise());
            tendencyRepository.save(createdTendency);
        }

//        List<GenrePreference> genrePreferenceList = genrePreferenceRepository.findAllByTendencyId(tendency.getId());
//        for(GenrePreference preference: myTendencyRequestDto.getGenrePreference()){
//            GenrePreference genrePreference = genrePreferenceRepository.findByTendencyId(tendency.getId());
//            genrePreference.updateGenrePreference(preference.getGenrePreference());
//            genrePreferenceRepository.save(genrePreference);
//        }
//        List<StylePreference> stylePreferenceList = stylePreferenceRepository.findAllByTendencyId(tendency.getId());
//        for(StylePreference preference: myTendencyRequestDto.getStylePreference()){
//            StylePreference stylePreference = stylePreferenceRepository.findByTendencyId(tendency.getId());
//            stylePreference.updateStylePreference(preference.getStylePreference());
//            stylePreferenceRepository.save(stylePreference);
//        }
        return "성향 수정 성공";
    }

    /*
    *
    * 내정보 전체 불러오기
    * */
    public ResponseDto<AllMyInfoResponseDto> getAllMyInfo(Long memberId) {
        Member member = memberRepository.findById(memberId).
                orElse(null);
        Optional<Tendency> tendency = Optional.ofNullable(tendencyRepository.findByMember(member));
//        Optional<Review> findReview = reviewRepository.findByMember(member);
        assert member != null;
        if(tendency.isEmpty() /*&& badge.isEmpty() && findReview.isEmpty()*/){
            AllMyInfoResponseDto allMyInfoResponseDto
                    = AllMyInfoResponseDto.builder()
                    .id(memberId).nickname(member.getNickname())
                    .mainBadgeName(member.getMainBadgeName()).mainBadgeImg(member.getMainBadgeImg())
                    .device(0).excitePreference(0).device(0)
                    .lockStyle(0).lessScare(0).roomSize(0)
                    .surprise(0).interior(0).genrePreference(null)
                    .stylePreference(null).totalAchieveCnt(0)
                    .build();
            return ResponseDto.success(allMyInfoResponseDto);
        }else{
            Tendency findTendency = tendencyRepository.findByMember(member);
            List<Review> reviews = reviewRepository.findReviewsByMember(member);
            int totalAchieveCnt = 0;
            int totalFailCnt = 0;
            for(Review review: reviews){
                if(review.isSuccess()){
                    totalAchieveCnt += 1;
                }else{
                    totalFailCnt += 1;
                }
            }
            return ResponseDto.success(new AllMyInfoResponseDto(member, findTendency, totalAchieveCnt, totalFailCnt));
        }
    }
}
