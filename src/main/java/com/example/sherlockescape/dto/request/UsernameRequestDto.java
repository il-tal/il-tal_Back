package com.example.sherlockescape.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UsernameRequestDto {

    @NotBlank(message = "Username은 공백일 수 없습니다.")
    @Size(min = 4, max = 12)//아이디 4~12 글자
    private String username;
}
