package com.example.sherlockescape.utils;


import com.example.sherlockescape.domain.Member;
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
                        () -> new IllegalArgumentException("사용자를 찾을 수 없습니다.")
                );
    }

    public Member isPresentMember(String username){
        Optional<Member> optionalMember = memberRepository.findByUsername(username);
        return optionalMember.orElse(null);
    }
}
