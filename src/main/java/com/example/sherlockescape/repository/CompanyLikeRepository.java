package com.example.sherlockescape.repository;

import com.example.sherlockescape.domain.CompanyLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CompanyLikeRepository extends JpaRepository<CompanyLike, Long> {

    Optional<CompanyLike> findByCompanyIdAndMemberId(long parseLong, Long memberId);
    Long countByCompanyId(long parseLong);
    Optional<CompanyLike> findByCompanyId(Long companyId);
    List<CompanyLike> findCompanyLikesByMemberId(Long memberId);
}
