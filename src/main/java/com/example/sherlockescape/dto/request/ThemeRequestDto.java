package com.example.sherlockescape.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Lob;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ThemeRequestDto {

    @NotBlank
    private String themeName;

    @NotBlank
    private String location;

    @NotBlank
    private Double difficulty;

    @NotBlank
    private String genre;

    @NotBlank
    private int playTime;

    @NotBlank
    private String synopsis;

    @NotBlank
    private String themeUrl;

    @NotBlank
    private Double themeScore;

    private List<Integer> people;

    @NotBlank
    private int price;

}