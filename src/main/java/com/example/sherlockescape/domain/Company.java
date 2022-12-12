package com.example.sherlockescape.domain;

import com.example.sherlockescape.dto.request.CompanyRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Builder
@Entity
@NoArgsConstructor
@Getter
@AllArgsConstructor
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "company_id")
    private Long id;

    @Column(nullable = false)
    private String companyName;

    @Column(nullable = false)
    private String companyImgUrl;

    @Column
    private double companyScore;

    @Column(nullable = false)
    private String companyUrl;
    
    @Column(nullable = false)
    private String location;

    @Column
    private String address;

    @Column
    private String phoneNumber;

    @Column
    private String workHour;

    //N + 1 문제 해결 EAGER -> LAZY
    @Builder.Default
    @OneToMany(mappedBy = "company", fetch = LAZY)
    private List<Theme> themeList = new ArrayList<>();
    public Company(Long companyId){
        this.id = companyId;
    }
    public void updateCompanyScore(double companyScore){
        this.companyScore = companyScore;
    }
    public void updateCompanyImgUrl(String imgUrl){ this.companyImgUrl = imgUrl; }
}