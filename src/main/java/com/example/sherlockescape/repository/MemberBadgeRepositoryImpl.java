package com.example.sherlockescape.repository;

import com.example.sherlockescape.domain.Company;
import com.example.sherlockescape.domain.MemberBadge;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;

import static com.example.sherlockescape.domain.QCompany.company;

@Repository
@RequiredArgsConstructor
public class MemberBadgeRepositoryImpl implements MemberBadgeRepositoryCustom{

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Page<MemberBadge> findAllByMemberUsername(Pageable pageable, String username);


	return ;
}

