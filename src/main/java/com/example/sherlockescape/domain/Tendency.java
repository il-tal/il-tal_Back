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
public class Tendency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false)
    private String genrePreference;

    @Column(nullable = false)
    private String stylePreference;

    @Column(nullable = false)
    private int lessScare;

    @Column(nullable = false)
    private int roomSize;

    @Column(nullable = false)
    private int lockStyle;

    @Column(nullable = false)
    private int device;

    @Column(nullable = false)
    private int interior;

    @Column(nullable = false)
    private int excitePreference;

    @Column(nullable = false)
    private int surprise;
}
