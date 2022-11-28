package com.example.sherlockescape.repository;


import com.example.sherlockescape.domain.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CompanyRepositoryCustom {
    Page<Company> getCompanyList(Pageable pageable, String location);
}
