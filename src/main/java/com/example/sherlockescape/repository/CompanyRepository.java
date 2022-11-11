package com.example.sherlockescape.repository;

import com.example.sherlockescape.domain.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
}
