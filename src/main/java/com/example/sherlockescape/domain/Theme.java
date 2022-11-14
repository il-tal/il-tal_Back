package com.example.sherlockescape.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.List;

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

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @Column(nullable = false)
    private String themeImgUrl;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String themeName;

    @Column(nullable = false)
    private double difficulty;

    @Column
    private String genre;

//    @Column
//    private String genreFilter;


    @Column(nullable = false)
    private int playTime;

    @Lob
    private String synopsis;

    @Column
    private Double themeScore;

    @Column(nullable = false)
    private String themeUrl;

    @ElementCollection
    private List<Integer> people;

//    @Column(nullable = false)
//    private int maxPeople;

    @Column(nullable = false)
    private int price;
    public Theme(Long themeId){
        this.id = themeId;
    }

}