package com.example.sherlockescape.repository;

import com.example.sherlockescape.domain.Company;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.example.sherlockescape.domain.QCompany.company;

@RequiredArgsConstructor
public class CompanyRepositoryImpl implements CompanyRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public List<Company> getCompanyList(Pageable pageable, String location) {
        return jpaQueryFactory
                .selectFrom(company)
                .where(company.location.eq(location))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .orderBy(company.id.asc())
                .fetch();
    }

}
