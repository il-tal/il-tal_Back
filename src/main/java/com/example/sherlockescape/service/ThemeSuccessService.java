package com.example.sherlockescape.service;


import com.example.sherlockescape.repository.ThemeRepository;
import com.example.sherlockescape.repository.ThemeSuccessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ThemeSuccessService {

    private final ThemeSuccessRepository themeSuccessRepository;


}
