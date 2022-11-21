package com.example.sherlockescape.dto.request;

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
//    @NotNull
//    private List<GenrePreference> genrePreference;
//    @NotNull
//    private List<StylePreference> stylePreference;
    private String genrePreference;
    private String stylePreference;
    private int lessScare;
    private int roomSize;
    private int lockStyle;
    private int device;
    private int interior;
    private int excitePreference;
    private int surprise;

}
