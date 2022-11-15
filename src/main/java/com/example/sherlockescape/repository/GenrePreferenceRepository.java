package com.example.sherlockescape.repository;

import com.example.sherlockescape.domain.GenrePreference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GenrePreferenceRepository extends JpaRepository<GenrePreference, Long> {

    List<GenrePreference> findAllByTendencyId(Long id);
}
