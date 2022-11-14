package com.example.sherlockescape.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Theme {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "theme_id")
    public Long id;

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @OneToOne(mappedBy = "theme")
    private Achievement achievement;

    @Column(nullable = false)
    private String imgUrl;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String themeName;

    @Column(nullable = false)
    private double difficulty;

    @Column
    private String genre;

    @Column(nullable = false)
    private int playTime;

    @Lob
    private String synopsis;

    @Column
    private double themeScore;

    @Column(nullable = false)
    private String themeUrl;

    @Column(nullable = false)
    private int maxPeople;

    @Column(nullable = false)
    private int minPeople;

    @Column(nullable = false)
    private int price;
    public Theme(Long themeId){
        this.id = themeId;
    }

}