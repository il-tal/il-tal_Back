package com.example.sherlockescape.repository;

import com.example.sherlockescape.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByMemberUsername(String usernameFromToken);
    void deleteByMemberUsername(String username);

    Optional<RefreshToken> findById(Long id);

}