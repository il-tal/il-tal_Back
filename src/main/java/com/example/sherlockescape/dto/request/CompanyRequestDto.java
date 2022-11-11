package com.example.sherlockescape.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyRequestDto {

    @NotBlank
    private String companyName;

    @NotBlank
    private String location;

    private double companyScore;

    @NotBlank
    private String companyUrl;
}