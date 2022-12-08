package com.example.sherlockescape.repository;

import com.example.sherlockescape.domain.Theme;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ThemeRepository extends JpaRepository<Theme, Long>, ThemeQueryRepository {
    List<Theme> findAllByCompanyId(Long companyId);

    Page<Theme> findAllByOrderByTotalLikeCntDesc(Pageable pageable);

}
