package com.example.sherlockescape.repository;

import com.example.sherlockescape.domain.CompanyLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CompanyLikeRepository extends JpaRepository<CompanyLike, Long> {
    Long countByCompanyId(long parseLong);
    Optional<CompanyLike> findByCompanyIdAndMemberUsername(Long companyId, String username);
    List<CompanyLike> findCompanyLikesByMemberUsername(String username);
}
