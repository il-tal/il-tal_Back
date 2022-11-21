package com.example.sherlockescape.domain;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;

import static com.example.sherlockescape.domain.QCompany.*;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
class CompanyTest {

    @Autowired
    EntityManager em;

    JPAQueryFactory jpaQueryFactory;

    //업체 전체 조회
    @Test
    public void showCompany(){
        jpaQueryFactory = new JPAQueryFactory(em);
        List<Company> companyList =  jpaQueryFactory
                .selectFrom(company)
                .fetch();
    }
}