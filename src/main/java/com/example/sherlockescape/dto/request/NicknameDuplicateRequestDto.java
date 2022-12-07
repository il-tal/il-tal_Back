package com.example.sherlockescape.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NicknameDuplicateRequestDto {

    @NotBlank(message = "닉네임은 공백일 수 없습니다.")
    @Size(min = 2, max = 10)
    private String nickname;
}
