package com.example.sherlockescape.repository;

import com.example.sherlockescape.domain.Company;
import com.example.sherlockescape.domain.Theme;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ThemeRepository extends JpaRepository<Theme, Long>, ThemeQueryRepository {

    List<Theme> findAllByCompanyId(Long companyId);


//    Page<Theme> findAllByOrderByThemeLikeCntDesc(Pageable pageable);

//    Long countById(Long id);

}
