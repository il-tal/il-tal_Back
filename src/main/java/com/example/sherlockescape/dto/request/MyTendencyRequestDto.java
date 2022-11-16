package com.example.sherlockescape.dto.request;


import com.example.sherlockescape.domain.GenrePreference;

import com.example.sherlockescape.domain.StylePreference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MyTendencyRequestDto {
    @NotNull
    private List<GenrePreference> genrePreference;
    @NotNull
    private List<StylePreference> stylePreference;

    private int lessScare;
    private int roomSize;
    private int lockStyle;
    private int device;
    private int interior;
    private int excitePreference;
    private int surprise;

}
