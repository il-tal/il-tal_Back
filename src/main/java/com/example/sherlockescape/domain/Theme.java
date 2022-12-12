package com.example.sherlockescape.domain;

import com.example.sherlockescape.dto.request.ThemeRequestDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

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

    @Column(nullable = false)
    private String themeImgUrl;

    @Column(nullable = false)
    private String themeName;

    @Column(nullable = false)
    private double difficulty;

    @Column
    private String genre;

    @Column
    private String genreFilter;

    @Column(nullable = false)
    private int playTime;

    @Lob
    private String synopsis;
    @Column
    private double themeScore;

    @Column(nullable = false)
    private String themeUrl;

    @Column(nullable = false)
    private int minPeople;

    @Column(nullable = false)
    private int maxPeople;

    @Column(nullable = false)
    private int price;
    @Column
    private Boolean themeLikeCheck;
    @Column
    private int totalLikeCnt;
    @Column
    private Long reviewCnt;

    public Theme(Long themeId) { this.id = themeId; }
    public void updateThemeLikeCheck(boolean themeLikeCheck){this.themeLikeCheck = themeLikeCheck;}
    public void updateTotalLikeCnt(int totalLikeCnt) { this.totalLikeCnt = totalLikeCnt; }
    public void updateThemeScore(double themeScore) { this.themeScore = themeScore; }
    public void updateReviewCnt(Long reviewCnt) { this.reviewCnt = reviewCnt; }
    public void updateThemeImgUrl(String imgUrl) { this.themeImgUrl = imgUrl; }
}