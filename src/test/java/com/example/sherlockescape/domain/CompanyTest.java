//package com.example.sherlockescape.domain;
//
//import com.querydsl.jpa.impl.JPAQueryFactory;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.persistence.EntityManager;
//
//import java.util.List;
//
//import static com.example.sherlockescape.domain.QCompany.*;
//import static org.assertj.core.api.Assertions.assertThat;
//
//
//@SpringBootTest
//@Transactional
//class CompanyTest {
//
//    @Autowired
//    EntityManager em;
//
//    JPAQueryFactory jpaQueryFactory;
//
//    //업체 전체 조회 (해당 지역에 있는 업체의 테마 까지 전부 조회)
//    @Test
//    public void showCompany(){
//        jpaQueryFactory = new JPAQueryFactory(em);
//        List<Company> findCompanyByLocation =  jpaQueryFactory
//                .selectFrom(company)
//                .where(company.location.eq("강남"))
//                .orderBy(company.id.asc())
//                .fetch();
//
//        for(Company company1: findCompanyByLocation){
//            assertThat(company1.getLocation().equals("강남"));
//        }
//    }
//}