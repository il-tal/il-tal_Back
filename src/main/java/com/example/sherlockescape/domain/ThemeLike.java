package com.example.sherlockescape.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ThemeLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "themeLike_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "theme_id")
    private Theme theme;

    public ThemeLike(Member member, Theme theme){
        this.member = member;
        this.theme = theme;
    }

}