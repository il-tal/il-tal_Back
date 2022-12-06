package com.example.sherlockescape.security.user;

import com.example.sherlockescape.domain.Member;
import com.example.sherlockescape.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final MemberRepository memberRepository;

    //시큐리티 session ⊃ Authentication ⊃ userDetails
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("아이디를 찾을 수 없습니다." + username));
        return createUserDetails(member);
    }

    private UserDetails createUserDetails(Member member){
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("member");

            return new User(
                    member.getUsername(),
                    member.getPassword(),
                    Collections.singleton(grantedAuthority)
            );
    }
}