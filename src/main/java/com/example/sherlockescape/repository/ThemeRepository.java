package com.example.sherlockescape.repository;

import com.example.sherlockescape.domain.Company;
import com.example.sherlockescape.domain.Theme;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ThemeRepository extends JpaRepository<Theme, Long>, ThemeQueryRepository {
}
