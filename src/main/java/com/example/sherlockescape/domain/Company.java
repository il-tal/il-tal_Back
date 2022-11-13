package com.example.sherlockescape.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.EAGER;

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

    @OneToMany(mappedBy = "company", fetch = EAGER)
    private List<Theme> themeList = new ArrayList<>();
    public Company(Long companyId){
        this.id = companyId;
    }
}