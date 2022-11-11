package com.example.sherlockescape.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ThemeLikeRequestDto {

    @NotBlank
    private String themeId;
}
