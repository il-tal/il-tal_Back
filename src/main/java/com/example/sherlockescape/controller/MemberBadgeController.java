package com.example.sherlockescape.controller;


import com.example.sherlockescape.service.MemberBadgeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberBadgeController {

    private final MemberBadgeService memberBadgeService;


}
