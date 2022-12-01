package com.example.sherlockescape.dto.request;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberRequestDto {
//
//    비밀번호 소문자/특수문자 포함 8-16자
//    아이디 영문/숫자포함 4-12자
//    닉네임 한글/영어포함 2-10자
    private static final String PASSWORD_REGEX = "^(?=.*[~`!@#$%\\^&*()-])(?=.*[a-zA-Z]).{8,16}$";

    @NotBlank(message = "Username은 공백일 수 없습니다.")
    //@Pattern(regexp = "[a-zA-Z0-9]{4,12}", message = "닉네임양식을 확인해주세요!")
    @Size(min = 4, max = 12)//아이디 4~12 글자
    private String username;

    @NotBlank(message = "닉네임은 공백일 수 없습니다.")
    @Size(min = 2, max = 10)
    private String nickname;

    @Pattern(regexp = PASSWORD_REGEX, message = "패스워드는 무조건 영문, 특수문자를 포함해야 합니다.")
    @NotBlank(message = "Password는 공백일 수 없습니다.")
    private String password;

    @NotBlank
    private String passwordConfirm;

    public void setEncodePwd(String encodePwd) {
        this.password = encodePwd;
    }
}