package com.example.sherlockescape.domain;

import com.example.sherlockescape.domain.base.BaseTimeEntity;
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

    @Column(nullable = false)
    private String nickname;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String kakaoEmail;

    @Column(nullable = false)
    private String profile;

//    @Column(nullable = false)
//    private PasswordEncoder passwordEncoder;




    public boolean validatePassword(PasswordEncoder passwordEncoder, String password) {
        return passwordEncoder.matches(password, this.password);
    }

    public Member(String kakaoEmail, String nickname, String profile, String encodedPassword) {
        this.kakaoEmail = kakaoEmail;
        this.nickname = nickname;
        this.profile = profile;
//        this.passwordEncoder = encodedPassword;
    }
//    kakaoEmail, nickname, profile, encodedPassword
}
