package com.example.sherlockescape.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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
    private String imgUrl;

    @Column(nullable = false)
    private String location;

    @Column
    private double companyScore;

    @Column(nullable = false)
    private String companyUrl;

    public Company(Long companyId){
        this.id = companyId;
    }
}