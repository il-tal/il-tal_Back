package com.example.sherlockescape.repository;

import com.example.sherlockescape.domain.Member;
import com.example.sherlockescape.domain.Tendency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TendencyRepository extends JpaRepository<Tendency, Long> {

    Tendency findByMember(Member member);
}
