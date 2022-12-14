package com.example.sherlockescape.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Builder
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String refreshToken;
    @NotBlank
    private String memberUsername;

    public RefreshToken(String token, String username) {
        this.refreshToken = token;
        this.memberUsername = username;
    }

    public RefreshToken updateToken(String token) {
        this.refreshToken = token;
        return this;
    }
}