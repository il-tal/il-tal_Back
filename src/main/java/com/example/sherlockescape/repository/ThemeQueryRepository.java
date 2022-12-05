package com.example.sherlockescape.repository;

import com.example.sherlockescape.domain.Theme;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ThemeQueryRepository {
    Page<Theme> findFilter(Pageable pageable, List<String> location, List<String> genreFilter , List<Integer> themeScore, List<Integer> difficulty, List<Integer> people) ;

    Page<Theme> findByThemeName(Pageable pageable, String themeName);
}
