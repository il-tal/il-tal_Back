package com.example.sherlockescape.utils;


import com.example.sherlockescape.domain.Member;
import com.example.sherlockescape.dto.request.MemberRequestDto;
import com.example.sherlockescape.exception.ErrorCode;
import com.example.sherlockescape.exception.GlobalException;
import com.example.sherlockescape.repository.MemberRepository;
import com.example.sherlockescape.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ValidateCheck {

    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public Member getMember(Long memberId){
        return memberRepository.findById(memberId)
                .orElseThrow(
                        () -> new GlobalException(ErrorCode.MEMBER_NOT_FOUND)
                );
    }

    public Member getMember(String username){
        return memberRepository.findByUsername(username)
                .orElseThrow(
                        () -> new GlobalException(ErrorCode.MEMBER_NOT_FOUND)
                );
    }

    public Member isPresentMember(String username){
        Optional<Member> optionalMember = memberRepository.findByUsername(username);
        return optionalMember.orElse(null);
    }

    //아이디 중복 체크
    public void usernameDuplicateCheck(MemberRequestDto memberReqDto) {
        if(memberRepository.findByUsername(memberReqDto.getUsername()).isPresent()){
            throw new GlobalException(ErrorCode.DUPLICATE_MEMBER_ID);
            // ex) return ResponseDto.fail()
        }
    }

    //닉네임 중복 체크
    public void userNicknameDuplicateCheck(MemberRequestDto memberRequestDto){
        if(memberRepository.findByNickname(memberRequestDto.getNickname()).isPresent()){
            throw new GlobalException(ErrorCode.DUPLICATE_MEMBER_NICKNAME);
        }
    }

}
