package com.example.sherlockescape.domain;

import com.example.sherlockescape.domain.base.BaseTimeEntity;
import com.example.sherlockescape.dto.request.KakaoUserInfoDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Builder
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//	@Column(name = "member_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(unique = true)
    private Long kakaoId;

    @Column(nullable = false)
    private String nickname;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @Column(/*nullable = false*/)
    private String encodedPassword;

    @Column(nullable = false)
    private String mainBadgeImg;

    @Column(nullable = false)
    private String mainBadgeName;



    public boolean validatePassword(PasswordEncoder passwordEncoder, String password) {
        return passwordEncoder.matches(password, this.password);
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateBadge(String badgeImg, String badgeName) {
        this.mainBadgeImg = badgeImg;
        this.mainBadgeName = badgeName;
    }

    public Member(Long kakaoId, String nickname, String encodedPassword) {
        this.kakaoId = kakaoId;
        this.nickname = nickname;
        this.encodedPassword = encodedPassword;

    }


    public static Member of(KakaoUserInfoDto kakaoUserInfoDto) {

        return Member.builder()
                .nickname(kakaoUserInfoDto.getNickname())
                .kakaoId(kakaoUserInfoDto.getKakaoId())
                .build();
    }


}
