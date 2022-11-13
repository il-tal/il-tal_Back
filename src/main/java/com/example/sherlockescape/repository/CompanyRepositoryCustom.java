package com.example.sherlockescape.repository;


import com.example.sherlockescape.domain.Company;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CompanyRepositoryCustom {
    List<Company> getCompanyList(Pageable pageable, String location);
}
