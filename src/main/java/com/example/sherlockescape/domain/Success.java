package com.example.sherlockescape.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Success {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "achieve_id")
    private Achievement achievement;


    public Success(Member member, Achievement achievement){
        this.member = member;
        this.achievement = achievement;
    }

}
