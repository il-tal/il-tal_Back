package com.example.sherlockescape.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Builder
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Badge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "badge_id")
    private Long id;

    @Column(nullable = false)
    private String badgeImgUrl;

    @Column(nullable = false)
    private String badgeName;
    //긴 글을 작성할때 사용함
    @Lob
    private String badgeExplain;

}
