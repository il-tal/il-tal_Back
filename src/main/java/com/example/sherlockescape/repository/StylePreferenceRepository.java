package com.example.sherlockescape.repository;

import com.example.sherlockescape.domain.StylePreference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StylePreferenceRepository extends JpaRepository<StylePreference, Long> {

    List<StylePreference> findAllByTendencyId(Long id);

    List<StylePreference> findStylePreferencesByTendencyId(Long id);

    StylePreference findByTendencyId(Long id);
}
