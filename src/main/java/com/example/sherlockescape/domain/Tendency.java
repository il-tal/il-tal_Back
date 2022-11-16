package com.example.sherlockescape.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tendency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

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

    public void updateTendency(int lessScare, int roomSize,int lockStyle, int device,
                               int interior, int excitePreference, int surprise)
    {
        this.lessScare = lessScare;
        this.roomSize = roomSize;
        this.lockStyle = lockStyle;
        this.device = device;
        this.interior = interior;
        this.excitePreference = excitePreference;
        this.surprise = surprise;
    }
}
