package com.example.sherlockescape.repository;

import com.example.sherlockescape.domain.Company;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.example.sherlockescape.domain.QCompany.company;

@RequiredArgsConstructor
public class CompanyRepositoryImpl implements CompanyRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public Page<Company> getCompanyList(Pageable pageable, String location) {
        List<Company> result = jpaQueryFactory
                .selectFrom(company)
                .where(eqLocation(location))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .orderBy(company.id.asc())
                .fetch();

        long totalSize = jpaQueryFactory
                .selectFrom(company)
                .where(
                        eqLocation(location)
                )
                .fetch().size();

        return new PageImpl<> (result, pageable, totalSize);
    }

    //업체 이름 검색
    @Override
    public Page<Company> findByCompanyName(Pageable pageable, String companyName) {
        List<Company> result = jpaQueryFactory
                .selectFrom(company)
                .where(eqCompanyName(companyName))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .orderBy(company.id.desc())
                .fetch();

        long totalSize = jpaQueryFactory
                .selectFrom(company)
                .where(eqCompanyName(companyName))
                .fetch().size();

        return new PageImpl<> (result, pageable, totalSize);
    }


    private BooleanExpression eqCompanyName(String companyName) {
        return companyName != null ? company.companyName.contains(companyName) : null;
    }

    private BooleanExpression eqLocation(String location) {
        return location != null ? company.location.eq(location) : null;
    }
}
