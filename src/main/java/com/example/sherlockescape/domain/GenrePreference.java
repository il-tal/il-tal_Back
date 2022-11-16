package com.example.sherlockescape.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class GenrePreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String genrePreference;

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "tendency_id")
    private Tendency tendency;

    public void updateGenrePreference(String genrePreference){
        this.genrePreference = genrePreference;
    }
}
